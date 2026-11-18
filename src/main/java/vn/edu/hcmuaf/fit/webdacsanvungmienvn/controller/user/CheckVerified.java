package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;

@WebServlet(name = "CheckVerified", value = "/check-verified")
public class CheckVerified extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        boolean verified = false;

        if (session != null && session.getAttribute("userId") != null && session.getAttribute("email") != null) {
            int userId = (int) session.getAttribute("userId");

            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);
            verified = user.isVerified();

            // Nếu đã verified → xóa khỏi session
            if (verified) {
                session.removeAttribute("userId");
                session.removeAttribute("email");
            }
        }

        response.getWriter().write("{\"verified\": " + verified + "}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}