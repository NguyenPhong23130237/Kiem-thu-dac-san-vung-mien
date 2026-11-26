package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.PasswordResetDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.PasswordResetToken;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@WebServlet(name = "PasswordReset", value = "/password-reset")
public class PasswordReset extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        if (token == null || token.trim().isEmpty()) {
            response.getWriter().println("Link không hợp lệ hoặc đã hết hạn.");
            return;
        }

        PasswordResetDAO dao = new PasswordResetDAO();
        PasswordResetToken vt = dao.findToken(token.trim());
        if (vt == null || vt.getExpiryTime() == null || !vt.getExpiryTime().after(new Date())) {
            response.getWriter().println("Link không hợp lệ hoặc đã hết hạn.");
            return;
        }

        String error = request.getParameter("error");
        if (error != null) {
            request.setAttribute("error", error);
        }
        request.getRequestDispatcher("password-reset.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        if (token == null || token.trim().isEmpty()) {
            response.getWriter().println("Link không hợp lệ hoặc đã hết hạn.");
            return;
        }
        token = token.trim();

        PasswordResetDAO dao = new PasswordResetDAO();
        PasswordResetToken vt = dao.findToken(token);

        if (vt != null && vt.getExpiryTime() != null && vt.getExpiryTime().after(new Date())) {
            String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            if (password == null || password.trim().isEmpty()) {
                response.sendRedirect("password-reset?token=" + encodedToken + "&error=" + URLEncoder.encode("Mật khẩu không được để trống", StandardCharsets.UTF_8));
                return;
            }
            boolean lengthValid = password.length() >= 8;
            boolean firstUppercase = Character.isUpperCase(password.charAt(0));
            boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+=\\-].*");

            if (!lengthValid || !firstUppercase || !hasSpecialChar) {
                response.sendRedirect("password-reset?token=" + encodedToken + "&error=" + URLEncoder.encode("Mật khẩu phải ≥ 8 ký tự, chữ cái đầu viết hoa và có ít nhất 1 ký tự đặc biệt", StandardCharsets.UTF_8));
                return;
            }

            if (!password.equals(confirmPassword)) {
                response.sendRedirect("password-reset?token=" + encodedToken + "&error=" + URLEncoder.encode("Mật khẩu nhập lại không khớp", StandardCharsets.UTF_8));
                return;
            }

            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(vt.getUserId());
            if (user == null) {
                dao.deleteTokenByUserId(vt.getUserId());
                response.getWriter().println("Link không hợp lệ hoặc đã hết hạn.");
                return;
            }

            if (!userDAO.updatePassword(user.getId(), password)) {
                response.sendRedirect("password-reset?token=" + encodedToken + "&error=" + URLEncoder.encode("Không thể cập nhật mật khẩu. Vui lòng thử lại.", StandardCharsets.UTF_8));
                return;
            }

            dao.deleteTokenByUserId(user.getId());

            request.setAttribute("email", user.getEmail());
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.setAttribute("passwordResetDone", true);
            }
            request.getRequestDispatcher("password-reset-success.jsp").forward(request, response);
        } else {
            response.getWriter().println("Link không hợp lệ hoặc đã hết hạn.");
        }
    }
}