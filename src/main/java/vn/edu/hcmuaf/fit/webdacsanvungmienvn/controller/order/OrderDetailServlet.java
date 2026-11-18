package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.order;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Order;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.OrderService;

import java.io.IOException;

@WebServlet(name = "OrderDetailServlet", value = "/order-detail")
public class OrderDetailServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getUserId() != user.getId()) {
            response.sendRedirect("order-history");
            return;
        }
        String signatureStatus = orderService.verifySignatureStatus(order);
        request.setAttribute("order", order);
        request.setAttribute("signatureStatus", signatureStatus);
        request.getRequestDispatcher("order-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}