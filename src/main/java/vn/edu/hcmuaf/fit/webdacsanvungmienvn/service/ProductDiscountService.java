package vn.edu.hcmuaf.fit.webdacsanvungmienvn.service;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ProductDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.ProductDiscountDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.Product;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.ProductDiscount;

import java.sql.Date;
import java.util.List;

public class ProductDiscountService {
    private final ProductDiscountDAO discountDAO = new ProductDiscountDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public ProductDiscount getDiscountById(int id) {
        return discountDAO.getDiscountById(id);
    }

    public boolean existsByProductName(String productName) {
        if (productName == null) return false;
        List<ProductDiscount> all = discountDAO.getAllDiscounts();
        for (ProductDiscount d : all) {
            if (d.getProductName() != null && d.getProductName().equalsIgnoreCase(productName.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean addDiscount(ProductDiscount discount) {
        if (discount == null) return false;
        if (isInvalidDiscount(discount)) return false;
        Product p = productDAO.getProductById(discount.getProductId());
        if (p == null) return false;
        if (existsByProductName(p.getName())) return false;
        return discountDAO.addDiscount(discount);
    }

    public boolean addDiscount(int productId, double discountPercent, Date startDate, Date endDate) {
        ProductDiscount discount = new ProductDiscount();
        discount.setProductId(productId);
        discount.setDiscountPercentage((int) discountPercent);
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);
        return addDiscount(discount);
    }

    public boolean updateDiscount(ProductDiscount discount) {
        if (discount == null) return false;
        if (discount.getId() <= 0) return false;
        if (isInvalidDiscount(discount)) return false;
        Product p = productDAO.getProductById(discount.getProductId());
        if (p == null) return false;
        return discountDAO.updateDiscount(discount);
    }

    public boolean deleteDiscount(int id) {
        if (id <= 0) return false;
            return discountDAO.deleteDiscount(id);
    }

    public List<ProductDiscount> getDiscountsPaged(int page, int pageSize) {
        return discountDAO.getDiscountsPaged(page, pageSize);
    }

    public int countAllDiscounts() {
        return discountDAO.countAllDiscounts();
    }

    public List<ProductDiscount> searchDiscountsPaged(String keyword, int page, int pageSize) {
        if (keyword == null) keyword = "";
        return discountDAO.searchDiscountsPaged(keyword.trim(), page, pageSize);
    }

    public int countSearchDiscounts(String keyword) {
        if (keyword == null) keyword = "";
        return discountDAO.countSearchDiscounts(keyword.trim());
    }

    private boolean isInvalidDiscount(ProductDiscount discount) {
        if (discount == null) return true;
        int pct = discount.getDiscountPercentage();
        if (pct < 1 || pct > 100) return true;
        Date start = discount.getStartDate();
        Date end = discount.getEndDate();
        if (start == null || end == null) return true;
        return start.after(end);
    }
}
