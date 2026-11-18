package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminCategoriesDiscount;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateCategoryDiscountServlet", value = "/UpdateCategoryDiscountServlet")
public class UpdateCategoryDiscountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.CategoryDiscountService service = new vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.CategoryDiscountService();
                var discount = service.getDiscountById(id);
                request.setAttribute("editDiscount", discount);
                request.getRequestDispatcher("/admin-category-promotions.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                // log error if needed
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/category-discounts");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String categoryStr = request.getParameter("category");
        String discountStr = request.getParameter("discount_percentage");
        String startStr = request.getParameter("start_date");
        String endStr = request.getParameter("end_date");
        try {
            int id = Integer.parseInt(idStr);
            int categoryId = Integer.parseInt(categoryStr);
            int discountPercentage = Integer.parseInt(discountStr);
            java.sql.Date startDate = java.sql.Date.valueOf(startStr);
            java.sql.Date endDate = java.sql.Date.valueOf(endStr);
            vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.CategoryDiscount cd = new vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.CategoryDiscount();
            cd.setId(id);
            cd.setCategoryId(categoryId);
            cd.setDiscountPercentage(discountPercentage);
            cd.setStartDate(startDate);
            cd.setEndDate(endDate);
            vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.CategoryDiscountService service = new vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.CategoryDiscountService();
            boolean result = service.updateDiscount(cd);
        } catch (Exception e) {
        }
        response.sendRedirect(request.getContextPath() + "/admin/category-discounts");
    }
}