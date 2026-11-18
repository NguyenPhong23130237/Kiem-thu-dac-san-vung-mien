package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.GenKeyDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.GenKey;

import java.io.IOException;

@WebServlet(name = "reportLostKeyServlet", value = "/reportLostKey")
public class ReportLostKeyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("user") instanceof User)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");

        String keyIdParam = request.getParameter("keyId");
        if (keyIdParam == null || !keyIdParam.matches("\\d+")) {
            session.setAttribute("error", "Key ID không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/generateKey");
            return;
        }

        int keyId = Integer.parseInt(keyIdParam);
        GenKey key = GenKeyDAO.getKeyById(keyId);
        if (key == null) {
            session.setAttribute("error", "Không tìm thấy key");
            response.sendRedirect(request.getContextPath() + "/generateKey");
            return;
        }

        if (key.getUserId() != user.getId()) {
            session.setAttribute("error", "Bạn không có quyền báo mất key này");
            response.sendRedirect(request.getContextPath() + "/generateKey");
            return;
        }

        boolean ok = GenKeyDAO.revokeKey(keyId);
        if (ok) {
            session.setAttribute("message", "Đã báo mất key và vô hiệu hóa thành công");
        } else {
            session.setAttribute("error", "Báo mất key thất bại");
        }

        response.sendRedirect(request.getContextPath() + "/generateKey");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}

