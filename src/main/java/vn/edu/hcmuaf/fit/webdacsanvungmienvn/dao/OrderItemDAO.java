package vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart.CartItem;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.OrderItem;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {
    public void createOrderItem(int orderId, CartItem cartItem) {
        String query = "INSERT INTO order_items (order_id, product_id, product_name, product_quantity, product_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, cartItem.getProduct().getId());
            ps.setString(3, cartItem.getName());
            ps.setInt(4, cartItem.getQuantity());
            ps.setDouble(5, cartItem.getPrice());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT oi.*, p.image AS product_image FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(rs.getInt("id"));
                orderItem.setOrderId(rs.getInt("order_id"));
                orderItem.setProductId(rs.getInt("product_id"));
                orderItem.setProductName(rs.getString("product_name"));
                orderItem.setProductQuantity(rs.getInt("product_quantity"));
                orderItem.setProductPrice(rs.getDouble("product_price"));
                orderItem.setProductImage(rs.getString("product_image"));
                orderItems.add(orderItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderItems;
    }
}
