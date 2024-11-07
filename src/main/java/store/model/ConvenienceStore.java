package store.model;

import store.constant.ErrorMessages;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvenienceStore {

    private final Map<String, Promotion> promotions; //프로모션

    private List<Inventory> inventories; //재고 리스트

    private List<Product> products; //일반 상품

    private List<PromotionProduct> promotionProducts; //프로모션 상품

    public ConvenienceStore(Map<String, Promotion> promotions, List<Inventory> inventories){
        this.promotions = promotions;
        this.inventories = inventories;
    }

    public Map<String, Promotion> getPromotions() {
        return promotions;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void assignProductAndPromotionProduct(){
        products = getProducts();
        promotionProducts = getPromotionProducts();
    }

    public List<Product> getProducts() {
        return inventories.stream()
                .filter(inventory -> inventory.getPromotion().isEmpty())
                .map(inventory -> new Product(inventory.getName(), inventory.getPrice(), inventory.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<PromotionProduct> getPromotionProducts() {
        return inventories.stream()
                .filter(inventory -> !inventory.getPromotion().isEmpty())
                .map(inventory -> new PromotionProduct(inventory.getName(), inventory.getPrice(), inventory.getQuantity(), inventory.getPromotion()))
                .collect(Collectors.toList());
    }

    public void checkPurchasePossible(Map<String, Integer> purchaseList){
        purchaseList.forEach((name, quantity) -> {
            if (!isContain(name)) { //존재하지 않는 상품
                throw new IllegalArgumentException(ErrorMessages.NOT_EXIST_PRODUCT.getMessage());
            }
            if(!enoughQuantity(name, quantity)){ //구매 수량이 재고 수량을 초과한 경우
                throw new IllegalArgumentException(ErrorMessages.EXCEED_PRODUCT_QUANTITY.getMessage());
            }
        });
    }

    private boolean isContain(String purchaseName){
        //프로모션 상품인 경우
        if(promotionProducts.stream().anyMatch(promotionProduct -> promotionProduct.getName().equals(purchaseName))) {
            return true;
        }
        //일반 상품인 경우
        if(products.stream().anyMatch(product -> product.getName().equals(purchaseName))) return true;
        return false;
    }

    private boolean enoughQuantity(String purchaseName, Integer quantity){
        int cnt = 0;
        //프로모션 상품이 있는지 확인
        for(PromotionProduct promotionProduct: promotionProducts){
            if(promotionProduct.getName().equals(purchaseName)) cnt = promotionProduct.getQuantity();
        }
        //일반 상품이 있는지 확인
        for(Product product: products){
            if(product.getName().equals(purchaseName)) cnt += product.getQuantity();
        }
        return cnt >= quantity;
    }

}
