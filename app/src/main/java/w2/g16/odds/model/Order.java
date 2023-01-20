package w2.g16.odds.model;

import java.io.Serializable;

public class Order implements Serializable {
    private String orderID;
    private String orderDate;
    private String amount;
    private String shopname;
    private String SKU;
    private String product_name;
    private String product_price;
    private String quantity;
    private String subtotal;
    private String product_img;
    private String order_status;
    private String rated;

    public Order(String orderID, String orderDate, String amount, String shopname, String SKU, String product_name, String product_price, String quantity, String subtotal, String product_img, String order_status, String rated) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.amount = amount;
        this.shopname = shopname;
        this.SKU = SKU;
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.product_img = product_img;
        this.order_status = order_status;
        this.rated = rated;
    }

/*    public Order(String orderID, String amount, String shopname, String product_name, String quantity, String subtotal, String product_img) {
        this.orderID = orderID;
        this.amount = amount;
        this.shopname = shopname;
        this.product_name = product_name;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.product_img = product_img;
    }*/

    public Order(String orderID, String amount, String shopname, String product_name, String quantity, String subtotal, String product_img, String order_status) {
        this.orderID = orderID;
        this.amount = amount;
        this.shopname = shopname;
        this.product_name = product_name;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.product_img = product_img;
        this.order_status = order_status;
    }

    public Order(String orderID, String SKU, String product_name, String product_price, String quantity, String product_img, String rated) {
        this.orderID = orderID;
        this.SKU = SKU;
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.product_img = product_img;
        this.rated = rated;
    }

    public Order(String SKU, String product_name, String product_price, String quantity, String product_img) {
        this.SKU = SKU;
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.product_img = product_img;
    }

    public Order(String shopname, String quantity) {
        this.shopname = shopname;
        this.quantity = quantity;
    }

    /*  public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }*/

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }
}
