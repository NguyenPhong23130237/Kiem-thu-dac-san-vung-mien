package vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ShippingInfo;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Cart implements Serializable {
    Map<Integer, CartItem> data;
    private User user;
    private ShippingInfo shippingInfo;
    private String paymentMethod;
    private String note;

    public Cart() {
        this.data = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            quantity = 1;
        }
        if (get(product.getId()) != null)
            data.get(product.getId()).upQuantity(quantity);
        else {
            if (product.getDiscountPercentage() > 0) {
                data.put(product.getId(), new CartItem(product.getName(), quantity, product.getDiscountPrice(), product));
            } else {
                data.put(product.getId(), new CartItem(product.getName(), quantity, product.getPrice(), product));
            }
        }
    }

    public boolean updateItem(int productId, int quantity) {
        if (get(productId) == null) return false;
        if (quantity <= 0) quantity = 1;
        data.get(productId).setQuantity(quantity);
        return true;
    }

    public CartItem removeItem(int productId) {
        if (get(productId) == null) return null;
        return data.remove(productId);
    }

    public List<CartItem> removeAllItems() {
        ArrayList<CartItem> cartItems = new ArrayList<>(data.values());
        data.clear();
        return cartItems;
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(data.values());
    }

    private CartItem get(int id) {
        return data.get(id);
    }

    public int getQuantityInCart(int productId) {
        CartItem item = get(productId);
        return item == null ? 0 : item.getQuantity();
    }

    public int getTotalQuantity() {
        AtomicInteger total = new AtomicInteger();
        getItems().forEach(item -> {
            total.addAndGet(item.getQuantity());
        });
        return total.get();
    }

    public double getTotal() {
        AtomicReference<Double> total = new AtomicReference<>((double) 0);
        getItems().forEach(item -> {
            total.updateAndGet(v -> v + (item.getQuantity() * item.getPrice()));
        });
        return total.get();
    }

    public void updateCustomer(User user) {
        this.user = user;
    }

    public String format(double price) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        return formatter.format(price);
    }

    public ShippingInfo getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public User getUser() {
        return user;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            paymentMethod = "COD";
        }
        this.paymentMethod = paymentMethod;
    }

    public String getNote() {
        return note == null ? "" : note.trim();
    }

    public void setNote(String note) {
        this.note = note;
    }
}
