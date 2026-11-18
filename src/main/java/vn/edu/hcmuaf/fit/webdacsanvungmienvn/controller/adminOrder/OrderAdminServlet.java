package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminOrder;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ProductDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Order;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.OrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderAdminServlet", value = "/admin/orders")
public class OrderAdminServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private static final int PAGE_SIZE = 10;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception ignored) {}
        int pageSize = PAGE_SIZE;
        try { String ps = request.getParameter("pageSize"); if (ps != null) pageSize = Math.max(1, Integer.parseInt(ps)); } catch (Exception ignored) {}

        String search = request.getParameter("search");

        List<Order> orders;
        int totalOrders;
        int totalPages;

        try {
            if (search != null && !search.trim().isEmpty()) {
                orders = orderService.searchOrders(search);
                totalOrders = orders.size();
                totalPages = (int) Math.max(1, Math.ceil((double) totalOrders / pageSize));
            } else {
                orders = orderService.getOrdersPaged(page, pageSize);
                totalOrders = orderService.countAllOrders();
                totalPages = (int) Math.max(1, Math.ceil((double) totalOrders / pageSize));
            }
            Map<Integer, String> signatureStatusMap = orderService.buildSignatureStatusMap(orders);
            request.setAttribute("orders", orders);
            request.setAttribute("signatureStatusMap", signatureStatusMap);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalItems", totalOrders);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("searchVal", search);
            request.setAttribute("search", search);

            request.getRequestDispatcher("/admin-orders.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        if ("add".equalsIgnoreCase(action)) {
            try {
                int userId;
                try {
                    userId = Integer.parseInt(request.getParameter("userId"));
                } catch (Exception ex) {
                    throw new IllegalArgumentException("User ID không hợp lệ");
                }

                String paymentMethod = request.getParameter("paymentMethod");
                String receiverName = request.getParameter("receiverName");
                String receiverPhone = request.getParameter("receiverPhone");
                String province = request.getParameter("province");
                String district = request.getParameter("district");
                String ward = request.getParameter("ward");
                String detailedAddress = request.getParameter("detailedAddress");
                String note = request.getParameter("note");

                if (receiverName == null || receiverName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Họ tên người nhận không được để trống");
                }
                if (receiverPhone == null || receiverPhone.trim().isEmpty()) {
                    throw new IllegalArgumentException("SĐT người nhận không được để trống");
                }
                // lightweight validation (same pattern as ShippingInfoService)
                if (!receiverPhone.trim().matches("^0\\d{9}$")) {
                    throw new IllegalArgumentException("SĐT người nhận không hợp lệ (phải đủ 10 số và bắt đầu bằng 0)");
                }
                if (province == null || province.trim().isEmpty()
                        || district == null || district.trim().isEmpty()
                        || ward == null || ward.trim().isEmpty()
                        || detailedAddress == null || detailedAddress.trim().isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập đầy đủ địa chỉ giao hàng");
                }

                User user = userDAO.getUserById(userId);
                if (user == null) {
                    throw new IllegalArgumentException("Không tìm thấy user với ID = " + userId);
                }

                String[] productIds = request.getParameterValues("productId");
                String[] quantities = request.getParameterValues("quantity");
                if (productIds == null || quantities == null || productIds.length == 0 || quantities.length == 0) {
                    throw new IllegalArgumentException("Vui lòng thêm ít nhất 1 sản phẩm");
                }
                if (productIds.length != quantities.length) {
                    throw new IllegalArgumentException("Danh sách sản phẩm không hợp lệ");
                }

                Cart cart = new Cart();
                cart.setUser(user);
                cart.setPaymentMethod(paymentMethod);
                cart.setNote(note);

                ShippingInfo si = new ShippingInfo();
                si.setUserId(userId);
                si.setReceiverName(receiverName.trim());
                si.setReceiverPhoneNumber(receiverPhone.trim());
                si.setProvince(province.trim());
                si.setDistrict(district.trim());
                si.setWard(ward.trim());
                si.setDetailedAddress(detailedAddress.trim());
                cart.setShippingInfo(si);

                for (int i = 0; i < productIds.length; i++) {
                    int pid;
                    int qty;
                    try {
                        pid = Integer.parseInt(productIds[i]);
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("Product ID không hợp lệ ở dòng " + (i + 1));
                    }
                    try {
                        qty = Integer.parseInt(quantities[i]);
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("Số lượng không hợp lệ ở dòng " + (i + 1));
                    }
                    if (qty <= 0) {
                        throw new IllegalArgumentException("Số lượng phải lớn hơn 0 ở dòng " + (i + 1));
                    }

                    Product p = productDAO.getProductById(pid);
                    if (p == null) {
                        throw new IllegalArgumentException("Sản phẩm với ID = " + pid + " không tồn tại");
                    }
                    cart.addItem(p, qty);
                }

                int newOrderId = orderService.createOrder(cart);
                HttpSession session = request.getSession();
                session.setAttribute("flashMessage", "Tạo đơn hàng thành công! Mã đơn: #DH" + newOrderId);
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;

            } catch (Exception ex) {
                HttpSession session = request.getSession();
                session.setAttribute("flashMessage", "Tạo đơn hàng thất bại: " + ex.getMessage());
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }
        }

        doGet(request, response);
    }
}