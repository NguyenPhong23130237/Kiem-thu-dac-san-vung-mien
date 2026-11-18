package vn.edu.hcmuaf.fit.webdacsanvungmienvn.service;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.dao.CategoryDiscountDAO;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.model.CategoryDiscount;

import java.sql.Date;
import java.util.List;

public class CategoryDiscountService {
    private final CategoryDiscountDAO categoryDiscountDAO = new CategoryDiscountDAO();

    public List<CategoryDiscount> getAllCategoryDiscounts() {
        return categoryDiscountDAO.getAllCategoryDiscounts();
    }


    public CategoryDiscount getDiscountById(int id) {
        return categoryDiscountDAO.getCategoryDiscountById(id);
    }

    public boolean existsByCategoryId(int categoryId) {
        List<CategoryDiscount> all = categoryDiscountDAO.getAllCategoryDiscounts();
        for (CategoryDiscount d : all) {
            if (d.getCategoryId() == categoryId) return true;
        }
        return false;
    }

    public boolean addDiscount(CategoryDiscount discount) {
        if (discount == null) return false;
        if (isInvalidDiscount(discount)) return false;
        if (existsByCategoryId(discount.getCategoryId())) return false;
        return categoryDiscountDAO.insertCategoryDiscount(discount);
    }

    public boolean updateDiscount(CategoryDiscount discount) {
        if (discount == null) return false;
        if (discount.getId() <= 0) return false;
        if (isInvalidDiscount(discount)) return false;
        return categoryDiscountDAO.updateCategoryDiscount(discount);
    }

    public boolean deleteDiscount(int id) {
        if (id <= 0) return false;
        return categoryDiscountDAO.deleteCategoryDiscount(id);
    }

    public List<CategoryDiscount> getDiscountsPaged(int page, int pageSize) {
        return categoryDiscountDAO.getDiscountsPaged(page, pageSize);
    }


    public int countAllDiscounts() {
        return categoryDiscountDAO.countAllDiscounts();
    }

    public List<CategoryDiscount> searchCategoryDiscountsPaged(String keyword, int page, int pageSize) {
        return categoryDiscountDAO.searchCategoryDiscountsPaged(keyword, page, pageSize);
    }

    public int countSearchCategoryDiscounts(String keyword) {
        return categoryDiscountDAO.countSearchCategoryDiscounts(keyword);
    }

    private boolean isInvalidDiscount(CategoryDiscount discount) {
        if (discount == null) return true;
        int pct = discount.getDiscountPercentage();
        if (pct < 1 || pct > 100) return true;
        Date start = discount.getStartDate();
        Date end = discount.getEndDate();
        if (start == null || end == null) return true;
        return start.after(end);
    }
}
