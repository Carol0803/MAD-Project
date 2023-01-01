package w2.g16.odds.model;

import java.util.Vector;

public class Products implements Comparable<Products> {

    private String SKU;
    private String product_name;
    private String description;
    private String rating;
    private String status;
    private String image;
    private String price;
    private String owned_by;
    private int sold_item;
    private String stock;
    private String under_category;

    public Products(String SKU, String product_name, String description, String rating, String status, String image, String price, String owned_by, int sold_item, String stock, String under_category) {
        this.SKU = SKU;
        this.product_name = product_name;
        this.description = description;
        this.rating = rating;
        this.status = status;
        this.image = image;
        this.price = price;
        this.owned_by = owned_by;
        this.sold_item = sold_item;
        this.stock = stock;
        this.under_category = under_category;
    }

    public Products(String SKU, String product_name, String image, String price, String owned_by) {
        this.SKU = SKU;
        this.product_name = product_name;
        this.image = image;
        this.price = price;
        this.owned_by = owned_by;
    }

    public Products(String SKU, String product_name, String image, String price, String owned_by, int sold_item) {
        this.SKU = SKU;
        this.product_name = product_name;
        this.image = image;
        this.price = price;
        this.owned_by = owned_by;
        this.sold_item = sold_item;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwned_by() {
        return owned_by;
    }

    public void setOwned_by(String owned_by) {
        this.owned_by = owned_by;
    }

    public int getSold_item() {
        return sold_item;
    }

    public void setSold_item(int sold_item) {
        this.sold_item = sold_item;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUnder_category() {
        return under_category;
    }

    public void setUnder_category(String under_category) {
        this.under_category = under_category;
    }

    @Override
    public int compareTo(Products compareProduct) {
        int compareSoldItem = ((Products)compareProduct).getSold_item();

        return compareSoldItem-this.sold_item;
    }
}
