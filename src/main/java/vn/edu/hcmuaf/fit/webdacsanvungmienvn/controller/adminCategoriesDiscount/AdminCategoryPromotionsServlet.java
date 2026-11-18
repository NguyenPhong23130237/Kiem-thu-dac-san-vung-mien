package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminCategoriesDiscount;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ProductDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.CategoryDiscount;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Category;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductDiscount;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.CategoryDiscountService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductCategoryService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "AdminCategoryPromotionsServlet", urlPatterns = {"/admin/category-discounts"})
public class AdminCategoryPromotionsServlet extends HttpServlet {
    private final CategoryDiscountService categoryDiscountService = new CategoryDiscountService();
    private final ProductCategoryService productCategoryService = new ProductCategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = categoryDiscountService.deleteDiscount(id);
                System.out.println("[CategoryDiscountServlet] delete id=" + id + " result=" + ok);
            } catch (Exception e) {
                System.err.println("[CategoryDiscountServlet] delete failed: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/admin/category-discounts");
            return;
        }
        if ("edit".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                CategoryDiscount edit = categoryDiscountService.getDiscountById(id);
                request.setAttribute("editDiscount", edit);
            } catch (Exception e) {
                System.err.println("[CategoryDiscountServlet] edit param error: " + e.getMessage());
            }
        }
        int page = 1;
        int pageSize = 10;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (Exception ignored) {}
        if (page < 1) page = 1;
        String search = request.getParameter("search");
        List<CategoryDiscount> discounts;
        int totalItems;
        if (search != null && !search.trim().isEmpty()) {
            discounts = categoryDiscountService.searchCategoryDiscountsPaged(search.trim(), page, pageSize);
            totalItems = categoryDiscountService.countSearchCategoryDiscounts(search.trim());
            request.setAttribute("search", search);
        } else {
            discounts = categoryDiscountService.getDiscountsPaged(page, pageSize);
            totalItems = categoryDiscountService.countAllDiscounts();
        }
        int totalPages = (int) Math.ceil(totalItems / (double) pageSize);
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) page = totalPages;
        List<vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductCategory> productCategories = productCategoryService.getAllProductCategories();
        List<Category> categories = new java.util.ArrayList<>();
        java.util.Map<Integer, String> categoryIdToName = new java.util.HashMap<>();
        for (vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductCategory pc : productCategories) {
            categories.add(new Category(pc.getId(), pc.getName()));
            categoryIdToName.put(pc.getId(), pc.getName());
        }
        for (CategoryDiscount d : discounts) {
            if (d.getCategoryName() == null || d.getCategoryName().isEmpty()) {
                String name = categoryIdToName.get(d.getCategoryId());
                if (name != null) d.setCategoryName(name);
            }
        }
        request.setAttribute("discounts", discounts);
        request.setAttribute("categories", categories);
        request.setAttribute("discountCount", discounts == null ? 0 : discounts.size());
        request.setAttribute("categoryCount", categories == null ? 0 : categories.size());
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        if ("1".equals(request.getParameter("debug"))) {
            response.setContentType("text/plain;charset=UTF-8");
            try {
                java.io.PrintWriter out = response.getWriter();
                out.println("discountsCount=" + (discounts == null ? 0 : discounts.size()));
                out.println("categoriesCount=" + (categories == null ? 0 : categories.size()));
                if (discounts != null) {
                    for (CategoryDiscount d : discounts) {
                        out.println("id=" + d.getId() + ", categoryId=" + d.getCategoryId() + ", categoryName=" + d.getCategoryName() + ", percent=" + d.getDiscountPercentage() + ", start=" + d.getStartDate() + ", end=" + d.getEndDate() + ", status=" + CategoryDiscount.getStatus(d.getStartDate(), d.getEndDate()));
                    }
                }
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        request.getRequestDispatcher("/admin-category-promotions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String categoryStr = request.getParameter("category");
        String discountStr = request.getParameter("discount_percentage");
        String startStr = request.getParameter("start_date");
        String endStr = request.getParameter("end_date");
        try {
            int categoryId = Integer.parseInt(categoryStr);
            int discountPercentage = Integer.parseInt(discountStr);
            Date startDate = Date.valueOf(startStr);
            Date endDate = Date.valueOf(endStr);
            CategoryDiscount cd = new CategoryDiscount();
            cd.setCategoryId(categoryId);
            cd.setDiscountPercentage(discountPercentage);
            cd.setStartDate(startDate);
            cd.setEndDate(endDate);
            boolean result;
            if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                cd.setId(id);
                result = categoryDiscountService.updateDiscount(cd);
            } else {
                result = categoryDiscountService.addDiscount(cd);
                if (!result) {
                    request.setAttribute("errorDuplicateCategory", true);
                    request.setAttribute("formCategory", categoryId);
                    request.setAttribute("formDiscount", discountPercentage);
                    request.setAttribute("formStart", startStr);
                    request.setAttribute("formEnd", endStr);
                    int page = 1;
                    int pageSize = 10;
                    List<CategoryDiscount> discounts = categoryDiscountService.getDiscountsPaged(page, pageSize);
                    List<vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductCategory> productCategories = productCategoryService.getAllProductCategories();
                    List<Category> categories = new java.util.ArrayList<>();
                    for (vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductCategory pc : productCategories) {
                        categories.add(new Category(pc.getId(), pc.getName()));
                    }
                    request.setAttribute("discounts", discounts);
                    request.setAttribute("categories", categories);
                    request.getRequestDispatcher("/admin-category-promotions.jsp").forward(request, response);
                    return;
                }
            }
            System.out.println("[CategoryDiscountServlet] action=" + action + " result=" + result);
        } catch (Exception e) {
            System.err.println("[CategoryDiscountServlet] failed to parse POST params: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin/category-discounts");
    }

}
