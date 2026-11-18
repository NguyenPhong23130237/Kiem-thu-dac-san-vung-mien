package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminUsers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "AddUserServlet", value = "/AddUserServlet")
public class AddUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        UserDAO userDAO = new UserDAO();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("full_name");
        String phoneNumber = request.getParameter("phone_number");
        String gender = request.getParameter("gender");
        String dobStr = request.getParameter("date_of_birth");
        String role = request.getParameter("role");

        request.setAttribute("addFullName", fullName);
        request.setAttribute("addEmail", email);
        request.setAttribute("addPhoneNumber", phoneNumber);
        request.setAttribute("addGender", gender);
        request.setAttribute("addDateOfBirth", dobStr);
        request.setAttribute("addRole", role);

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("addError", "Email và mật khẩu là bắt buộc.");
            request.setAttribute("openAddModal", true);
            // Lấy lại đúng danh sách user, phân trang, search
            int page = 1;
            int pageSize = 50;
            String search = request.getParameter("search");
            try {
                String pageStr = request.getParameter("page");
                if (pageStr != null) page = Integer.parseInt(pageStr);
            } catch (Exception ignored) {}
            if (page < 1) page = 1;
            List<User> users;
            int totalUsers;
            if (search != null && !search.isEmpty()) {
                users = userDAO.searchUsers(search);
                totalUsers = users.size();
            } else {
                users = userDAO.getUserPaged(page, pageSize);
                totalUsers = userDAO.countAllUsers();
            }
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            if (totalPages < 1) totalPages = 1;
            request.setAttribute("users", users);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("search", search);
            request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
            return;
        }

        if (userDAO.existsByEmail(email.trim())) {
            request.setAttribute("addError", "Email đã tồn tại.");
            request.setAttribute("openAddModal", true);
            int page = 1;
            int pageSize = 50;
            String search = request.getParameter("search");
            try {
                String pageStr = request.getParameter("page");
                if (pageStr != null) page = Integer.parseInt(pageStr);
            } catch (Exception ignored) {}
            if (page < 1) page = 1;
            List<User> users;
            int totalUsers;
            if (search != null && !search.isEmpty()) {
                users = userDAO.searchUsers(search);
                totalUsers = users.size();
            } else {
                users = userDAO.getUserPaged(page, pageSize);
                totalUsers = userDAO.countAllUsers();
            }
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            if (totalPages < 1) totalPages = 1;
            request.setAttribute("users", users);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("search", search);
            request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
            return;
        }

        Date sqlDob = null;
        try {
            if (dobStr != null && !dobStr.isEmpty()) sqlDob = Date.valueOf(dobStr);
        } catch (Exception ex) {
        }

        boolean ok = userDAO.insertUserbyAdmin(email.trim(), password.trim(), fullName, phoneNumber, gender, sqlDob, role);
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } else {
            int page = 1;
            int pageSize = 50;
            String search = request.getParameter("search");
            try {
                String pageStr = request.getParameter("page");
                if (pageStr != null) page = Integer.parseInt(pageStr);
            } catch (Exception ignored) {}
            if (page < 1) page = 1;

            List<User> users;
            int totalUsers;
            if (search != null && !search.isEmpty()) {
                users = userDAO.searchUsers(search);
                totalUsers = users.size();
            } else {
                users = userDAO.getUserPaged(page, pageSize);
                totalUsers = userDAO.countAllUsers();
            }
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            if (totalPages < 1) totalPages = 1;
            request.setAttribute("users", users);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("search", search);

            request.setAttribute("addError", "Không thể thêm người dùng (lỗi hệ thống).");
            request.setAttribute("openAddModal", true);
            request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
        }
    }
}
