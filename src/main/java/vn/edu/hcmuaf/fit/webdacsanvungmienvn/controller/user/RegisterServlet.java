package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.VerificationDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.VerificationService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.EmailUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String fullname = request.getParameter("fullName");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("rePassword");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email không được để trống");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (fullname == null || fullname.trim().isEmpty()) {
            request.setAttribute("error", "Họ và tên không được để trống");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Mật khẩu không được để trống");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        boolean lengthValid = password.length() >= 8;
        boolean firstUppercase = Character.isUpperCase(password.charAt(0));
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+=\\-].*");

        if (!lengthValid || !firstUppercase || !hasSpecialChar) {
            request.setAttribute("error", "Mật khẩu phải ≥ 8 ký tự, chữ cái đầu viết hoa và có ít nhất 1 ký tự đặc biệt");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(rePassword)) {
            request.setAttribute("error", "Mật khẩu nhập lại không khớp");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (userDAO.existsByEmail(email)) {
            request.setAttribute("error", "Email đã tồn tại");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        int userId = userDAO.insertUser(email, fullname, password);

        request.getSession().setAttribute("userId", userId);
        request.getSession().setAttribute("email", email);

        VerificationService verificationService = new VerificationService();
        try {
            verificationService.sendVerificationEmail(userId, email);
            response.sendRedirect("verify-notice.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
