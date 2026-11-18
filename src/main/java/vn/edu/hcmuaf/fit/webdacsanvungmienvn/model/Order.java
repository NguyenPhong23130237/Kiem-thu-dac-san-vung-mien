package vn.edu.hcmuaf.fit.webdacsanvungmienvn.model;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private String status;
    private String paymentMethod;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Date date;
    private String note;
    private List<OrderItem> orderItems;
    private double total;
    private String signature;
    private int keyId;
    private Date signed_at;
    private String signature_algorithm;
    private String publicKey;

    public Order() {
    }

    public Order(int id, int userId, String status, String paymentMethod, String fullName, String phoneNumber, String address, Date date, String note, List<OrderItem> orderItems, double total) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.date = date;
        this.note = note;
        this.orderItems = orderItems;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Date getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public double getTotal() {
        return total;
    }

    public String getSignature() { return signature; }

    public int getKeyId() { return keyId; }

    public Date getSigned_at() { return signed_at; }

    public String getSignature_algorithm() { return signature_algorithm; }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setSignature(String signature) { this.signature = signature; }

    public void setKeyId(int keyId) { this.keyId = keyId; }

    public void setSigned_at(Date signed_at) { this.signed_at = signed_at; }

    public void setSignature_algorithm(String signature_algorithm) { this.signature_algorithm = signature_algorithm; }

    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }

    public String format(double price) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        return formatter.format(price);
    }
}
