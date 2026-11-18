package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.brands;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Brand;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.BrandService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.PaginationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BrandsServlet", value = "/brands")
public class BrandsServlet extends HttpServlet {
    private final BrandService brandService = new BrandService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Product> products = new ArrayList<>();
        int totalProducts = 0;
        if (request.getParameter("brandId") != null) {
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            products = productService.getBrandProducts(brandId, 20, page);
            totalProducts = productService.countBrandProducts(brandId);
            request.setAttribute("brandId", brandId);
        }

        List<Brand> brands = brandService.getAllBrands();
        int totalPages = PaginationUtil.calculateTotalPages(totalProducts, 20);

        request.setAttribute("brands", brands);
        request.setAttribute("products", products);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);

        request.getRequestDispatcher("brands.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}