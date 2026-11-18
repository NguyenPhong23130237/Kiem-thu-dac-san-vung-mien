package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.PasswordResetService;

import java.io.IOException;

@WebServlet(name = "ResendPasswordReset", value = "/resend-password-reset")
public class ResendPasswordReset extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (session == null || session.getAttribute("userId") == null ||
                session.getAttribute("email") == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Phiên làm việc hết hạn\"}");
            return;
        }
        int userId = (int) session.getAttribute("userId");
        String email = (String) session.getAttribute("email");

        PasswordResetService passwordResetService = new PasswordResetService();
        try {
            passwordResetService.sendPasswordResetEmail(userId, email);
            response.getWriter().write("{\"success\": true, \"message\": \"Đã gửi lại email\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi gửi email\"}");
        }
    }
}