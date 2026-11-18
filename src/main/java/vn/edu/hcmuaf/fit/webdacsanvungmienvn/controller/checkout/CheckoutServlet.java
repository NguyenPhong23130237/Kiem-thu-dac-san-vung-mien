package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.checkout;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String error = request.getParameter("error");
        if (error != null) {
            request.setAttribute("error", error);
        }
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect("my-cart");
            return;
        }
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}