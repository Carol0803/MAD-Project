package w2.g16.odds.model;

public class Address {

    private String receiver_name;
    private String receiver_tel;
    private String addr1;
    private String addr2;
    private String city;
    private String postcode;
    private String state;

    public Address(String receiver_name, String receiver_tel, String addr1, String addr2, String city, String postcode, String state) {
        this.receiver_name = receiver_name;
        this.receiver_tel = receiver_tel;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.postcode = postcode;
        this.state = state;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_tel() {
        return receiver_tel;
    }

    public void setReceiver_tel(String receiver_tel) {
        this.receiver_tel = receiver_tel;
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
}
