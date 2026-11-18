package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.onlinePayment;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "OnlinePaymentServlet", value = "/online-payment")
public class OnlinePaymentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("orderId");
        if (orderId == null || orderId.isBlank()) {
            response.sendRedirect("checkout?error=" + URLEncoder.encode("Thiếu mã đơn để thanh toán", StandardCharsets.UTF_8));
            return;
        }
        request.setAttribute("orderId", orderId);
        request.getRequestDispatcher("online-payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}