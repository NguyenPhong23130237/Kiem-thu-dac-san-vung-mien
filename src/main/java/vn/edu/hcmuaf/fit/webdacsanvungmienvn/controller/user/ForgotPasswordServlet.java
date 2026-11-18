package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.PasswordResetService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String error = request.getParameter("error");
        if (error != null && !error.trim().isEmpty()) {
            request.setAttribute("error", error);
        }
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect("forgot-password?error=" + URLEncoder.encode("Email không được để trống", StandardCharsets.UTF_8));
            return;
        }
        User user = userDAO.findByEmail(email);
        int userId = user != null && user.getProvider().equals("LOCAL") ? user.getId() : -1;
        session.setAttribute("userId", userId);
        session.setAttribute("email", email);
        PasswordResetService passwordResetService = new PasswordResetService();
        try {
            passwordResetService.sendPasswordResetEmail(userId, email);
            response.sendRedirect("password-reset-notice.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
