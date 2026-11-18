package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.shippingInfo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ShippingInfoService;

import java.io.IOException;

@WebServlet(name = "ChooseShippingInfo", value = "/choose-shipping-info")
public class ChooseShippingInfo extends HttpServlet {
    private final ShippingInfoService shippingInfoService = new ShippingInfoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        int shippingInfoId = Integer.parseInt(request.getParameter("shippingInfoId"));
        ShippingInfo shippingInfo = shippingInfoService.getShippingInfoById(shippingInfoId);
        if (shippingInfo == null || shippingInfo.getUserId() != user.getId()) {
            response.sendRedirect("shipping-info");
            return;
        }
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            response.sendRedirect("my-cart");
            return;
        }
        cart.setShippingInfo(shippingInfo);
        response.sendRedirect("checkout");
    }
}