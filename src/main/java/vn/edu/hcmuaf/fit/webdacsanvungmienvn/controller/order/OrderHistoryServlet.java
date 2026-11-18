package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.order;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Order;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.OrderService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderHistoryServlet", value = "/order-history")
public class OrderHistoryServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        System.out.println(user.getId());
        System.out.println(orders.size());
        Map<Integer, String> signatureStatusMap = orderService.buildSignatureStatusMap(orders);
        request.setAttribute("orders", orders);
        request.setAttribute("signatureStatusMap", signatureStatusMap);
        request.getRequestDispatcher("order-history.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}