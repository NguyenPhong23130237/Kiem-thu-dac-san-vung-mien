package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Order;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public int createOrder(Cart cart) {
        String query = "INSERT INTO orders (user_id, payment_method, full_name, " +
                "phone_number, address, note, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cart.getUser().getId());
            ps.setString(2, cart.getPaymentMethod());
            ps.setString(3, cart.getShippingInfo().getReceiverName());
            ps.setString(4, cart.getShippingInfo().getReceiverPhoneNumber());
            ps.setString(5, cart.getShippingInfo().getFullAddress());
            ps.setString(6, cart.getNote());
            ps.setDouble(7, cart.getTotal());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE user_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
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
                order.setSignature(rs.getString("signature"));
                order.setKeyId(rs.getInt("key_id"));
                order.setSigned_at(rs.getDate("signed_at"));
                order.setSignature_algorithm(rs.getString("signature_algorithm"));
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
                order.setSignature(rs.getString("signature"));
                order.setKeyId(rs.getInt("key_id"));
                order.setSigned_at(rs.getDate("signed_at"));
                order.setSignature_algorithm(rs.getString("signature_algorithm"));
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasUserOrderedProduct(int userId, int productId) {
        String query = "SELECT COUNT(*) AS count FROM orders o " +
                "JOIN order_items oi ON o.id = oi.order_id " +
                "WHERE o.user_id = ? AND oi.product_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY date ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
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
                order.setSignature(rs.getString("signature"));
                order.setKeyId(rs.getInt("key_id"));
                order.setSigned_at(rs.getDate("signed_at"));
                order.setSignature_algorithm(rs.getString("signature_algorithm"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersPaged(int page, int pageSize) {
        List<Order> orders = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM orders ORDER BY date ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
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
                order.setSignature(rs.getString("signature"));
                order.setKeyId(rs.getInt("key_id"));
                order.setSigned_at(rs.getDate("signed_at"));
                order.setSignature_algorithm(rs.getString("signature_algorithm"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrderPagedByUserId(int userId, int page, int pageSize) {
        List<Order> orders = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY date ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, pageSize);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
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
                order.setSignature(rs.getString("signature"));
                order.setKeyId(rs.getInt("key_id"));
                order.setSigned_at(rs.getDate("signed_at"));
                order.setSignature_algorithm(rs.getString("signature_algorithm"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public int countAllOrders() {
        String sql = "SELECT COUNT(DISTINCT o.id) AS cnt FROM orders o LEFT JOIN order_items oi ON o.id = oi.order_id";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countOrdersBySearch(String keyword) {
        String sql = "SELECT COUNT(DISTINCT o.id) AS cnt FROM orders o LEFT JOIN order_items oi ON o.id = oi.order_id " +
                "WHERE o.full_name LIKE ? OR o.phone_number LIKE ? OR o.address LIKE ?";
        String pattern = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOrderWithItems(int id) {
        String delItems = "DELETE FROM order_items WHERE order_id = ?";
        String delOrder = "DELETE FROM orders WHERE id = ?";
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(delItems)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }
            int rowsAffected;
            try (PreparedStatement ps2 = conn.prepareStatement(delOrder)) {
                ps2.setInt(1, id);
                rowsAffected = ps2.executeUpdate();
            }
            conn.commit();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception ex) { /* ignore */ }
            }
        }
        return false;
    }

    public boolean updateOrderStatus(int id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countAllOrderToday() {
        String sql = "SELECT COUNT(*) AS cnt FROM orders WHERE DATE(date) = CURDATE()";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double sumAllOrderMonth() {
        String sql = "SELECT SUM(oi.product_quantity * oi.product_price) AS sum_total " +
                "FROM orders o " +
                "JOIN order_items oi ON oi.order_id = o.id " +
                "WHERE MONTH(o.date) = MONTH(CURDATE()) " +
                "AND YEAR(o.date) = YEAR(CURDATE())";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                double result = rs.getDouble("sum_total");
                if (rs.wasNull()) return 0.0;
                return result;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public boolean isOrderSign(int id) {
        String sql = "SELECT signature FROM orders WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getString("signature") != null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateOrderSignature(int orderId, String signature, int keyId, String algorithm) {
        String sql = "UPDATE orders SET signature = ?, key_id = ?, signed_at = NOW(), signature_algorithm = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, signature);
            ps.setInt(2, keyId);
            ps.setString(3, algorithm);
            ps.setInt(4, orderId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
