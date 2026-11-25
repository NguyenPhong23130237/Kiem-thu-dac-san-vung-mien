package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminOrder;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Order;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UpdateOrderAdminServlet", value = "/UpdateOrderAdminServlet")
public class UpdateOrderAdminServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("orderId");
        if (idStr == null || idStr.isEmpty()) {
            idStr = request.getParameter("id");
        }
        if (idStr == null || idStr.isEmpty()) {
            // extra fallback for mismatched client field names
            idStr = request.getParameter("edit_id");
        }
        String status = request.getParameter("status");

        System.out.println("[UpdateOrderAdminServlet] Received POST with orderId=" + idStr + " status=" + status);

        try {
            java.util.Map<String, String[]> paramMap = request.getParameterMap();
            System.out.println("[UpdateOrderAdminServlet] Parameters:");
            for (java.util.Map.Entry<String, String[]> e : paramMap.entrySet()) {
                System.out.println("  " + e.getKey() + " = " + java.util.Arrays.toString(e.getValue()));
            }
        } catch (Exception ex) {
            System.out.println("[UpdateOrderAdminServlet] Failed to dump parameters: " + ex.getMessage());
        }

        try {
            System.out.println("[UpdateOrderAdminServlet] Headers:");
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String hn = headerNames.nextElement();
                if (hn.equalsIgnoreCase("x-requested-with") || hn.equalsIgnoreCase("content-type") || hn.equalsIgnoreCase("referer")) {
                    System.out.println("  " + hn + " = " + request.getHeader(hn));
                }
            }
        } catch (Exception ex) {
            System.out.println("[UpdateOrderAdminServlet] Failed to dump headers: " + ex.getMessage());
        }

        boolean success = false;
        String message;
        if (idStr == null || idStr.trim().isEmpty()) {
            message = "Missing order id (expected parameter: orderId)";
        } else if (status == null) {
            message = "Missing status value";
        } else {
            try {
                int id = Integer.parseInt(idStr.trim());

                Order order = orderService.getOrderById(id);
                if (order == null) {
                    message = "Không tìm thấy đơn hàng.";
                    System.out.println("[UpdateOrderAdminServlet] Blocked update for missing order id=" + id);
                } else if (orderService.isDeliveredStatus(order.getStatus())) {
                    message = "Không thể cập nhật trạng thái: đơn hàng đã giao.";
                    System.out.println("[UpdateOrderAdminServlet] Blocked update for delivered order id=" + id);
                } else {
                    String sigStatus = orderService.verifySignatureStatus(order);
                    if (!"VALID".equals(sigStatus)) {
                        String reason = "UNSIGNED".equals(sigStatus)
                                ? "Đơn hàng chưa được xác thực bằng chữ ký số."
                                : "Chữ ký số của đơn hàng không hợp lệ (dữ liệu đã bị thay đổi).";
                        message = "Không thể cập nhật trạng thái: " + reason;
                        System.out.println("[UpdateOrderAdminServlet] Blocked update for order " + id + " — sigStatus=" + sigStatus);
                    } else {
                        success = orderService.updateOrderStatus(id, status);
                        message = success ? "Cập nhật trạng thái thành công" : "Cập nhật trạng thái thất bại";
                        System.out.println("[UpdateOrderAdminServlet] updateOrderStatus returned=" + success + " for id=" + id + " status=" + status);
                    }
                }
            } catch (NumberFormatException e) {
                message = "Order id không hợp lệ";
                System.out.println("[UpdateOrderAdminServlet] Invalid order id format: '" + idStr + "'");
            } catch (Exception e) {
                message = "Lỗi khi cập nhật trạng thái";
                System.out.println("[UpdateOrderAdminServlet] Exception when updating order status: " + e.getMessage());
                e.printStackTrace();
            }
        }

        String xReq = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(xReq)) {
            response.setContentType("application/json;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"success\":" + (success ? "true" : "false") + ",\"message\":\"" + message.replace("\"","\\\"") + "\"}");
                out.flush();
            }
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("flashMessage", message);
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}