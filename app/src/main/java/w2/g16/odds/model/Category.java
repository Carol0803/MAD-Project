package w2.g16.odds.model;

public class Category {

    private String categoryID;
    private String categoryName;
    private String img;
    private String product_quantity;

    public Category(String categoryID, String categoryName, String img, String product_quantity) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.img = img;
        this.product_quantity = product_quantity;
    }

    public Category(String categoryID, String categoryName, String img) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.img = img;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
}
