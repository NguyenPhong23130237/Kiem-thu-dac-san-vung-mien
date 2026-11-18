package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminProductDiscount;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteProductDiscountServlet", value = "/DeleteProductDiscountServlet")
public class DeleteProductDiscountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService service = new vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService();
                boolean result = service.deleteDiscount(id);
            } catch (Exception e) {
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/product-discounts");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}