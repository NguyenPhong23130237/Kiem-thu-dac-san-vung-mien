package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.checkout;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.OrderService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "ConfirmCheckout", value = "/confirm-checkout")
public class ConfirmCheckout extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String note = request.getParameter("note");
        String paymentMethod = request.getParameter("payment-method");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");
        if (user == null || cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect("my-cart");
            return;
        }
        if (cart.getShippingInfo() == null) {
            response.sendRedirect("checkout?error=" +
                    URLEncoder.encode("Vui lòng chọn thông tin giao hàng", StandardCharsets.UTF_8));
            return;
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            response.sendRedirect("checkout?error=" +
                    URLEncoder.encode("Vui lòng chọn phương thức thanh toán", StandardCharsets.UTF_8));
            return;
        }
        cart.setUser(user);
        cart.setNote(note);
        cart.setPaymentMethod(paymentMethod);

        try {
            int orderId = orderService.createOrder(cart);

            if ("Online".equalsIgnoreCase(paymentMethod)) {
                response.sendRedirect("online-payment?orderId=" + orderId);
                return;
            }
            session.removeAttribute("cart");
            response.sendRedirect("order-sign?orderId=" + orderId);
        } catch (IllegalArgumentException e) {
            response.sendRedirect("checkout?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }
}