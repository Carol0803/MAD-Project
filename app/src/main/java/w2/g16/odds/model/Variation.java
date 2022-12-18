package w2.g16.odds.model;

public class Variation {

    private String variationID;
    private String variation_type;
    private String image;
    private String price;
    private String quantity;

    public Variation(String variationID, String variation_type, String image, String price, String quantity) {
        this.variationID = variationID;
        this.variation_type = variation_type;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
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
