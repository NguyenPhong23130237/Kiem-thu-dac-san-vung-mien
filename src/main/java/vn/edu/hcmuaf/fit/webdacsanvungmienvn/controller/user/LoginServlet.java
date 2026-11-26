package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.VerificationDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.VerificationService;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.EmailUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email != null) email = email.trim();
        if (password != null) password = password.trim();

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        UserDAO dao = new UserDAO();
        User user = dao.login(email, password);


        if (user != null) {
            if ("LOCAL".equals(user.getProvider()) && !user.isVerified()) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("email", user.getEmail());

                VerificationService verificationService = new VerificationService();
                try {
                    String baseUrl = VerificationService.buildBaseUrl(request);
                    verificationService.sendVerificationEmail(user.getId(), user.getEmail(), baseUrl);
                    response.sendRedirect("verify-notice.jsp");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Tài khoản chưa được xác minh và không thể gửi email xác minh. Vui lòng thử lại sau.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
            }
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            if (user.getRole().equals("admin")) {
                response.sendRedirect(request.getContextPath() + "/admin");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
