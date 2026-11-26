package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.PasswordResetToken;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PasswordResetDAO {

    public void saveToken(int userId, String token, Timestamp expiry) throws SQLException {
        String sql = "INSERT INTO password_reset_tokens(user_id, token, expiry_time) VALUES(?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, expiry);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Failed to insert password reset token for userId=" + userId);
            }
        }
    }

    public PasswordResetToken findToken(String token) {
        String sql = "SELECT * FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    PasswordResetToken prt = new PasswordResetToken();
                    prt.setUserId(rs.getInt("user_id"));
                    prt.setToken(rs.getString("token"));
                    prt.setExpiryTime(rs.getTimestamp("expiry_time"));
                    return prt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTokenByUserId(int userId) {
        String sql = "DELETE FROM password_reset_tokens WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
