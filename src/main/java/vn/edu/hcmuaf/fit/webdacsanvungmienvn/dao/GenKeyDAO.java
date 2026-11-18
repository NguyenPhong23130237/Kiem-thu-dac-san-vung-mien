package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.GenKey;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenKeyDAO {
    public static boolean insertKey(GenKey key) {
        try {
            Connection conn = DBConnect.getConnection();
            String sql = "INSERT INTO user_keys " +
                            "(user_id, public_key, algorithm, created_at, status) " +
                            "VALUES (?, ?, ?, NOW(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, key.getUserId());
            ps.setString(2, key.getPublicKey());
            ps.setString(3, key.getAlgorithm());
            ps.setString(4, key.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Integer insertKeyAndReturnId(GenKey key) {
        try {
            Connection conn = DBConnect.getConnection();
            String sql = "INSERT INTO user_keys (user_id, public_key, algorithm, created_at, status) VALUES (?, ?, ?, NOW(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, key.getUserId());
            ps.setString(2, key.getPublicKey());
            ps.setString(3, key.getAlgorithm());
            ps.setString(4, key.getStatus());
            int updated = ps.executeUpdate();
            if (updated <= 0) return null;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<GenKey> getAllInfoKeys() {
        List<GenKey> list = new ArrayList<>();
        try {
            Connection conn = DBConnect.getConnection();
            String sql = "SELECT * FROM user_keys ORDER BY id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GenKey key = new GenKey();
                key.setId(rs.getInt("id"));
                key.setUserId(rs.getInt("user_id"));
                key.setPublicKey(rs.getString("public_key"));
                key.setAlgorithm(rs.getString("algorithm"));
                key.setCreatedAt(rs.getTimestamp("created_at"));
                key.setRevokedAt(rs.getTimestamp("revoked_at"));
                key.setStatus(rs.getString("status"));
                list.add(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static GenKey getKeyById(int id) {
        try {
            Connection conn = DBConnect.getConnection();
            String sql = "SELECT * FROM user_keys WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GenKey key = new GenKey();
                key.setId(rs.getInt("id"));
                key.setUserId(rs.getInt("user_id"));
                key.setPublicKey(rs.getString("public_key"));
                key.setAlgorithm(rs.getString("algorithm"));
                key.setCreatedAt(rs.getTimestamp("created_at"));
                key.setRevokedAt(rs.getTimestamp("revoked_at"));
                key.setStatus(rs.getString("status"));
                return key;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean revokeKey(int keyId) {
        try {
            Connection conn = DBConnect.getConnection();
            String sql = "UPDATE user_keys SET status = 'REVOKED', revoked_at = NOW() WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, keyId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<GenKey> getKeysByUserId(int userId) {
        List<GenKey> list = new ArrayList<>();
        String sql = "SELECT * FROM user_keys WHERE user_id = ? ORDER BY id DESC";
        try {
            Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GenKey key = new GenKey();
                key.setId(rs.getInt("id"));
                key.setUserId(rs.getInt("user_id"));
                key.setPublicKey(rs.getString("public_key"));
                key.setAlgorithm(rs.getString("algorithm"));
                key.setCreatedAt(rs.getTimestamp("created_at"));
                key.setRevokedAt(rs.getTimestamp("revoked_at"));
                key.setStatus(rs.getString("status"));
                list.add(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static GenKey getActiveKeyByUserId(int userId) {
        String sql = "SELECT * FROM user_keys WHERE user_id = ? AND status = 'ACTIVE' ORDER BY id DESC LIMIT 1";
        try {
            Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GenKey key = new GenKey();
                key.setId(rs.getInt("id"));
                key.setUserId(rs.getInt("user_id"));
                key.setPublicKey(rs.getString("public_key"));
                key.setAlgorithm(rs.getString("algorithm"));
                key.setCreatedAt(rs.getTimestamp("created_at"));
                key.setRevokedAt(rs.getTimestamp("revoked_at"));
                key.setStatus(rs.getString("status"));
                return key;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isKeyRevoked(int userID){
        String sql = "SELECT status FROM user_keys WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        try(Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "REVOKED".equals(rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static List<GenKey> getKeysPaginated(String search, int page, int pageSize) {
        List<GenKey> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String sql = "SELECT * FROM user_keys WHERE status = 'ACTIVE' ";
        if (search != null && !search.isBlank()) {
            sql += "AND (CAST(user_id AS CHAR) LIKE ? OR algorithm LIKE ?) ";
        }
        sql += "ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (search != null && !search.isBlank()) {
                String searchPattern = "%" + search + "%";
                ps.setString(idx++, searchPattern);
                ps.setString(idx++, searchPattern);
            }
            ps.setInt(idx++, pageSize);
            ps.setInt(idx++, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GenKey key = new GenKey();
                key.setId(rs.getInt("id"));
                key.setUserId(rs.getInt("user_id"));
                key.setPublicKey(rs.getString("public_key"));
                key.setAlgorithm(rs.getString("algorithm"));
                key.setCreatedAt(rs.getTimestamp("created_at"));
                key.setRevokedAt(rs.getTimestamp("revoked_at"));
                key.setStatus(rs.getString("status"));
                list.add(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int getTotalKeysCount(String search) {
        String sql = "SELECT COUNT(*) FROM user_keys WHERE status = 'ACTIVE' ";
        if (search != null && !search.isBlank()) {
            sql += "AND (CAST(user_id AS CHAR) LIKE ? OR algorithm LIKE ?) ";
        }
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (search != null && !search.isBlank()) {
                String searchPattern = "%" + search + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
