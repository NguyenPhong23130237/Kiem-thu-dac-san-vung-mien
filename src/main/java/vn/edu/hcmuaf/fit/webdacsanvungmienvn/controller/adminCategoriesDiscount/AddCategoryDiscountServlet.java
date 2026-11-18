package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminCategoriesDiscount;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.CategoryDiscount;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.CategoryDiscountDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ProductCategoryDAO;

@WebServlet(name = "AddCategoryServlet", value = "/AddCategoryServlet")
public class AddCategoryDiscountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String categoryIdStr = request.getParameter("categoryId");
        String discountPercentStr = request.getParameter("discountPercent");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            double discountPercent = Double.parseDouble(discountPercentStr);
            java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
            java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);

            ProductCategoryDAO categoryDAO = new ProductCategoryDAO();
            if (!categoryDAO.existsById(categoryId)) {
                response.sendRedirect(request.getContextPath() + "/admin/category-discounts?error=category_not_found");
                return;
            }

            CategoryDiscount discount = new CategoryDiscount();
            discount.setCategoryId(categoryId);
            discount.setDiscountPercentage((int) discountPercent);
            discount.setStartDate(startDate);
            discount.setEndDate(endDate);

            CategoryDiscountDAO dao = new CategoryDiscountDAO();
            boolean ok = dao.insertCategoryDiscount(discount);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/admin/category-discounts?success=added");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/category-discounts?error=add_failed");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/category-discounts?error=invalid_input");
        }
    }
}