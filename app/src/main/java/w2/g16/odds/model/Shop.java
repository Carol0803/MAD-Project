package w2.g16.odds.model;

import java.util.List;

public class Shop {

    private String shopID;
    private String shop_name;
    private String tel_no;
    private String shop_open;
    private String shop_close;
    private String addr1;
    private String addr2;
    private String city;
    private String postcode;
    private String state;
    private String owner;
    private double shop_rating;
    private List<Cart> cartList;
    private String longitude;
    private String latitude;

    public Shop(){}

    public Shop(String shopID, String shopname) {
        this.shopID = shopID;
        this.shop_name = shopname;
    }

    public Shop(String shopID, String shopname, List<Cart> cartList) {
        this.shopID = shopID;
        this.shop_name = shopname;
        this.cartList = cartList;
    }

    public Shop(String shopID, String shopname, String tel_no, String shop_open, String shop_close, String addr1, String addr2, String city, String postcode, String state, String owner, double rating, List<Cart> cartList, String longitude, String latitude) {
        this.shopID = shopID;
        this.shop_name = shopname;
        this.tel_no = tel_no;
        this.shop_open = shop_open;
        this.shop_close = shop_close;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.postcode = postcode;
        this.state = state;
        this.owner = owner;
        this.shop_rating = rating;
        this.cartList = cartList;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Shop(String shopID, String shopname, String shop_open, String shop_close, double rating) {
        this.shopID = shopID;
        this.shop_name = shopname;
        this.shop_open = shop_open;
        this.shop_close = shop_close;
        this.shop_rating = rating;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getShop_rating() {
        return shop_rating;
    }

    public void setShop_rating(double shop_rating) {
        this.shop_rating = shop_rating;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        Shop itemCompare = (Shop) obj;
        if(itemCompare.getShop_name().equals(this.getShop_name()))
            return true;

        return false;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getShop_open() {
        return shop_open;
    }

    public void setShop_open(String shop_open) {
        this.shop_open = shop_open;
    }

    public String getShop_close() {
        return shop_close;
    }

    public void setShop_close(String shop_close) {
        this.shop_close = shop_close;
    }
}
