package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.shippingInfo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ShippingInfoService;

import java.io.IOException;

@WebServlet(name = "DeleteShippingInfo", value = "/delete-shipping-info")
public class DeleteShippingInfo extends HttpServlet {
    private final ShippingInfoService shippingInfoService = new ShippingInfoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int shippingInfoId = Integer.parseInt(request.getParameter("shippingInfoId"));
        String mode = request.getParameter("mode");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        Cart cart = (Cart) session.getAttribute("cart");

        ShippingInfo shippingInfo = shippingInfoService.getShippingInfoById(shippingInfoId);
        if (shippingInfo == null || shippingInfo.getUserId() != user.getId()) {
            response.sendRedirect("shipping-info");
            return;
        }

        if (cart != null
                && cart.getShippingInfo() != null
                && cart.getShippingInfo().getId() == shippingInfoId) {

            cart.setShippingInfo(null);
        }
        shippingInfoService.deleteShippingInfo(shippingInfoId);
        String redirectUrl = "shipping-info";
        if (mode != null && !mode.isEmpty()) {
            redirectUrl += "?mode=" + mode;
        }
        response.sendRedirect(redirectUrl);
    }
}