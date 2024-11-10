package store.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Consumer {
    private final Map<String, Integer> wishList; //장바구니
    private List<PurchaseProduct> purchaseProducts;
    private List<Gift> gifts;
    private Integer totalQty; //총 구매 수량
    private Integer totalAmount; //총 구매액
    private Integer nonPromotion; //프로모션 적용 후 남은 금액
    private Integer eventDiscount; //행사 할인 금액
    private Integer membershipDiscount; //멤버십 할인 금액
    private Integer amountToPay; //내실 돈

    public Consumer(Map<String, Integer> wishList) {
        this.wishList = wishList;
        this.purchaseProducts = new ArrayList<>();
        this.gifts = new ArrayList<>();
        totalQty = 0;
        totalAmount = 0;
        nonPromotion = 0;
        eventDiscount = 0;
        membershipDiscount = 0;
        amountToPay = 0;
    }

    public Map<String, Integer> getWishList() {
        return wishList;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setEventDiscount(Integer eventDiscount) {
        this.eventDiscount = eventDiscount;
    }

    public void setMembershipDiscount(Integer membershipDiscount) {
        this.membershipDiscount = membershipDiscount;
    }

    public void setAmountToPay(Integer amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public Integer getEventDiscount() {
        return eventDiscount;
    }

    public Integer getMembershipDiscount() {
        return membershipDiscount;
    }

    public Integer getAmountToPay() {
        return amountToPay;
    }

    public void setNonPromotion(Integer nonPromotion) {
        this.nonPromotion = nonPromotion;
    }

    public Integer getNonPromotion() {
        return nonPromotion;
    }

    public List<PurchaseProduct> getPurchaseProducts() {
        return purchaseProducts;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void addProduct(PurchaseProduct product){purchaseProducts.add(product);}

    public void addGift(Gift gift){gifts.add(gift);}
}
