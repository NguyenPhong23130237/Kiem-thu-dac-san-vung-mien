package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShippingInfoDAO {
    public void addShippingInfo(ShippingInfo shippingInfo) {
        String query = "INSERT INTO shipping_infos (user_id, receiver_name, receiver_phone_number, province, district, ward, detailed_address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, shippingInfo.getUserId());
            ps.setString(2, shippingInfo.getReceiverName());
            ps.setString(3, shippingInfo.getReceiverPhoneNumber());
            ps.setString(4, shippingInfo.getProvince());
            ps.setString(5, shippingInfo.getDistrict());
            ps.setString(6, shippingInfo.getWard());
            ps.setString(7, shippingInfo.getDetailedAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteShippingInfo(int id) {
        String query = "DELETE FROM shipping_infos WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ShippingInfo> getShippingInfosByUserId(int userId) {
        List<ShippingInfo> shippingInfos = new ArrayList<>();
        String query = "SELECT * FROM shipping_infos WHERE user_id = ? ORDER BY id ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ShippingInfo shippingInfo = new ShippingInfo();
                shippingInfo.setId(rs.getInt("id"));
                shippingInfo.setUserId(rs.getInt("user_id"));
                shippingInfo.setReceiverName(rs.getString("receiver_name"));
                shippingInfo.setReceiverPhoneNumber(rs.getString("receiver_phone_number"));
                shippingInfo.setProvince(rs.getString("province"));
                shippingInfo.setDistrict(rs.getString("district"));
                shippingInfo.setWard(rs.getString("ward"));
                shippingInfo.setDetailedAddress(rs.getString("detailed_address"));
                shippingInfos.add(shippingInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippingInfos;
    }

    public ShippingInfo getShippingInfoById(int id) {
        ShippingInfo shippingInfo = null;
        String query = "SELECT * FROM shipping_infos WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                shippingInfo = new ShippingInfo();
                shippingInfo.setId(rs.getInt("id"));
                shippingInfo.setUserId(rs.getInt("user_id"));
                shippingInfo.setReceiverName(rs.getString("receiver_name"));
                shippingInfo.setReceiverPhoneNumber(rs.getString("receiver_phone_number"));
                shippingInfo.setProvince(rs.getString("province"));
                shippingInfo.setDistrict(rs.getString("district"));
                shippingInfo.setWard(rs.getString("ward"));
                shippingInfo.setDetailedAddress(rs.getString("detailed_address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippingInfo;
    }

    public void updateShippingInfo(ShippingInfo shippingInfo) {
        String query = "UPDATE shipping_infos SET receiver_name = ?, receiver_phone_number = ?, province = ?, district = ?, ward = ?, detailed_address = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, shippingInfo.getReceiverName());
            ps.setString(2, shippingInfo.getReceiverPhoneNumber());
            ps.setString(3, shippingInfo.getProvince());
            ps.setString(4, shippingInfo.getDistrict());
            ps.setString(5, shippingInfo.getWard());
            ps.setString(6, shippingInfo.getDetailedAddress());
            ps.setInt(7, shippingInfo.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getShippingInfoCount() {
        String sql = "SELECT COUNT(*) FROM shipping_infos";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ShippingInfo> getShippingInfosPaged(int page, int pageSize) {
        List<ShippingInfo> shippingInfos = new ArrayList<>();
        String sql = "SELECT * FROM shipping_infos ORDER BY id ASC LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;
        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    ShippingInfo shippingInfo = new ShippingInfo();
                    shippingInfo.setId(rs.getInt("id"));
                    shippingInfo.setUserId(rs.getInt("user_id"));
                    shippingInfo.setReceiverName(rs.getString("receiver_name"));
                    shippingInfo.setReceiverPhoneNumber(rs.getString("receiver_phone_number"));
                    shippingInfo.setProvince(rs.getString("province"));
                    shippingInfo.setDistrict(rs.getString("district"));
                    shippingInfo.setWard(rs.getString("ward"));
                    shippingInfo.setDetailedAddress(rs.getString("detailed_address"));
                    shippingInfos.add(shippingInfo);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return shippingInfos;
    }

    public List<ShippingInfo> searchShippingByAdminPaged(String keyword, int page, int pageSize) {
        List<ShippingInfo> shippingInfos = new ArrayList<>();
        String sql = "SELECT * FROM shipping_infos WHERE LOWER(receiver_name) LIKE ? OR LOWER(receiver_phone_number) LIKE ? OR LOWER(province) LIKE ? OR LOWER(district) LIKE ? OR LOWER(ward) LIKE ? OR LOWER(detailed_address) LIKE ? ORDER BY id ASC LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;
        String likeKeyword = "%" + (keyword == null ? "" : keyword.toLowerCase()) + "%";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 6; i++) ps.setString(i, likeKeyword);
            ps.setInt(7, pageSize);
            ps.setInt(8, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShippingInfo shippingInfo = new ShippingInfo();
                    shippingInfo.setId(rs.getInt("id"));
                    shippingInfo.setUserId(rs.getInt("user_id"));
                    shippingInfo.setReceiverName(rs.getString("receiver_name"));
                    shippingInfo.setReceiverPhoneNumber(rs.getString("receiver_phone_number"));
                    shippingInfo.setProvince(rs.getString("province"));
                    shippingInfo.setDistrict(rs.getString("district"));
                    shippingInfo.setWard(rs.getString("ward"));
                    shippingInfo.setDetailedAddress(rs.getString("detailed_address"));
                    shippingInfos.add(shippingInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippingInfos;
    }

    public int getSearchCount(String keyword) {
        String sql = "SELECT COUNT(*) FROM shipping_infos WHERE LOWER(receiver_name) LIKE ? OR LOWER(receiver_phone_number) LIKE ? OR LOWER(province) LIKE ? OR LOWER(district) LIKE ? OR LOWER(ward) LIKE ? OR LOWER(detailed_address) LIKE ?";
        String likeKeyword = "%" + (keyword == null ? "" : keyword.toLowerCase()) + "%";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 6; i++) ps.setString(i, likeKeyword);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
 }
