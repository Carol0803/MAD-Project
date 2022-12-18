package w2.g16.odds.model;

import java.util.Vector;

public class Products {

    private String SKU;
    private String product_name;
    private String description;
    private String rating;
    private String status;
    private Shop shop;
    private Category category;
    private Vector<Variation> variation;

    public Products(String SKU, String product_name, String description, String rating, String status, Shop shop, Category category, Vector<Variation> variation) {
        this.SKU = SKU;
        this.product_name = product_name;
        this.description = description;
        this.rating = rating;
        this.status = status;
        this.shop = shop;
        this.category = category;
        this.variation = variation;
    }

    public Products(String SKU, String product_name, Vector<Variation> variation) {
        this.SKU = SKU;
        this.product_name = product_name;
        this.variation = variation;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Vector<Variation> getVariation() {
        return variation;
    }

    public void setVariation(Vector<Variation> variation) {
        this.variation = variation;
    }
}
