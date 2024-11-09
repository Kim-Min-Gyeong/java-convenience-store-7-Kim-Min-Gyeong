package store.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Consumer {
    private final Map<String, Integer> wishList; //장바구니

    private Map<String, Integer> purchaseList; //일반 상품 구매 내역

    private Map<String, Integer> giftList; //프로모션 상품 구매 내역

    public Consumer(Map<String, Integer> wishList) {
        this.wishList = wishList;
        this.purchaseList = new LinkedHashMap<>();
        this.giftList = new LinkedHashMap<>();
    }

    public Map<String, Integer> getWishList() {
        return wishList;
    }

    public Map<String, Integer> getPurchaseList() {
        return purchaseList;
    }

    public Map<String, Integer> getGiftList() {
        return giftList;
    }

    public void addPurchase(String name, Integer quantity){
        purchaseList.put(name, quantity);
    }

    public void addGift(String name, Integer quantity){
        wishList.put(name, quantity);
    }
}
