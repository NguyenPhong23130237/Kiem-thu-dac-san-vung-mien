package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminBanners;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.BannerService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.CloudinaryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "AddBannerServlet", value = "/admin/banner/add")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 20 * 1024 * 1024 // 20MB
)
public class AddBannerServlet extends HttpServlet {
    private final BannerService bannerService = new BannerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Part imagePart = request.getPart("bannerImage");
        if (imagePart == null || imagePart.getSize() == 0) {
            response.sendRedirect(request.getContextPath() + "/admin/banners?error=empty");
            return;
        }

        String savedRelativePath = CloudinaryUtil.saveUpload(imagePart, "banners");
        if (savedRelativePath == null) {
            response.sendRedirect(request.getContextPath() + "/admin/banners?error=upload");
            return;
        }

        int newId = bannerService.createBanner(savedRelativePath);
        if (newId <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/banners?error=db");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/banners?success=1");
    }
}
