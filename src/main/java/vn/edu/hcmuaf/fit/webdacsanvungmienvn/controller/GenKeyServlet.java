package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.algorithm.DSA;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.GenKeyDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.GenKey;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "genKeyServlet", value = "/generateKey")
public class GenKeyServlet extends HttpServlet {
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
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp"); // Hoặc trang login của bạn
            return;
        }

        String clearNew = request.getParameter("clearNew");
        if ("1".equals(clearNew)) {
            session.removeAttribute("newPublicKey");
            session.removeAttribute("newPrivateKey");
            session.removeAttribute("newKeyId");
        }

        Integer userId = getLoggedInUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String flashMessage = (String) session.getAttribute("flash_message");
        if (flashMessage != null) {
            request.setAttribute("message", flashMessage);
            session.removeAttribute("flash_message");
        }
        String flashError = (String) session.getAttribute("flash_error");
        if (flashError != null) {
            request.setAttribute("error", flashError);
            session.removeAttribute("flash_error");
        }

        List<GenKey> keyList = GenKeyDAO.getKeysByUserId(userId);
        request.setAttribute("keyList", keyList);

        GenKey currentKey = GenKeyDAO.getActiveKeyByUserId(userId);
        if (currentKey != null) {
            request.setAttribute("currentKey", currentKey);
        }

        request.getRequestDispatcher("/gen-key.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer userId = getLoggedInUserId(request);
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            String algorithm = "DSA";

            GenKey activeKey = GenKeyDAO.getActiveKeyByUserId(userId);
            if (activeKey != null) {
                GenKeyDAO.revokeKey(activeKey.getId());
            }

            DSA dsa = new DSA();
            dsa.genKey();

            String publicKey = dsa.getPublicKeyBase64();
            String privateKey = dsa.getPrivateKeyBase64();

            GenKey key = new GenKey();
            key.setUserId(userId);
            key.setPublicKey(publicKey);
            key.setAlgorithm(algorithm);
            key.setStatus("ACTIVE");

            Integer createdKeyId = GenKeyDAO.insertKeyAndReturnId(key);
            boolean success = createdKeyId != null;

            HttpSession session = request.getSession();
            if (success) {
                session.setAttribute("flash_message", "Tạo key thành công");
                session.setAttribute("newPublicKey", publicKey);
                session.setAttribute("newPrivateKey", privateKey);
                session.setAttribute("newKeyId", createdKeyId);

            } else {
                session.setAttribute("flash_error", "Lưu database thất bại");
            }

            response.sendRedirect(request.getContextPath() + "/generateKey");

        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("flash_error", "Tạo key thất bại do lỗi hệ thống");
            response.sendRedirect(request.getContextPath() + "/generateKey");
        }
    }
}
