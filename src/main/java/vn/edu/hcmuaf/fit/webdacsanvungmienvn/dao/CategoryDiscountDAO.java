package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.CategoryDiscount;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductDiscount;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDiscountDAO {
    public List<CategoryDiscount> getAllCategoryDiscounts() {
        List<CategoryDiscount> discounts = new ArrayList<>();
        String sql = "SELECT cd.id, cd.category_id, pc.name as categoryName, cd.discount_percentage, cd.start_date, cd.end_date FROM category_discounts cd JOIN product_categories pc ON cd.category_id = pc.id ORDER BY cd.id ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CategoryDiscount discount = new CategoryDiscount();
                discount.setId(rs.getInt("id"));
                discount.setCategoryId(rs.getInt("category_id"));
                discount.setCategoryName(rs.getString("categoryName"));
                discount.setDiscountPercentage(rs.getInt("discount_percentage"));
                discount.setStartDate(rs.getDate("start_date"));
                discount.setEndDate(rs.getDate("end_date"));
                discounts.add(discount);
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDiscountDAO] Lỗi truy vấn getAllCategoryDiscounts: " + e.getMessage());
            e.printStackTrace();
        }
        return discounts;
    }

    public boolean insertCategoryDiscount(CategoryDiscount discount) {
        String sql = "INSERT INTO category_discounts (category_id, discount_percentage, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, discount.getCategoryId());
            ps.setInt(2, discount.getDiscountPercentage());
            ps.setDate(3, new java.sql.Date(discount.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(discount.getEndDate().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoryDiscountDAO] Lỗi insert: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCategoryDiscount(CategoryDiscount discount) {
        String sql = "UPDATE category_discounts SET category_id=?, discount_percentage=?, start_date=?, end_date=? WHERE id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, discount.getCategoryId());
            ps.setInt(2, discount.getDiscountPercentage());
            ps.setDate(3, new java.sql.Date(discount.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(discount.getEndDate().getTime()));
            ps.setInt(5, discount.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoryDiscountDAO] Lỗi update: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCategoryDiscount(int id) {
        String sql = "DELETE FROM category_discounts WHERE id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public CategoryDiscount getCategoryDiscountById(int id) {
        String sql = "SELECT cd.id, cd.category_id, pc.name as categoryName, cd.discount_percentage, cd.start_date, cd.end_date FROM category_discounts cd JOIN product_categories pc ON cd.category_id = pc.id WHERE cd.id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CategoryDiscount discount = new CategoryDiscount();
                    discount.setId(rs.getInt("id"));
                    discount.setCategoryId(rs.getInt("category_id"));
                    discount.setCategoryName(rs.getString("categoryName"));
                    discount.setDiscountPercentage(rs.getInt("discount_percentage"));
                    discount.setStartDate(rs.getDate("start_date"));
                    discount.setEndDate(rs.getDate("end_date"));
                    return discount;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDiscountDAO] getCategoryDiscountById error: " + e.getMessage());
        }
        return null;
    }

    public int countAllDiscounts() {
        String sql = "SELECT COUNT(*) FROM category_discounts";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDiscountDAO] countAllDiscounts error: " + e.getMessage());
        }
        return 0;
    }

    public List<CategoryDiscount> getDiscountsPaged(int page, int pageSize) {
        List<CategoryDiscount> discounts = new ArrayList<>();
        String sql = "SELECT cd.id, cd.category_id, pc.name as categoryName, cd.discount_percentage, cd.start_date, cd.end_date FROM category_discounts cd JOIN product_categories pc ON cd.category_id = pc.id ORDER BY cd.id ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CategoryDiscount discount = new CategoryDiscount();
                    discount.setId(rs.getInt("id"));
                    discount.setCategoryId(rs.getInt("category_id"));
                    discount.setCategoryName(rs.getString("categoryName"));
                    discount.setDiscountPercentage(rs.getInt("discount_percentage"));
                    discount.setStartDate(rs.getDate("start_date"));
                    discount.setEndDate(rs.getDate("end_date"));
                    discounts.add(discount);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDiscountDAO] getDiscountsPaged error: " + e.getMessage());
        }
        return discounts;
    }

    public List<CategoryDiscount> searchCategoryDiscountsPaged(String keyword, int page, int pageSize) {
        List<CategoryDiscount> list = new ArrayList<>();
        String sql = "SELECT cd.id, cd.category_id, pc.name as categoryName, cd.discount_percentage, cd.start_date, cd.end_date " +
                "FROM category_discounts cd JOIN product_categories pc ON cd.category_id = pc.id " +
                "WHERE LOWER(pc.name) LIKE ? OR CAST(cd.discount_percentage AS CHAR) LIKE ? " +
                "ORDER BY cd.id ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setInt(3, pageSize);
            ps.setInt(4, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoryDiscount discount = new CategoryDiscount();
                discount.setId(rs.getInt("id"));
                discount.setCategoryId(rs.getInt("category_id"));
                discount.setCategoryName(rs.getString("categoryName"));
                discount.setDiscountPercentage(rs.getInt("discount_percentage"));
                discount.setStartDate(rs.getDate("start_date"));
                discount.setEndDate(rs.getDate("end_date"));
                list.add(discount);
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDiscountDAO] Lỗi truy vấn searchCategoryDiscountsPaged: " + e.getMessage());
        }
        return list;
    }

    public int countSearchCategoryDiscounts(String keyword) {
        String sql = "SELECT COUNT(*) FROM category_discounts cd JOIN product_categories pc ON cd.category_id = pc.id " +
                "WHERE LOWER(pc.name) LIKE ? OR CAST(cd.discount_percentage AS CHAR) LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[CategoryDiscountDAO] Lỗi truy vấn countSearchCategoryDiscounts: " + e.getMessage());
        }
        return 0;
    }
}
