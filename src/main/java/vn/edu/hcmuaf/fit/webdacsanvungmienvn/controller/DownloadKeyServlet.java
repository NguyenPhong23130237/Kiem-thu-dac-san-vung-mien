package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.GenKeyDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.GenKey;

@WebServlet(name = "downloadKeyServlet", value = "/downloadKey")
public class DownloadKeyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String idParam = request.getParameter("id");

        if (type == null || type.isBlank()) {
            response.getWriter().println("Thiếu tham số type");
            return;
        }

        HttpSession session = request.getSession(false);
        if (idParam != null && idParam.matches("\\d+")) {
            int id = Integer.parseInt(idParam);
            GenKey key = GenKeyDAO.getKeyById(id);
            if (key == null || key.getPublicKey() == null || key.getPublicKey().isEmpty()) {
                response.getWriter().println("Không tìm thấy key");
                return;
            }
            if (!"public".equals(type)) {
                response.getWriter().println("Private key chỉ có thể tải ngay sau khi tạo");
                return;
            }
            writePem(response, "public", key.getPublicKey());
            return;
        }
        String keyContent = null;
        if (session != null) {
            if ("public".equals(type)) {
                keyContent = (String) session.getAttribute("newPublicKey");
            } else if ("private".equals(type)) {
                keyContent = (String) session.getAttribute("newPrivateKey");
            }
        }

        if (keyContent == null || keyContent.isEmpty()) {
            response.getWriter().println("Không tìm thấy key");
            return;
        }

        writePem(response, type, keyContent);
    }

    private void writePem(HttpServletResponse response, String type, String keyContent) throws IOException {
        String fileName = "public".equals(type) ? "public_key.pem" : "private_key.pem";
        response.setContentType("application/x-pem-file");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        PrintWriter out = response.getWriter();
        out.println(keyContent);
        out.flush();
    }
}