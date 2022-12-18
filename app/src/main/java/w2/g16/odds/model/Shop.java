package w2.g16.odds.model;

public class Shop {

    private String shopID;
    private String shopname;
    private String tel_no;
    private String operating_hour;
    private String addr1;
    private String addr2;
    private String city;
    private String postcode;
    private String state;
    private String owner;
    private String rating;

    public Shop(String shopID, String shopname) {
        this.shopID = shopID;
        this.shopname = shopname;
    }

    public Shop(String shopID, String shopname, String tel_no, String operating_hour, String addr1, String addr2, String city, String postcode, String state, String owner, String rating) {
        this.shopID = shopID;
        this.shopname = shopname;
        this.tel_no = tel_no;
        this.operating_hour = operating_hour;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.postcode = postcode;
        this.state = state;
        this.owner = owner;
        this.rating = rating;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shop_name) {
        this.shopname = shop_name;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getOperating_hour() {
        return operating_hour;
    }

    public void setOperating_hour(String operating_hour) {
        this.operating_hour = operating_hour;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        Shop itemCompare = (Shop) obj;
        if(itemCompare.getShopname().equals(this.getShopname()))
            return true;

        return false;
    }
}
