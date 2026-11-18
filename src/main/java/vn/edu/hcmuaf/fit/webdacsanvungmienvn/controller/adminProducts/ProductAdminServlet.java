package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminProducts;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.BrandService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductCategoryService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "ProductAdminServlet", urlPatterns = {
        "/admin/products",
        "/admin/products/view"
})
public class ProductAdminServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final BrandService brandService = new BrandService();
    private final ProductCategoryService productCategoryService = new ProductCategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/admin/products".equals(servletPath)) {
            handleList(request, response);
            return;
        }

        if ("/admin/products/view".equals(servletPath)) {
            handleView(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String search = request.getParameter("search");
        int pageSize = 10;
        int page = 1;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException ignored) {
        }
        if (page < 1) page = 1;

        if (search != null && !search.trim().isEmpty()) {
            request.setAttribute("products", productService.getSearchProducts(search.trim(), Integer.MAX_VALUE, 1));
            request.setAttribute("totalItems", productService.countSearchProducts(search.trim()));
            request.setAttribute("currentPage", 1);
            request.setAttribute("pageSize", Integer.MAX_VALUE);
            request.setAttribute("totalPages", 1);
        } else {
            int totalItems = productService.countAllProducts("all");
            int totalPages = (int) Math.ceil(totalItems / (double) pageSize);
            if (totalPages < 1) totalPages = 1;
            if (page > totalPages) page = totalPages;

            request.setAttribute("products", productService.getAllProducts("all", pageSize, page));
            request.setAttribute("totalItems", totalItems);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalPages", totalPages);
        }
        request.setAttribute("brands", brandService.getBrandsPaged(1, Integer.MAX_VALUE));
        request.setAttribute("categories", productCategoryService.getAllProductCategories());
        request.getRequestDispatcher("/admin-products.jsp").forward(request, response);
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing/invalid id");
            return;
        }

        Product product = productService.getProductById(id);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(toJson(product));
        }
    }

    private String toJson(Product p) {
        return "{" +
                "\"id\":" + p.getId() + "," +
                "\"name\":\"" + esc(p.getName()) + "\"," +
                "\"price\":" + p.getPrice() + "," +
                "\"brandId\":" + p.getBrandId() + "," +
                "\"brandName\":\"" + esc(p.getBrandName()) + "\"," +
                "\"categoryId\":" + p.getCategoryId() + "," +
                "\"categoryName\":\"" + esc(p.getCategoryName()) + "\"," +
                "\"region\":\"" + esc(p.getRegion()) + "\"," +
                "\"quantity\":" + p.getQuantity() + "," +
                "\"soldQuantity\":" + p.getSoldQuantity() + "," +
                "\"specification\":\"" + esc(p.getSpecification()) + "\"," +
                "\"description\":\"" + esc(p.getDescription()) + "\"," +
                "\"image\":\"" + esc(p.getImage()) + "\"," +
                "\"createdAt\":\"" + esc(p.getCreatedAt() == null ? null : p.getCreatedAt().toString()) + "\"," +
                "\"discountPercentage\":" + p.getDiscountPercentage() + "," +
                "\"discountPrice\":" + p.getDiscountPrice() +
                "}";
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");
            String brandIdStr = request.getParameter("brandId");
            String categoryIdStr = request.getParameter("categoryId");
            String region = request.getParameter("region");
            String specification = request.getParameter("specification");
            String description = request.getParameter("description");
            String image = request.getParameter("image");

            if (name == null || name.trim().isEmpty() || image == null || image.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=empty_name");
                return;
            }

            if (productService.existsByNameAndImage(name.trim(), image.trim())) {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=duplicate");
                return;
            }

            double price;
            int quantity, brandId, categoryId;
            try {
                price = Double.parseDouble(priceStr);
                quantity = Integer.parseInt(quantityStr);
                brandId = Integer.parseInt(brandIdStr);
                categoryId = Integer.parseInt(categoryIdStr);
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_input");
                return;
            }

            Product p = new Product();
            p.setName(name.trim());
            p.setPrice(price);
            p.setQuantity(quantity);
            p.setSoldQuantity(0);
            p.setBrandId(brandId);
            p.setCategoryId(categoryId);
            p.setRegion(region);
            p.setSpecification(specification);
            p.setDescription(description);
            p.setImage(image.trim());

            int newId = productService.createProduct(p);
            if (newId <= 0) {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=db");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/products?success=created");
            return;
        }
    }
}
