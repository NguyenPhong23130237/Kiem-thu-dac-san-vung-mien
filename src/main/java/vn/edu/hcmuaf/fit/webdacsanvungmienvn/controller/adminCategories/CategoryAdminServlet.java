package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminCategories;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductCategoryService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductCategory;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryAdminServlet", urlPatterns = {
        "/admin/categories"
})
public class CategoryAdminServlet extends HttpServlet {

    private final ProductCategoryService productCategoryService = new ProductCategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/admin/categories".equals(servletPath)) {
            handleList(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int pageSize = 10;
        int page = 1;
        String search = request.getParameter("search");
        if (search != null) search = search.trim();
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException ignored) {
        }
        if (page < 1) page = 1;

        int totalItems;
        int totalPages;
        List<ProductCategory> categories;
        if (search != null && !search.isEmpty()) {
            totalItems = productCategoryService.countSearchProductCategories(search);
            totalPages = (int) Math.ceil(totalItems / (double) pageSize);
            if (totalPages < 1) totalPages = 1;
            if (page > totalPages) page = totalPages;
            categories = productCategoryService.searchProductCategories(search, page, pageSize);
        } else {
            totalItems = productCategoryService.countAllCategories();
            totalPages = (int) Math.ceil(totalItems / (double) pageSize);
            if (totalPages < 1) totalPages = 1;
            if (page > totalPages) page = totalPages;
            categories = productCategoryService.getCategoriesPaged(page, pageSize);
        }
        vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductService productService = new vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductService();
        for (vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductCategory cat : categories) {
            cat.setProductCount(productService.countCategoryProducts(cat.getId()));
        }
        request.setAttribute("categories", categories);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);

        request.getRequestDispatcher("/admin-categories.jsp").forward(request, response);
    }
}
