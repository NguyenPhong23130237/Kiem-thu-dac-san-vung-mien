package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.VerificationService;

import java.io.IOException;

@WebServlet(name = "ResendVerification", value = "/resend-verification")
public class ResendVerification extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (session == null || session.getAttribute("userId") == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Phiên làm việc hết hạn\"}");
            return;
        }
        UserDAO userDAO = new UserDAO();
        int userId = (int) session.getAttribute("userId");
        String email = (String) session.getAttribute("email");
        User user = userDAO.getUserById(userId);

        if (user == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy người dùng\"}");
            return;
        }

        if (user.isVerified()) {
            response.getWriter().write("{\"success\": false, \"message\": \"Tài khoản đã được xác minh\"}");
            return;
        }

        VerificationService verificationService = new VerificationService();
        try {
            verificationService.sendVerificationEmail(userId, email);
            response.getWriter().write("{\"success\": true, \"message\": \"Đã gửi lại email xác minh\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi gửi email\"}");
        }
    }
}