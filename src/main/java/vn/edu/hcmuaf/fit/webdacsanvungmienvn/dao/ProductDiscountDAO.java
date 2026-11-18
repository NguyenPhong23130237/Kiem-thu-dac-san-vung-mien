package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductDiscount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDiscountDAO {

    public List<ProductDiscount> getAllDiscounts() {
        List<ProductDiscount> discounts = new ArrayList<>();
        String sql = "SELECT pd.*, p.name as product_name FROM product_discounts pd " +
                "LEFT JOIN products p ON pd.product_id = p.id ORDER BY pd.id ASC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductDiscount discount = new ProductDiscount();
                discount.setId(rs.getInt("id"));
                discount.setProductId(rs.getInt("product_id"));
                discount.setDiscountPercentage(rs.getInt("discount_percentage"));
                discount.setStartDate(rs.getDate("start_date"));
                discount.setEndDate(rs.getDate("end_date"));
                String productName = rs.getString("product_name");
                if (productName == null) productName = "Không xác định";
                discount.setProductName(productName);
                discounts.add(discount);
            }
        } catch (SQLException e) {
            System.err.println("[ProductDiscountDAO] Lỗi truy vấn getAllDiscounts: " + e.getMessage());
            e.printStackTrace();
        }
        return discounts;
    }

    public ProductDiscount getDiscountById(int id) {
        String sql = "SELECT pd.*, p.name as product_name FROM product_discounts pd " +
                "JOIN products p ON pd.product_id = p.id WHERE pd.id = ? ";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProductDiscount discount = new ProductDiscount();
                discount.setId(rs.getInt("id"));
                discount.setProductId(rs.getInt("product_id"));
                discount.setDiscountPercentage(rs.getInt("discount_percentage"));
                discount.setStartDate(rs.getDate("start_date"));
                discount.setEndDate(rs.getDate("end_date"));
                discount.setProductName(rs.getString("product_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addDiscount(ProductDiscount discount) {
        String sql = "INSERT INTO product_discounts (product_id, discount_percentage, start_date, end_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, discount.getProductId());
            ps.setInt(2, discount.getDiscountPercentage());
            ps.setDate(3, discount.getStartDate());
            ps.setDate(4, discount.getEndDate());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDiscount(ProductDiscount discount) {
        String sql = "UPDATE product_discounts SET product_id = ?, discount_percentage = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, discount.getProductId());
            ps.setInt(2, discount.getDiscountPercentage());
            ps.setDate(3, discount.getStartDate());
            ps.setDate(4, discount.getEndDate());
            ps.setInt(5, discount.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDiscount(int id) {
        String sql = "DELETE FROM product_discounts WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ProductDiscount> getDiscountsPaged(int page, int pageSize) {
        List<ProductDiscount> list = new ArrayList<>();
        String sql = "SELECT pd.*, p.name as product_name FROM product_discounts pd LEFT JOIN products p ON pd.product_id = p.id ORDER BY pd.id ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductDiscount pd = new ProductDiscount();
                pd.setId(rs.getInt("id"));
                pd.setProductId(rs.getInt("product_id"));
                pd.setDiscountPercentage(rs.getInt("discount_percentage"));
                pd.setStartDate(rs.getDate("start_date"));
                pd.setEndDate(rs.getDate("end_date"));
                String productName = rs.getString("product_name");
                pd.setProductName(productName != null ? productName : "Không xác định");
                list.add(pd);
            }
        } catch (SQLException e) {
            System.err.println("[ProductDiscountDAO] Lỗi truy vấn getDiscountsPaged: " + e.getMessage());
        }
        return list;
    }

    public int countAllDiscounts() {
        String sql = "SELECT COUNT(*) FROM product_discounts";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.err.println("[ProductDiscountDAO] Lỗi truy vấn countAllDiscounts: " + e.getMessage());
        }
        return 0;
    }

    public List<ProductDiscount> searchDiscountsPaged(String keyword, int page, int pageSize) {
        List<ProductDiscount> list = new ArrayList<>();
        String sql = "SELECT pd.*, p.name as product_name FROM product_discounts pd LEFT JOIN products p ON pd.product_id = p.id WHERE LOWER(p.name) LIKE ? ORDER BY pd.id ASC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductDiscount pd = new ProductDiscount();
                pd.setId(rs.getInt("id"));
                pd.setProductId(rs.getInt("product_id"));
                pd.setDiscountPercentage(rs.getInt("discount_percentage"));
                pd.setStartDate(rs.getDate("start_date"));
                pd.setEndDate(rs.getDate("end_date"));
                String productName = rs.getString("product_name");
                pd.setProductName(productName != null ? productName : "Không xác định");
                list.add(pd);
            }
        } catch (SQLException e) {
            System.err.println("[ProductDiscountDAO] Lỗi truy vấn searchDiscountsPaged: " + e.getMessage());
        }
        return list;
    }

    public int countSearchDiscounts(String keyword) {
        String sql = "SELECT COUNT(*) FROM product_discounts pd LEFT JOIN products p ON pd.product_id = p.id WHERE LOWER(p.name) LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[ProductDiscountDAO] Lỗi truy vấn countSearchDiscounts: " + e.getMessage());
        }
        return 0;
    }
}
