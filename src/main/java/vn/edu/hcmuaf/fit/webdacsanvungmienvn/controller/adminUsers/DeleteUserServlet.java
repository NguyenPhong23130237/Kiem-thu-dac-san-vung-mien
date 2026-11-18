package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminUsers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.service.UserService;

import java.io.IOException;

@WebServlet(name = "DeleteUserServlet", value = "/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=invalid_id");
            return;
        }

        boolean ok = userService.deleteUser(id);
        if (!ok) {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=delete_failed");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/users?success=deleted");
    }
}

