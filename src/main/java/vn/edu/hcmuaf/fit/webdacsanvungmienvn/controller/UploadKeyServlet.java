package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.algorithm.DSA;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.GenKeyDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.GenKey;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@WebServlet(name = "UploadKeyServlet", value = "/UploadKeyServlet")
@MultipartConfig
public class UploadKeyServlet extends HttpServlet {

    private static Integer getLoggedInUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        Object u = session.getAttribute("user");
        if (u instanceof User) {
            return ((User) u).getId();
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/generateKey");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            Integer userId = getLoggedInUserId(request);
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            GenKey activeKey = GenKeyDAO.getActiveKeyByUserId(userId);
            if (activeKey != null) {
                session.setAttribute("flash_error", "Bạn đang có một khóa hoạt động! Vui lòng hủy khóa cũ trước khi tải lên khóa mới.");
                response.sendRedirect(request.getContextPath() + "/generateKey");
                return;
            }

            Part filePart = request.getPart("keyFile");
            if (filePart == null || filePart.getSize() == 0) {
                session.setAttribute("flash_error", "Vui lòng chọn file định dạng .pem hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/generateKey");
                return;
            }

            String fileContent;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream()))) {
                fileContent = reader.lines().collect(Collectors.joining("\n"));
            }

            String publicKeyBase64 = fileContent
                    .replaceAll("-----(BEGIN|END)[A-Z ]+-----", "")
                    .replaceAll("\\s+", "");

            if (publicKeyBase64.isEmpty()) {
                session.setAttribute("flash_error", "Nội dung file khóa trống hoặc sai cấu trúc văn bản!");
                response.sendRedirect(request.getContextPath() + "/generateKey");
                return;
            }

            DSA dsa = new DSA();
            if (!dsa.isValidPublicKey(publicKeyBase64)) {
                session.setAttribute("flash_error", "Nội dung không phải là một Public Key DSA hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/generateKey");
                return;
            }

            GenKey key = new GenKey();
            key.setUserId(userId);
            key.setPublicKey(publicKeyBase64);
            key.setAlgorithm("DSA");
            key.setStatus("ACTIVE");

            Integer createdKeyId = GenKeyDAO.insertKeyAndReturnId(key);
            if (createdKeyId != null) {
                session.setAttribute("flash_message", "Tải lên và kích hoạt khóa Public Key thành công!");
            } else {
                session.setAttribute("flash_error", "Lưu khóa vào cơ sở dữ liệu không thành công!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("flash_error", "Lỗi hệ thống khi xử lý file: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/generateKey");
    }
}