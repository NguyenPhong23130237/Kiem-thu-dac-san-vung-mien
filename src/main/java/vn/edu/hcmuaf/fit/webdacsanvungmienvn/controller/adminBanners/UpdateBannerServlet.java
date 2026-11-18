package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminBanners;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Banner;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.BannerService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.CloudinaryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "UpdateBannerServlet", value = "/admin/banner/update")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class UpdateBannerServlet extends HttpServlet {
    private final BannerService bannerService = new BannerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/banners?error=id");
            return;
        }

        Banner existing = bannerService.findById(id);
        if (existing == null) {
            response.sendRedirect(request.getContextPath() + "/admin/banners?error=notfound");
            return;
        }

        Part imagePart = request.getPart("bannerImage");
        String imagePath = existing.getImage();

        if (imagePart != null && imagePart.getSize() > 0) {
            String savedRelativePath = CloudinaryUtil.saveUpload(imagePart, "banners");
            if (savedRelativePath == null) {
                response.sendRedirect(request.getContextPath() + "/admin/banners?error=upload");
                return;
            }
            imagePath = savedRelativePath;
        }

        boolean ok = bannerService.updateBannerImage(id, imagePath);
        if (!ok) {
            response.sendRedirect(request.getContextPath() + "/admin/banners?error=db");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/banners?success=1");
    }
}

