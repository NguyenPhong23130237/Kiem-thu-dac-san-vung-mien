package vn.edu.hcmuaf.fit.webdacsanvungmienvn.model;

import java.sql.Date;

public class CategoryDiscount {
    private int id;
    private int categoryId;
    private String categoryName;
    private int discountPercentage;
    private java.sql.Date startDate;
    private java.sql.Date endDate;

    public CategoryDiscount() {
    }

    public CategoryDiscount(int id, int categoryId, int discountPercentage, java.sql.Date startDate, java.sql.Date endDate) {
        this.id = id;
        this.categoryId = categoryId;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static String getStatus(java.sql.Date startDate, java.sql.Date endDate) {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        if (startDate == null || endDate == null) {
            return "Không xác định";
        }
        if (today.before(startDate)) {
            return "Sắp áp dụng";
        } else if (today.after(endDate)) {
            return "Đã kết thúc";
        } else {
            return "Đang áp dụng";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public java.sql.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
