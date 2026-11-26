package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.EmailVerificationToken;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class VerificationDAO {

    public void saveToken(int userId, String token, Timestamp expiry) throws SQLException {
        String query = "INSERT INTO email_verification_tokens(user_id, token, expiry_time) VALUES(?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, expiry);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Failed to insert verification token for userId=" + userId);
            }
        }
    }

    public EmailVerificationToken findToken(String token) {
        String query = "SELECT * FROM email_verification_tokens WHERE token = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, token);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmailVerificationToken vt = new EmailVerificationToken();
                    vt.setUserId(rs.getInt("user_id"));
                    vt.setToken(rs.getString("token"));
                    vt.setExpiryTime(rs.getTimestamp("expiry_time"));
                    return vt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTokenByUserId(int userId) {
        String query = "DELETE FROM email_verification_tokens WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
