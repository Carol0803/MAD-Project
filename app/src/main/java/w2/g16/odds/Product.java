package w2.g16.odds;

public class Product {

    private String name;
    private String price;
    private String variation;
    private String quantity;

    public Product(String name, String price, String variation, String quantity) {
        this.name = name;
        this.price = price;
        this.variation = variation;
        this.quantity = quantity;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
