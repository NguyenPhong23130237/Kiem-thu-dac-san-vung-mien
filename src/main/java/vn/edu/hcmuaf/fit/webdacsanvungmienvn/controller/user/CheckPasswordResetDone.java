package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.user;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CheckPasswordResetDone", value = "/check-password-reset-done")
public class CheckPasswordResetDone extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        boolean passwordResetDone = false;

        if (session != null && session.getAttribute("userId") != null &&
                session.getAttribute("email") != null) {
            passwordResetDone = (Boolean) session.getAttribute("passwordResetDone");

            if (passwordResetDone) {
                session.removeAttribute("userId");
                session.removeAttribute("email");
                session.removeAttribute("passwordResetDone");
            }
        }
        response.getWriter().write("{\"passwordResetDone\": " + passwordResetDone + "}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}