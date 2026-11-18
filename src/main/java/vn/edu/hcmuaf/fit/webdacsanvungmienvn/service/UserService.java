package vn.edu.hcmuaf.fit.webdacsanvungmienvn.service;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.UserDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public int countAllUsers() {
        return userDAO.countAllUsers();
    }

    public List<User> getUsersPaged(int page, int pageSize) {
        return userDAO.getUsersPaged(page, pageSize);
    }

    public boolean createUser(User user) throws SQLException {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || user.getPassword() == null || user.getPassword().trim().isEmpty()) return false;
        return userDAO.createUser(user);
    }

    public boolean updateUser(User user) throws SQLException {
        if (user.getId() <= 0 || user.getEmail() == null || user.getEmail().trim().isEmpty()) return false;
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int id) {
        if (id <= 0) return false;
        return userDAO.deleteUser(id);
    }

    public boolean userEmailExists(String email) {
        if (email == null) return false;
        String normalized = email.trim();
        if (normalized.isEmpty()) return false;
        return userDAO.existsByEmail(normalized);
    }

    public boolean userEmailExistsExcludingId(String email, int excludeId) {
        if (email == null) return false;
        String normalized = email.trim();
        if (normalized.isEmpty()) return false;
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(normalized) && u.getId() != excludeId) return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<User> searchUsers(String keyword) throws SQLException {
        if (keyword == null) keyword = "";
        return userDAO.searchUsersbyAdmin(keyword.trim());
    }

    public int countSearchUsers(String keyword) throws SQLException {
        if (keyword == null) keyword = "";
        return searchUsers(keyword.trim()).size();
    }

    public User findById(int id) {
        if (id <= 0) return null;
        return userDAO.findById(id);
    }
}
