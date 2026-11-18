package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.algorithm.DSA;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.GenKeyDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.GenKey;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Order;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.OrderService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.OrderPayloadUtil;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.SHA256Util;

import java.io.IOException;

@WebServlet(name = "OrderSignServlet", value = "/order-sign")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class OrderSignServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isBlank()) {
            response.sendError(400, "Missing orderId");
            return;
        }
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            response.sendError(400, "Invalid orderId");
            return;
        }
        request.setAttribute("orderId", orderId);
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            response.sendError(404);
            return;
        }
        if (order.getUserId() != user.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập hoặc ký đơn hàng này.");
            return;
        }
        String orderPayload = OrderPayloadUtil.buildOrderPayload(order);
        String orderHash = SHA256Util.checksum(orderPayload);
        System.out.println("Order Payload: " + orderPayload);
        System.out.println("Order Hash: " + orderHash);
        request.setAttribute("orderHash", orderHash);

        boolean keyRevoked = GenKeyDAO.isKeyRevoked(user.getId());

        request.setAttribute("keyRevoked", keyRevoked);

        GenKey activeKey = GenKeyDAO.getActiveKeyByUserId(user.getId());
        if (activeKey != null) {
            request.setAttribute("activeKey", activeKey);
        }

        request.getRequestDispatcher("order-sign.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        if (GenKeyDAO.isKeyRevoked(user.getId())) {
            response.getWriter().write("""
        {
            "success": false,
            "message": "Khóa của bạn đã bị báo mất hoặc thu hồi. Vui lòng tạo khóa mới trước khi xác thực."
        }
    """);
            return;
        }

        String orderIdStr = request.getParameter("orderId");
        String signatureText = request.getParameter("signatureText");

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Mã đơn hàng không hợp lệ.\"}");
            return;
        }

        if (signatureText == null || signatureText.isBlank()) {
            response.getWriter().write("{\"success\": false, \"message\": \"Vui lòng dán nội dung chữ ký số vào ô trống.\"}");
            return;
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Đơn hàng không tồn tại.\"}");
            return;
        }
        if (order.getUserId() != user.getId()) {
            response.getWriter().write("{\"success\": false, \"message\": \"Bạn không có quyền xác thực đơn hàng này.\"}");
            return;
        }

        GenKey activeKey = GenKeyDAO.getActiveKeyByUserId(user.getId());
        if (activeKey == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Bạn không có khóa nào đang active để xác thực đơn hàng. Vui lòng tạo khóa mới.\"}");
            return;
        }

        String orderPayload = OrderPayloadUtil.buildOrderPayload(order);
        String orderHash = SHA256Util.checksum(orderPayload);

        boolean isVerified = false;
        String sigAlgo = "SHA256withDSA";
        try {
            DSA dsa = new DSA();
            dsa.loadPublicKey(activeKey.getPublicKey());
            isVerified = dsa.verify(orderHash, signatureText);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi khi xác thực chữ ký: " + e.getMessage() + "\"}");
            return;
        }

        if (isVerified) {
            boolean updated = orderService.updateOrderSignature(orderId, signatureText.trim(), activeKey.getId(), sigAlgo);
            if (updated) {
                response.getWriter().write("{\"success\": true, \"redirect\": \"order-success?orderId=" + orderId + "\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Xác thực chữ ký thành công nhưng không thể cập nhật thông tin đơn hàng vào hệ thống.\"}");
            }
        } else {
            response.getWriter().write("{\"success\": false, \"message\": \"Chữ ký số không khớp với đơn hàng hoặc khóa công khai của bạn.\"}");
        }
    }
}