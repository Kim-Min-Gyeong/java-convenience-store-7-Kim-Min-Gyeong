package store.model;

import java.util.Optional;

public class Inventory {

    private final String name;

    private final Integer price;

    private Integer quantity;

    private Optional<Promotion> promotion;

    public Inventory(String name, Integer price, Integer quantity, Promotion promotion){
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

    public String getName() {
        return name;
    }

    public Integer getPrice() {return price;}

    public Integer getQuantity() {return quantity;}

    public Optional<Promotion> getPromotion() {
        return promotion;
    }

//    public boolean hasPromotion(String purchaseName){ //프로모션 상품인지 확인
//        if(name.equals(purchaseName)){
//            if(!promotion.isEmpty()) return true;
//        }
//        return false;
//    }

//    public boolean hasQuantity(String purchaseName, Integer quantity, boolean hasPromotion){
//        if(name.equals(purchaseName)){
//            if(hasPromotion && !promotion.isEmpty()){
//
//            }
//        }
//    }

}
