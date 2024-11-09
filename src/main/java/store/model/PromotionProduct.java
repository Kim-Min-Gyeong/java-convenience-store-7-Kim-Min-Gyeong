package store.model;

import java.util.Optional;

public class PromotionProduct {

    private final String name;
    private final Integer price;
    private Integer quantity;
    private final Promotion promotion;

    public PromotionProduct(String name, Integer price, Integer quantity, Optional<Promotion> promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion.get();
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
