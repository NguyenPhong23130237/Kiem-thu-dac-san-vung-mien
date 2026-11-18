package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminBrands;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.BrandService;

import java.io.IOException;

@WebServlet(name = "BrandAdminServlet", urlPatterns = {
        "/admin/brands"
})
public class BrandAdminServlet extends HttpServlet {

    private final BrandService brandService = new BrandService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/admin/brands".equals(servletPath)) {
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
        if (search != null && !search.isEmpty()) {
            totalItems = brandService.countSearchBrands(search);
            totalPages = (int) Math.ceil(totalItems / (double) pageSize);
            if (totalPages < 1) totalPages = 1;
            if (page > totalPages) page = totalPages;
            request.setAttribute("brands", brandService.searchBrands(search, page, pageSize));
        } else {
            totalItems = brandService.countAllBrands();
            totalPages = (int) Math.ceil(totalItems / (double) pageSize);
            if (totalPages < 1) totalPages = 1;
            if (page > totalPages) page = totalPages;
            request.setAttribute("brands", brandService.getBrandsPaged(page, pageSize));
        }
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);

        request.getRequestDispatcher("/admin-brands.jsp").forward(request, response);
    }
}
