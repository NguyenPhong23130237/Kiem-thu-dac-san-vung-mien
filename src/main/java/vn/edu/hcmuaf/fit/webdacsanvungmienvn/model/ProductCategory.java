package vn.edu.hcmuaf.fit.webdacsanvungmienvn.model;

public class ProductCategory {
    private int id;
    private String name;
    private int productCount;

    public ProductCategory() {
    }

    public ProductCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
