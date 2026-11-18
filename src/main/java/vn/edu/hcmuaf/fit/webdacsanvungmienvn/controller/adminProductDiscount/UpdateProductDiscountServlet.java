package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminProductDiscount;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateProductDiscountServlet", value = "/UpdateProductDiscountServlet")
public class UpdateProductDiscountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService service = new vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService();
                var discount = service.getDiscountById(id);
                request.setAttribute("editDiscount", discount);
                request.getRequestDispatcher("/admin-product-promotions.jsp").forward(request, response);
                return;
            } catch (Exception e) {
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/product-discounts");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String productStr = request.getParameter("product");
        String discountStr = request.getParameter("discount_percentage");
        String startStr = request.getParameter("start_date");
        String endStr = request.getParameter("end_date");
        try {
            int id = Integer.parseInt(idStr);
            int productId = Integer.parseInt(productStr);
            int discountPercentage = Integer.parseInt(discountStr);
            java.sql.Date startDate = java.sql.Date.valueOf(startStr);
            java.sql.Date endDate = java.sql.Date.valueOf(endStr);
            vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductDiscount pd = new vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductDiscount();
            pd.setId(id);
            pd.setProductId(productId);
            pd.setDiscountPercentage(discountPercentage);
            pd.setStartDate(startDate);
            pd.setEndDate(endDate);
            vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService service = new vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService();
            boolean result = service.updateDiscount(pd);
        } catch (Exception e) {
        }
        response.sendRedirect(request.getContextPath() + "/admin/product-discounts");
    }
}