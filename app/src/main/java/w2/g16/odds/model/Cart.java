package w2.g16.odds.model;

import java.util.Vector;

public class Cart {

    /*private Shop shop;
    private Vector<Products> products;

    public Cart(Shop shop, Vector<Products> products) {
        this.shop = shop;
        this.products = products;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Vector<Products> getProducts() {
        return products;
    }

    public void setProducts(Vector<Products> products) {
        this.products = products;
    }*/

    private String shopID;
    private String cartID;
    private String SKU;
    private String product_name;
    private String variationID;
    private String variation_type;
    private String image;
    private String price;
    private String quantity;

    public Cart(String shopID, String cartID, String SKU, String product_name, String variationID, String variation_type, String image, String price, String quantity) {
        this.shopID = shopID;
        this.cartID = cartID;
        this.SKU = SKU;
        this.product_name = product_name;
        this.variationID = variationID;
        this.variation_type = variation_type;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
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

    public String getVariationID() {
        return variationID;
    }

    public void setVariationID(String variationID) {
        this.variationID = variationID;
    }

    public String getVariation_type() {
        return variation_type;
    }

    public void setVariation_type(String variation_type) {
        this.variation_type = variation_type;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
