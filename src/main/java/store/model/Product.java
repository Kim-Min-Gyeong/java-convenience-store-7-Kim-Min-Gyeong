package store.model;

import java.util.Optional;

public class Product {

    private final String name;

    private final Integer price;

    private Integer quantity;

    private Optional<Promotion> promotion;

    public Product(String name, Integer price, Integer quantity, Promotion promotion){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = Optional.ofNullable(promotion);
    }

    @Override
    public String toString() {
        String result = "- " + name + " " + price + "원 ";

        if (quantity == 0) {
            return promotion.map(value -> result + "재고 없음 " + value.getName())
                    .orElse(result + "재고 없음");
        }

        return promotion.map(value -> result + quantity + "개 " + value.getName())
                .orElse(result + quantity + "개");
    }

}
