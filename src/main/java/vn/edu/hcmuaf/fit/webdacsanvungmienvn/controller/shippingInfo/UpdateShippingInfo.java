package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.shippingInfo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.Cart;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ShippingInfoService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "UpdateShippingInfo", value = "/update-shipping-info")
public class UpdateShippingInfo extends HttpServlet {
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
        String receiverName = request.getParameter("receiverName");
        String receiverPhoneNumber = request.getParameter("receiverPhoneNumber");
        String province = request.getParameter("provinceName");
        String district = request.getParameter("districtName");
        String ward = request.getParameter("wardName");
        String detailedAddress = request.getParameter("detailedAddress");
        String mode = request.getParameter("mode");
        Cart cart = (Cart) session.getAttribute("cart");

        ShippingInfo shippingInfo = shippingInfoService.getShippingInfoById(shippingInfoId);
        if (shippingInfo == null || shippingInfo.getUserId() != user.getId()) {
            response.sendRedirect("shipping-info");
            return;
        }

        try {
            if (cart != null
                    && cart.getShippingInfo() != null
                    && cart.getShippingInfo().getId() == shippingInfoId) {
                ShippingInfo cartShippingInfo = cart.getShippingInfo();
                cartShippingInfo.setReceiverName(receiverName);
                cartShippingInfo.setReceiverPhoneNumber(receiverPhoneNumber);
                cartShippingInfo.setProvince(province);
                cartShippingInfo.setDistrict(district);
                cartShippingInfo.setWard(ward);
                cartShippingInfo.setDetailedAddress(detailedAddress);
            }
            ShippingInfo newShippingInfo = new ShippingInfo();
            newShippingInfo.setId(shippingInfoId);
            newShippingInfo.setUserId(user.getId());
            newShippingInfo.setReceiverName(receiverName);
            newShippingInfo.setReceiverPhoneNumber(receiverPhoneNumber);
            newShippingInfo.setProvince(province);
            newShippingInfo.setDistrict(district);
            newShippingInfo.setWard(ward);
            newShippingInfo.setDetailedAddress(detailedAddress);
            shippingInfoService.updateShippingInfo(newShippingInfo);
            String redirectUrl = "shipping-info";
            if (mode != null && !mode.isEmpty()) {
                redirectUrl += "?mode=" + mode;
            }
            response.sendRedirect(redirectUrl);
        } catch (IllegalArgumentException e) {
            String redirectUrl = "shipping-info?editError=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8) +
                    "&editId=" + shippingInfoId;
            if (mode != null && !mode.isEmpty()) {
                redirectUrl += "&mode=" + mode;
            }
            response.sendRedirect(redirectUrl);
        }
    }
}