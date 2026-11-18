package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.onlinePayment;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.OrderService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "ConfirmOnlinePayment", value = "/confirm-online-payment")
public class ConfirmOnlinePayment extends HttpServlet {
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
            response.sendRedirect("checkout?error=" + URLEncoder.encode("Thiếu mã đơn", StandardCharsets.UTF_8));
            return;
        }
        try {
            int orderId = Integer.parseInt(orderIdStr);
            session.removeAttribute("cart");
            response.sendRedirect("order-sign?orderId=" + orderId);
        } catch (IllegalArgumentException e) {
            response.sendRedirect("checkout?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}