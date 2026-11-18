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

@WebServlet(name = "UpdateProductServlet", value = "/admin/product/update")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class UpdateProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_id");
            return;
        }

        Product existing = productService.getProductById(id);
        if (existing == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_id");
            return;
        }

        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String soldQuantityStr = request.getParameter("soldQuantity");
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
        int soldQuantity = 0;
        int brandId;
        int categoryId;
        try {
            price = Double.parseDouble(priceStr);
            quantity = Integer.parseInt(quantityStr);
            if (soldQuantityStr != null && !soldQuantityStr.trim().isEmpty()) {
                soldQuantity = Integer.parseInt(soldQuantityStr);
            }
            brandId = Integer.parseInt(brandIdStr);
            categoryId = Integer.parseInt(categoryIdStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_input");
            return;
        }

        if (price < 0 || quantity < 0 || soldQuantity < 0 || brandId <= 0 || categoryId <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=invalid_input");
            return;
        }

        // Optional: upload new image, otherwise keep existing
        String imagePath = existing.getImage();
        Part imagePart = null;
        try {
            imagePart = request.getPart("thumbnailImage");
        } catch (Exception ignored) {
        }
        if (imagePart != null && imagePart.getSize() > 0) {
            String savedRelativePath = CloudinaryUtil.saveUpload(imagePart, "productThumbnails");
            if (savedRelativePath == null) {
                response.sendRedirect(request.getContextPath() + "/admin/products?error=upload");
                return;
            }
            imagePath = savedRelativePath;
        }

        Product p = new Product();
        p.setId(id);
        p.setName(name.trim());
        p.setPrice(price);
        p.setQuantity(quantity);
        p.setSoldQuantity(soldQuantity);
        p.setBrandId(brandId);
        p.setCategoryId(categoryId);
        p.setRegion(region);
        p.setSpecification(specification.trim());
        p.setDescription(description);
        p.setImage(imagePath);

        boolean ok = productService.updateProduct(p);
        if (!ok) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=update_failed");
            return;
        }
        String currentPage = request.getParameter("currentPage");
        if (currentPage == null || currentPage.trim().isEmpty()) {
            currentPage = "1";
        }

        response.sendRedirect(request.getContextPath() + "/admin/products?page=" + currentPage + "&success=updated");
    }
}
