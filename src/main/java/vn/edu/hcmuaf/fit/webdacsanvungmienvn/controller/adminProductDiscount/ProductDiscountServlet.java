package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminProductDiscount;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ProductDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductDiscount;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductDiscountService;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "ProductDiscountServlet", value = "/admin/product-discounts")
public class ProductDiscountServlet extends HttpServlet {
    private final ProductDiscountService discountService = new ProductDiscountService();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = discountService.deleteDiscount(id);
                System.out.println("[ProductDiscountServlet] delete id=" + id + " result=" + ok);
            } catch (Exception e) {
                System.err.println("[ProductDiscountServlet] delete failed: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/admin/product-discounts");
            return;
        }
        if ("edit".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                ProductDiscount edit = discountService.getDiscountById(id);
                request.setAttribute("editDiscount", edit);
            } catch (Exception e) {
                System.err.println("[ProductDiscountServlet] edit param error: " + e.getMessage());
            }
        }
        int page = 1;
        int pageSize = 10;
        String search = request.getParameter("search");
        if (search != null) search = search.trim();
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (Exception ignored) {}
        if (page < 1) page = 1;
        int totalItems;
        int totalPages;
        List<ProductDiscount> discounts;
        if (search != null && !search.isEmpty()) {
            totalItems = discountService.countSearchDiscounts(search);
            totalPages = (int) Math.ceil(totalItems / (double) pageSize);
            if (totalPages < 1) totalPages = 1;
            if (page > totalPages) page = totalPages;
            discounts = discountService.searchDiscountsPaged(search, page, pageSize);
        } else {
            totalItems = discountService.countAllDiscounts();
            totalPages = (int) Math.ceil(totalItems / (double) pageSize);
            if (totalPages < 1) totalPages = 1;
            if (page > totalPages) page = totalPages;
            discounts = discountService.getDiscountsPaged(page, pageSize);
        }
        List<Product> products = productDAO.getAllProducts();

        request.setAttribute("discounts", discounts);
        request.setAttribute("products", products);
        request.setAttribute("discountCount", discounts == null ? 0 : discounts.size());
        request.setAttribute("productCount", products == null ? 0 : products.size());
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("search", search);

        if ("1".equals(request.getParameter("debug"))) {
            response.setContentType("text/plain;charset=UTF-8");
            try {
                java.io.PrintWriter out = response.getWriter();
                out.println("discountsCount=" + (discounts == null ? 0 : discounts.size()));
                out.println("productsCount=" + (products == null ? 0 : products.size()));
                if (discounts != null) {
                    for (ProductDiscount d : discounts) {
                        out.println("id=" + d.getId() + ", productId=" + d.getProductId() + ", productName=" + d.getProductName() + ", percent=" + d.getDiscountPercentage() + ", start=" + d.getStartDate() + ", end=" + d.getEndDate() + ", status=" + ProductDiscount.getStatus(d.getStartDate(), d.getEndDate()));
                    }
                }
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        request.getRequestDispatcher("/admin-product-promotions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String productStr = request.getParameter("product");
        String discountStr = request.getParameter("discount_percentage");
        String startStr = request.getParameter("start_date");
        String endStr = request.getParameter("end_date");

        try {
            int productId = Integer.parseInt(productStr);
            int discountPercentage = Integer.parseInt(discountStr);
            Date startDate = Date.valueOf(startStr);
            Date endDate = Date.valueOf(endStr);

            ProductDiscount pd = new ProductDiscount();
            pd.setProductId(productId);
            pd.setDiscountPercentage(discountPercentage);
            pd.setStartDate(startDate);
            pd.setEndDate(endDate);

            boolean result;
            if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                pd.setId(id);
                result = discountService.updateDiscount(pd);
            } else {
                result = discountService.addDiscount(pd);
                if (!result) {
                    request.setAttribute("errorDuplicateProduct", true);
                    request.setAttribute("openAddModal", true); // yêu cầu mở modal add khi forward
                    request.setAttribute("addError", "Sản phẩm đã có khuyến mãi trùng thời gian.");
                    request.setAttribute("formProduct", productId);
                    request.setAttribute("formDiscount", discountPercentage);
                    request.setAttribute("formStart", startStr);
                    request.setAttribute("formEnd", endStr);
                    int page = 1;
                    int pageSize = 10;
                    List<ProductDiscount> discounts = discountService.getDiscountsPaged(page, pageSize);
                    List<Product> products = productDAO.getAllProducts();
                    request.setAttribute("discounts", discounts);
                    request.setAttribute("products", products);
                    // Provide pagination attributes so JSP computes STT correctly
                    int totalItems = discountService.countAllDiscounts();
                    int totalPages = (int) Math.ceil(totalItems / (double) pageSize);
                    if (totalPages < 1) totalPages = 1;
                    request.setAttribute("currentPage", page);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("pageSize", pageSize);
                    request.getRequestDispatcher("/admin-product-promotions.jsp").forward(request, response);
                    return;
                }
            }
            System.out.println("[ProductDiscountServlet] action=" + action + " result=" + result);
        } catch (Exception e) {
            System.err.println("[ProductDiscountServlet] failed to parse POST params: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/product-discounts");
    }
}
