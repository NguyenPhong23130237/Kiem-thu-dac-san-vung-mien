package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Brand;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductCategory;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {

    public List<Brand> getTopBrands(int limit) {
        List<Brand> topBrands = new ArrayList<>();
        String sql = "SELECT * FROM brands ORDER BY id ASC LIMIT ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Brand b = new Brand();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                topBrands.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topBrands;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM brands";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Brand> getBrandsPaged(int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safePageSize;

        List<Brand> brands = new ArrayList<>();
        String sql = "SELECT id, name, info FROM brands ORDER BY id ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, safePageSize);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Brand b = new Brand();
                    b.setId(rs.getInt("id"));
                    b.setName(rs.getString("name"));
                    b.setInfo(rs.getString("info"));
                    brands.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brands;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM brands WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsByNameExcludingId(String name, int excludeId) {
        String sql = "SELECT 1 FROM brands WHERE LOWER(name) = LOWER(?) AND id <> ? LIMIT 1";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int create(String name, String info) {
        String sql = "INSERT INTO brands(name, info) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, info);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean update(int id, String name, String info) {
        String sql = "UPDATE brands SET name=?, info=? WHERE id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, info);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM brands WHERE id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Brand> getAllBrands() {
        List<Brand> brands = new ArrayList<>();
        String query = "SELECT * FROM brands ORDER BY id ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Brand brand = new Brand();
                brand.setId(rs.getInt("id"));
                brand.setName(rs.getString("name"));
                brands.add(brand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brands;
    }

    public List<Brand> searchBrands(String keyword, int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safePageSize;
        List<Brand> brands = new ArrayList<>();
        String sql = "SELECT id, name, info FROM brands WHERE LOWER(name) LIKE ? ORDER BY id ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setInt(2, safePageSize);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Brand b = new Brand();
                    b.setId(rs.getInt("id"));
                    b.setName(rs.getString("name"));
                    b.setInfo(rs.getString("info"));
                    brands.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brands;
    }

    public int countSearchBrands(String keyword) {
        String sql = "SELECT COUNT(*) FROM brands WHERE LOWER(name) LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
