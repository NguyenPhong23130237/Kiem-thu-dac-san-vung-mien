package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.CloudinaryUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "RichTextUploadImage", value = "/rich-text-upload-image")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 20 * 1024 * 1024 // 20MB
)
public class RichTextUploadImage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        response.setContentType("application/json");
        Part imagePart = request.getPart("file");
        if (imagePart == null || imagePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"No file uploaded\"}");
            return;
        }
        try {
            String imageUrl;
            switch (type) {
                case "product":
                    imageUrl = CloudinaryUtil.saveUpload(imagePart, "richTextProducts");
                    break;
                case "news":
                    imageUrl = CloudinaryUtil.saveUpload(imagePart, "richTextNews");
                    break;
                default:
                    imageUrl = CloudinaryUtil.saveUpload(imagePart, "richTextOthers");
                    break;
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"location\":\"" + imageUrl + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Upload failed\"}");
        }
    }
}