package store.model;

public class PurchaseProduct {
    private final String name;
    private final Integer quantity;
    private final Integer price;

    public PurchaseProduct(String name, Integer quantity, Integer price){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }
}
