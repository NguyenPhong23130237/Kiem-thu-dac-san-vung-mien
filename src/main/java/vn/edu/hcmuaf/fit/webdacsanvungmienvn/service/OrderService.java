package vn.edu.hcmuaf.fit.webdacsanvungmienvn.service;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.algorithm.DSA;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.CartItem;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.GenKeyDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.OrderDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.OrderItemDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ProductDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.GenKey;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Order;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.OrderItem;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.OrderPayloadUtil;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.SHA256Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final GenKeyDAO keyDao = new GenKeyDAO();

    public int createOrder(Cart cart) {
        if (cart.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Vui lòng chọn phương thức thanh toán");
        }
        if (cart.getShippingInfo() == null) {
            throw new IllegalArgumentException("Vui lòng chọn thông tin giao hàng");
        }
        int userID = cart.getUser().getId();
        for (CartItem item : cart.getItems()) {
            Product product = productDAO.getProductById(item.getProduct().getId());
            if (product == null) {
                throw new IllegalArgumentException("Sản phẩm với ID " + item.getProduct().getId() + " không tồn tại");
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Số lượng sản phẩm với ID " + item.getProduct().getId() + " phải lớn hơn 0");
            }
            if (product.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm với ID " + item.getProduct().getId() + " không đủ số lượng trong kho");
            }
        }
        int orderId = orderDAO.createOrder(cart);
        for (CartItem item : cart.getItems()) {
            orderItemDAO.createOrderItem(orderId, item);
            productDAO.updateQuantity(item.getProduct().getId(), item.getQuantity());
        }
        return orderId;
    }

    private void normalizeStatus(Order order) {
        if (order == null) return;
        String s = order.getStatus();
        if (s == null || s.trim().isEmpty()) {
            order.setStatus("Đang xử lý");
            return;
        }

        String raw = s.trim();

        if (raw.equals("Đang xử lý") || raw.equals("Đang giao") || raw.equals("Đã giao") || raw.equals("Đã hủy")) {
            order.setStatus(raw);
            return;
        }

        String low = raw.toLowerCase();
        if (low.contains("process") || low.contains("pend") || low.contains("pending") || low.contains("xử") || low.contains("xu ly") || low.contains("chưa")) {
            order.setStatus("Đang xử lý");
            return;
        }
        if (low.contains("ship") || low.contains("shipping") || low.contains("shipped") || low.contains("đang giao") || low.contains("giao")) {
            order.setStatus("Đang giao");
            return;
        }
        if (low.contains("complete") || low.contains("completed") || low.contains("delivered") || low.contains("done") || low.contains("đã giao") || low.contains("da giao") || low.contains("hoàn thành")) {
            order.setStatus("Đã giao");
            return;
        }
        if (low.contains("cancel") || low.contains("canceled") || low.contains("cancelled") || low.contains("hủy") || low.contains("huy")) {
            order.setStatus("Đã hủy");
            return;
        }
        if (low.contains("paid") || low.contains("thanh toán") || low.contains("online")) {
            order.setStatus("Đã giao");
            return;
        }

        order.setStatus(raw);
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = orderDAO.getOrdersByUserId(userId);
        for (Order order : orders) {
            order.setOrderItems(orderItemDAO.getOrderItemsByOrderId(order.getId()));
        }
        return orders;
    }

    public Order getOrderById(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        order.setOrderItems(orderItemDAO.getOrderItemsByOrderId(orderId));
        return order;
    }
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = orderDAO.getAllOrders();
        for (Order order : orders) {
            normalizeStatus(order);
            order.setOrderItems(orderItemDAO.getOrderItemsByOrderId(order.getId()));
        }
        return orders;
    }
    public List<Order> getOrdersPaged(int page, int pageSize) throws SQLException {
        List<Order> orders = orderDAO.getOrdersPaged(page, pageSize);
        for (Order order : orders) {
            normalizeStatus(order);
            order.setOrderItems(orderItemDAO.getOrderItemsByOrderId(order.getId()));
        }
        return orders;
    }
    public boolean deleteOrder(int id) {
        if (id <= 0) return false;
        return orderDAO.deleteOrderWithItems(id);
    }

    public boolean updateOrderStatus(int id, String status) {
        if (id <= 0 || status == null) return false;
        String raw = status.trim();
        if (raw.isEmpty()) return false;

        String low = raw.toLowerCase();

        String mapped;
        if (raw.equals("Đang xử lý") || raw.equals("Đang giao") || raw.equals("Đã giao") || raw.equals("Đã hủy")) {
            mapped = raw;
        } else if (low.contains("processing") || low.contains("xử") || low.contains("xu ly") || low.contains("pending")) {
            mapped = "Đang xử lý";
        } else if (low.contains("shipping") || low.contains("giao") || low.contains("ship")) {
            mapped = "Đang giao";
        } else if (low.contains("completed") || low.contains("đã giao") || low.contains("da giao") || low.contains("delivered") || low.contains("done")) {
            mapped = "Đã giao";
        } else if (low.contains("cancel") || low.contains("hủy") || low.contains("huy")) {
            mapped = "Đã hủy";
        } else {
            mapped = raw;
        }

        return orderDAO.updateOrderStatus(id, mapped);
    }

    public List<Order> searchOrders(String keyword) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders " +
                "WHERE full_name LIKE ? OR phone_number LIKE ? OR address LIKE ? " +
                "ORDER BY date ASC";
        String pattern = "%" + keyword.trim() + "%";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setStatus(rs.getString("status"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setFullName(rs.getString("full_name"));
                    order.setPhoneNumber(rs.getString("phone_number"));
                    order.setAddress(rs.getString("address"));
                    Timestamp ts = rs.getTimestamp("date");
                    order.setDate(ts != null ? new java.util.Date(ts.getTime()) : null);
                    order.setNote(rs.getString("note"));
                    order.setTotal(rs.getDouble("total"));
                    normalizeStatus(order);
                    order.setOrderItems(orderItemDAO.getOrderItemsByOrderId(order.getId()));
                    orders.add(order);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return orders;
        }
    }
    public List<Order> getOrderPagedByUserId(int userId, int page, int pageSize) {
        List<Order> orders = orderDAO.getOrderPagedByUserId(userId, page, pageSize);
        for (Order order : orders) {
            normalizeStatus(order);
            order.setOrderItems(orderItemDAO.getOrderItemsByOrderId(order.getId()));
        }
        return orders;
    }

    public int countAllOrders() {
        return orderDAO.countAllOrders();
    }

    public int countOrdersBySearch(String keyword) {
        return orderDAO.countOrdersBySearch(keyword);
    }

    public int countAllOrderToday(){
        return orderDAO.countAllOrderToday();
    }
    public double sumAllOrderMonth() {
        return orderDAO.sumAllOrderMonth();
    }

    public boolean updateOrderSignature(int orderId, String signature, int keyId, String algorithm) {
        return orderDAO.updateOrderSignature(orderId, signature, keyId, algorithm);
    }

    public String verifySignatureStatus(Order order) {
        if (order == null) return "UNSIGNED";
        String signature = order.getSignature();
        if (signature == null || signature.trim().isEmpty()) {
            return "UNSIGNED";
        }
        int keyId = order.getKeyId();
        if (keyId <= 0) return "INVALID";
        GenKey key = GenKeyDAO.getKeyById(keyId);
        if (key == null || key.getPublicKey() == null) return "INVALID";
        if (key.getUserId() != order.getUserId()) return "INVALID";
        order.setPublicKey(key.getPublicKey());
        try {
            String payload = OrderPayloadUtil.buildOrderPayload(order);
            String hash = SHA256Util.checksum(payload);
            DSA dsa = new DSA();
            dsa.loadPublicKey(key.getPublicKey());
            boolean valid = dsa.verify(hash, signature.trim());
            return valid ? "VALID" : "INVALID";
        } catch (Exception e) {
            e.printStackTrace();
            return "INVALID";
        }
    }

    public Map<Integer, String> buildSignatureStatusMap(List<Order> orders) {
        Map<Integer, String> map = new HashMap<>();
        if (orders == null) return map;
        for (Order order : orders) {
            map.put(order.getId(), verifySignatureStatus(order));
        }
        return map;
    }
}

