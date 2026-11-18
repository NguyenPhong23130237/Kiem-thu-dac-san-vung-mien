package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminUsers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "UpdateUserServlet", value = "/UpdateUserServlet")
public class UpdateUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                UserDAO userDAO = new UserDAO();
                User user = userDAO.findById(id);
                if (user != null) {
                    request.setAttribute("editUser", user);
                    request.setAttribute("openEditModal", true);
                    request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
                    return;
                }
            } catch (Exception e) {
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String email = request.getParameter("email");
        String fullName = request.getParameter("full_name");
        String phoneNumber = request.getParameter("phone_number");
        String gender = request.getParameter("gender");
        String dobStr = request.getParameter("date_of_birth");
        String role = request.getParameter("role");
        UserDAO userDAO = new UserDAO();
        try {
            int id = Integer.parseInt(idStr);
            Date dob = null;
            if (dobStr != null && !dobStr.isEmpty()) dob = Date.valueOf(dobStr);
            boolean ok = userDAO.updateUser(id, email, fullName, phoneNumber, gender, dob, role);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            } else {
                User user = userDAO.findById(id);
                request.setAttribute("editError", "Không thể cập nhật người dùng (lỗi hệ thống).");
                request.setAttribute("openEditModal", true);
                request.setAttribute("editUser", user);
                request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}