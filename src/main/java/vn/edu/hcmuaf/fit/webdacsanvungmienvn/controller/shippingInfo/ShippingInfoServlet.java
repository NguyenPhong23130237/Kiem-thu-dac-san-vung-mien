package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.shippingInfo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ShippingInfoService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShippingInfoServlet", value = "/shipping-info")
public class ShippingInfoServlet extends HttpServlet {
    private final ShippingInfoService shippingInfoService = new ShippingInfoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        List<ShippingInfo> shippingInfos = shippingInfoService.getShippingInfosByUserId(user.getId());
        String mode = request.getParameter("mode");
        boolean isSelectMode = "select".equals(mode);
        request.setAttribute("isSelectMode", isSelectMode);
        String error = request.getParameter("error");
        String editId = request.getParameter("editId");
        String editError = request.getParameter("editError");
        if (error != null) request.setAttribute("error", error);
        if (editError != null) request.setAttribute("editError", editError);
        if (editId != null) request.setAttribute("editId", editId);
        request.setAttribute("shippingInfos", shippingInfos);
        request.getRequestDispatcher("shipping-info.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}