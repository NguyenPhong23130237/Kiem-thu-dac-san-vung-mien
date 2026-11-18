package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.shippingInfo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ShippingInfoService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AddShippingInfo", value = "/add-shipping-info")
public class AddShippingInfo extends HttpServlet {
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
        int userId = user.getId();
        String receiverName = request.getParameter("receiverName");
        String receiverPhoneNumber = request.getParameter("receiverPhoneNumber");
        String province = request.getParameter("provinceName");
        String district = request.getParameter("districtName");
        String ward = request.getParameter("wardName");
        String detailedAddress = request.getParameter("detailedAddress");
        String mode = request.getParameter("mode");

        try {
            ShippingInfo shippingInfo = new ShippingInfo();
            shippingInfo.setUserId(userId);
            shippingInfo.setReceiverName(receiverName.trim());
            shippingInfo.setReceiverPhoneNumber(receiverPhoneNumber.trim());
            shippingInfo.setProvince(province.trim());
            shippingInfo.setDistrict(district.trim());
            shippingInfo.setWard(ward.trim());
            shippingInfo.setDetailedAddress(detailedAddress.trim());
            shippingInfoService.addShippingInfo(shippingInfo);
            String redirectUrl = "shipping-info";
            if (mode != null && !mode.isEmpty()) {
                redirectUrl += "?mode=" + mode;
            }
            response.sendRedirect(redirectUrl);
        } catch (IllegalArgumentException e) {
            String redirectUrl = "shipping-info?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            if (mode != null && !mode.isEmpty()) {
                redirectUrl += "&mode=" + mode;
            }
            response.sendRedirect(redirectUrl);
        }
    }
}