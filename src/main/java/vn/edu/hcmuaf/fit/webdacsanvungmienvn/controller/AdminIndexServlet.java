package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.OrderService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AdminIndexServlet", urlPatterns = {"/admin"})
public class AdminIndexServlet extends HttpServlet {
    private static final OrderService orderService = new OrderService();
    private static final ProductService productService = new ProductService();
    private static final UserService userService = new UserService();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("admin", true);

        int totalProducts = 0;
        int ordersToday = 0;
        int totalUsers = 0;
        double monthlyRevenue = 0.0;

        try {
            totalProducts = productService.countAllProducts();
        } catch (Exception e) {
            System.err.println("[AdminIndexServlet] failed to get totalProducts: " + e.getMessage());
        }

        try {
            ordersToday = orderService.countAllOrderToday();
        } catch (Exception e) {
            System.err.println("[AdminIndexServlet] failed to get ordersToday: " + e.getMessage());
        }

        try {
            totalUsers = userService.countAllUsers();
        } catch (Exception e) {
            System.err.println("[AdminIndexServlet] failed to get totalUsers: " + e.getMessage());
        }

        try {
            monthlyRevenue = orderService.sumAllOrderMonth();
        } catch (Exception e) {
            System.err.println("[AdminIndexServlet] failed to get monthlyRevenue: " + e.getMessage());
        }

        String debug = request.getParameter("debug");
        if ("1".equals(debug)) {
            response.setContentType("text/plain;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("totalProducts=" + totalProducts);
                out.println("ordersToday=" + ordersToday);
                out.println("totalUsers=" + totalUsers);
                out.println("monthlyRevenue=" + monthlyRevenue);
                out.flush();
            }
            return;
        }

        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("ordersToday", ordersToday);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("monthlyRevenue", monthlyRevenue);

        request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
    }
}
