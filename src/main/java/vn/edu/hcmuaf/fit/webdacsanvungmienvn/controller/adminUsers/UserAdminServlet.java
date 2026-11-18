package vn.edu.hcmuaf.fit.webdacsanvungmienvn.controller.adminUsers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "UserAdminServlet", value = "/admin/users")
public class UserAdminServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = userDAO.deleteUser(id);
                System.out.println("[UserAdminServlet] delete id=" + id + " result=" + ok);
            } catch (Exception e) {
                System.err.println("[UserAdminServlet] delete failed: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        if ("edit".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                User edit = userDAO.findById(id);
                request.setAttribute("editUser", edit);
                request.setAttribute("openEditModal", true);
            } catch (Exception e) {
                System.err.println("[UserAdminServlet] edit param error: " + e.getMessage());
            }
        }

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        if (page < 1) page = 1;

        String search = request.getParameter("search");
        List<User> users;
        int totalUsers;
        if (search != null && !search.isEmpty()) {
            List<User> allSearchResults = userDAO.searchUsers(search);
            totalUsers = allSearchResults.size();
            int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);
            if (totalPages < 1) totalPages = 1;
            int fromIndex = (page - 1) * PAGE_SIZE;
            int toIndex = Math.min(fromIndex + PAGE_SIZE, totalUsers);
            if (fromIndex >= totalUsers) {
                users = List.of();
            } else {
                users = allSearchResults.subList(fromIndex, toIndex);
            }
            request.setAttribute("users", users);
            request.setAttribute("currentPage", page);
            request.setAttribute("search", search);
            request.setAttribute("totalPages", totalPages);
        } else {
            users = userDAO.getUserPaged(page, PAGE_SIZE);
            totalUsers = userDAO.countAllUsers();
            int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);
            if (totalPages < 1) totalPages = 1;
            request.setAttribute("users", users);
            request.setAttribute("currentPage", page);
            request.setAttribute("search", search);
            request.setAttribute("totalPages", totalPages);
        }
        request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phoneNumber = request.getParameter("phone_number");
        String gender = request.getParameter("gender");
        String dobStr = request.getParameter("date_of_birth");
        String role = request.getParameter("role");

        Date dob = null;
        try {
            if (dobStr != null && !dobStr.isEmpty()) dob = Date.valueOf(dobStr);
        } catch (Exception ignored) {}

        request.setAttribute("addFullName", fullName);
        request.setAttribute("addEmail", email);
        request.setAttribute("addPhoneNumber", phoneNumber);
        request.setAttribute("addGender", gender);
        request.setAttribute("addDateOfBirth", dobStr);
        request.setAttribute("addRole", role);

        if (action == null || !"update".equals(action)) {
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                request.setAttribute("addError", "Email và mật khẩu là bắt buộc.");
                request.setAttribute("openAddModal", true);
                List<User> users = userDAO.getUserPaged(1, PAGE_SIZE);
                request.setAttribute("users", users);
                request.setAttribute("currentPage", 1);
                int totalUsers = userDAO.countAllUsers();
                int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);
                if (totalPages < 1) totalPages = 1;
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
                return;
            }

            if (userDAO.existsByEmail(email.trim())) {
                request.setAttribute("addError", "Email đã tồn tại.");
                request.setAttribute("openAddModal", true);
                List<User> users = userDAO.getUserPaged(1, PAGE_SIZE);
                request.setAttribute("users", users);
                request.setAttribute("currentPage", 1);
                int totalUsers = userDAO.countAllUsers();
                int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);
                if (totalPages < 1) totalPages = 1;
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
                return;
            }

            boolean ok = userDAO.insertUserbyAdmin(email.trim(), password.trim(), fullName, phoneNumber, gender, dob, role);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else {
                request.setAttribute("addError", "Không thể thêm người dùng (lỗi hệ thống).");
                request.setAttribute("openAddModal", true);
                List<User> users = userDAO.getUserPaged(1, PAGE_SIZE);
                request.setAttribute("users", users);
                request.setAttribute("currentPage", 1);
                int totalUsers = userDAO.countAllUsers();
                int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);
                if (totalPages < 1) totalPages = 1;
                request.setAttribute("totalPages", totalPages);
                request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
            }
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            User existing = userDAO.findById(id);
            if (existing == null) {
                request.setAttribute("editError", "Người dùng không tồn tại.");
                request.setAttribute("openEditModal", true);
                List<User> users = userDAO.getUserPaged(1, PAGE_SIZE);
                request.setAttribute("users", users);
                request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
                return;
            }
            if (email != null && !email.trim().equalsIgnoreCase(existing.getEmail()) && userDAO.existsByEmail(email.trim())) {
                request.setAttribute("editError", "Email đã tồn tại.");
                request.setAttribute("openEditModal", true);
                request.setAttribute("editUser", existing);
                List<User> users = userDAO.getUserPaged(1, PAGE_SIZE);
                request.setAttribute("users", users);
                request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
                return;
            }

            boolean ok = userDAO.updateUser(id, email, fullName, phoneNumber, gender, dob, role);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else {
                request.setAttribute("editError", "Không thể cập nhật người dùng (lỗi hệ thống).");
                request.setAttribute("openEditModal", true);
                request.setAttribute("editUser", existing);
                List<User> users = userDAO.getUserPaged(1, PAGE_SIZE);
                request.setAttribute("users", users);
                request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("[UserAdminServlet] failed to handle update: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}

