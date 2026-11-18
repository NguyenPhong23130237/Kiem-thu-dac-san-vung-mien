package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminProducts;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.ProductService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.CloudinaryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "AddProductServlet", value = "/admin/product/add")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class AddProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String brandIdStr = request.getParameter("brandId");
        String categoryIdStr = request.getParameter("categoryId");
        String region = request.getParameter("region");
        String specification = request.getParameter("specification");
        String description = request.getParameter("description");

        if (name == null || name.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=empty_name");
            return;
        }
        if (specification == null || specification.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=empty_specification");
            return;
        }

        double price;
        int quantity;
        int brandId;
        int categoryId;
        try {
            price = Double.parseDouble(priceStr);
            quantity = Integer.parseInt(quantityStr);
            brandId = Integer.parseInt(brandIdStr);
            categoryId = Integer.parseInt(categoryIdStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_input");
            return;
        }

        if (price < 0 || quantity < 0 || brandId <= 0 || categoryId <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_input");
            return;
        }

        Part imagePart = request.getPart("thumbnailImage");
        if (imagePart == null || imagePart.getSize() == 0) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=upload");
            return;
        }

        String imagePath = CloudinaryUtil.saveUpload(imagePart, "productThumbnails");
        if (imagePath == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=upload");
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
        p.setSpecification(specification.trim());
        p.setDescription(description);
        p.setImage(imagePath);

        if (productService.existsByName(p.getName())) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=duplicate");
            return;
        }

        int newId = productService.createProduct(p);
        if (newId <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=db");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/products?success=created");
    }
}
