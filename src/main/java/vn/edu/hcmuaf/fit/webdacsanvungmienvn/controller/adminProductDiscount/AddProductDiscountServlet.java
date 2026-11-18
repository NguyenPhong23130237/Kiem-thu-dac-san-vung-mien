package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminProductDiscount;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ProductDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService;

import java.io.IOException;

@WebServlet(name = "AddProductDiscountServlet", value = "/AddProductDiscountServlet")
public class AddProductDiscountServlet extends HttpServlet {
    private final ProductDiscountService discountService = new ProductDiscountService();
    private final ProductDAO productDAO = new ProductDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String productIdStr = request.getParameter("productId");
        String discountPercentStr = request.getParameter("discountPercent");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        try {
            int productId = Integer.parseInt(productIdStr);
            double discountPercent = Double.parseDouble(discountPercentStr);
            java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
            java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);

            String productName = productDAO.getProductById(productId).getName();
            if (discountService.existsByProductName(productName)) {
                response.sendRedirect(request.getContextPath() + "/admin/product-discounts?error=duplicate_name");
                return;
            }

            boolean ok = discountService.addDiscount(productId, discountPercent, startDate, endDate);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/admin/product-discounts?success=added");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/product-discounts?error=add_failed");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/product-discounts?error=invalid_input");
        }
    }
}