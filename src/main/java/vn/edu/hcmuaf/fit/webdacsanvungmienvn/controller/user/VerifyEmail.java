package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.VerificationDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.EmailVerificationToken;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;
import java.util.Date;

@WebServlet(name = "VerifyEmail", value = "/verify-email")
public class VerifyEmail extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        VerificationDAO dao = new VerificationDAO();
        EmailVerificationToken vt = dao.findToken(token);

        if (vt != null && vt.getExpiryTime().after(new Date())) {

            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(vt.getUserId());
            userDAO.setVerified(user.getId());

            dao.deleteTokenByUserId(vt.getUserId());

            request.setAttribute("email", user.getEmail());
            request.getRequestDispatcher("verify-success.jsp").forward(request, response);
        } else {
            response.getWriter().println("Link không hợp lệ hoặc đã hết hạn.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}