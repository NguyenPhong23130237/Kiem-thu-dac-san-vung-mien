package vn.edu.hcmuaf.fit.webdacsanvungmienvn.cart;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;

public class CartItem {
    private String name;
    private int quantity;
    private double price;
    private Product product;

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    public CartItem(String name, int quantity, double price, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }

    public CartItem() {
    }

    public void upQuantity(int quantity) {
        this.quantity += quantity;
    }

    public String format(double price) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        return formatter.format(price);
    }
}
