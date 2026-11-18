package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.MD5Util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        UserDAO dao = new UserDAO();

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");


        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu hiện tại");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }


        User dbUser = dao.login(sessionUser.getEmail(), oldPassword.trim());
        if (dbUser == null) {
            request.setAttribute("error", "Mật khẩu hiện tại không đúng");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("error", "Mật khẩu mới không được để trống");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }

        newPassword = newPassword.trim();
        boolean valid = newPassword.length() >= 8
                && Character.isUpperCase(newPassword.charAt(0))
                && newPassword.matches(".*[!@#$%^&*()_+=\\-].*");

        if (!valid) {
            request.setAttribute("error",
                    "Mật khẩu mới phải ≥ 8 ký tự, chữ cái đầu viết hoa và có ít nhất 1 ký tự đặc biệt");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới không khớp");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }


        boolean updated = dao.updatePassword(sessionUser.getId(), newPassword);

        if (updated) {
            sessionUser.setPassword(MD5Util.hash(newPassword));
            session.setAttribute("user", sessionUser);

            request.setAttribute("success", "Đổi mật khẩu thành công");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại");
        }

        request.getRequestDispatcher("change-password.jsp").forward(request, response);
    }
}
