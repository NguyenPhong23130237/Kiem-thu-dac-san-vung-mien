package vn.edu.hcmuaf.fit.webdacsanvungmienvn.model;

import java.sql.Date;

public class ProductDiscount {
    private int id;
    private int productId;
    private String productName;
    private int discountPercentage;
    private Date startDate;
    private Date endDate;

    public ProductDiscount() {
    }

    public ProductDiscount(int id, int productId, int discountPercentage, Date startDate, Date endDate) {
        this.id = id;
        this.productId = productId;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static String getStatus(Date startDate, Date endDate) {
        Date today = new Date(System.currentTimeMillis());
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
