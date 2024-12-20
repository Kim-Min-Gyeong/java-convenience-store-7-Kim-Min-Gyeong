package store.model;

import store.constant.Constants;
import store.constant.ErrorMessages;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvenienceStore {

    private static final Double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final Integer MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final Map<String, Promotion> promotions;
    private List<Inventory> inventories;
    private List<Product> products;
    private List<PromotionProduct> promotionProducts;

    public ConvenienceStore(Map<String, Promotion> promotions, List<Inventory> inventories) {
        this.promotions = promotions;
        this.inventories = inventories;
    }

    public List<Inventory> getInventories() { return inventories; }

    public void assignProductAndPromotionProduct() {
        products = getProducts();
        promotionProducts = getPromotionProducts();
    }

    public List<Product> getProducts() { // 재고 리스트로부터 일반 상품 가져오기
        return inventories.stream().filter(inv -> inv.getPromotion().isEmpty())
                .map(inv -> new Product(inv.getName(), inv.getPrice(), inv.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<PromotionProduct> getPromotionProducts() { // 재고 리스트로부터 프로모션 상품 가져오기
        return inventories.stream().filter(inv -> !inv.getPromotion().isEmpty())
                .map(inv -> new PromotionProduct(inv.getName(), inv.getPrice(), inv.getQuantity(), inv.getPromotion()))
                .collect(Collectors.toList());
    }

    public void checkPurchasePossible(Map<String, Integer> wishList) { // 적절한지 확인
        wishList.forEach((name, qty) -> {
            if (!isContain(name)) throw new IllegalArgumentException(ErrorMessages.NOT_EXIST_PRODUCT.getMessage());
            if (!enoughQuantity(name, qty)) throw new IllegalArgumentException(ErrorMessages.EXCEED_PRODUCT_QUANTITY.getMessage());
        });
    }

    private boolean isContain(String purchaseName) { // 존재하는 상품 확인
        return promotionProducts.stream().anyMatch(p -> p.getName().equals(purchaseName))
                || products.stream().anyMatch(p -> p.getName().equals(purchaseName));
    }

    private boolean enoughQuantity(String purchaseName, Integer qty) { // 재고가 충분한지 확인
        int cnt = promotionProducts.stream().filter(p -> p.getName().equals(purchaseName))
                .mapToInt(PromotionProduct::getQuantity).sum()
                + products.stream().filter(p -> p.getName().equals(purchaseName))
                .mapToInt(Product::getQuantity).sum();
        return cnt >= qty;
    }

    public boolean checkPromotionProductPossible(String name, Integer qty) { // 프로모션 재고 구매 가능 여부
        return isPromotion(name, qty);
    }

    private boolean isPromotion(String name, Integer qty) { // 프로모션 중인지 확인
        return promotionProducts.stream().filter(p -> p.getName().equals(name))
                .anyMatch(p -> canPurchaseWithPromotion(qty, p.getQuantity(), p.getPromotion()));
    }

    public static boolean canPurchaseWithPromotion(int qty, int invQty, Promotion promo) { // 프로모션 재고로 구매 가능 여부
        int bundleSize = promo.getBuyQuantity() + promo.getGetQuantity();
        int fullBundles = qty / bundleSize;
        int remainingItems = qty % bundleSize;
        return fullBundles * bundleSize + remainingItems <= invQty;
    }

    public boolean checkBringOneMore(String name, Integer qty) { // 증정 상품 1개 더 가져와야 하는지 확인
        Promotion promo = getPromotion(name);
        int n = promo.getBuyQuantity(), m = promo.getGetQuantity();
        return qty % (n + m) == n && promotionProducts.stream()
                .anyMatch(p -> p.getName().equals(name) && !p.getQuantity().equals(qty));
    }

    public void purchasePromotionProduct(String answer, String name, Integer qty, Consumer consumer) { // Y: 증정 추가, N: 추가 x
        int n = getPromotion(name).getBuyQuantity(), m = getPromotion(name).getGetQuantity(), bonus = qty / (n + m) * m;
        if(answer.equals(Constants.YES.getConstant())) {
            consumer.addGift(new Gift(name, ++bonus, getPrice(name, true)));
        }
        consumer.addProduct(new PurchaseProduct(name, bonus + (qty / (n + m) * n) + (qty % (n + m)), getPrice(name, true)));
        subtractInventory(name, bonus, true);
        consumer.setTotalQty(consumer.getTotalQty() + bonus + (qty / (n + m) * n + qty % (n + m)));
        consumer.setTotalAmount(consumer.getTotalAmount() + (getPrice(name, true) * (bonus + (qty / (n + m) * n + qty % (n + m)))));
        consumer.setEventDiscount(consumer.getEventDiscount() + bonus * getPrice(name, true));
    }

    private Promotion getPromotion(String name) { // 프로모션 제품 리턴
        return promotionProducts.stream().filter(p -> p.getName().equals(name))
                .map(PromotionProduct::getPromotion).findFirst().orElse(null);
    }

    public void purchasePromotionProduct(String name, Integer qty, Consumer consumer) { // 프로모션 재고 내에서 구매 가능
        Promotion promo = getPromotion(name);
        int n = promo.getBuyQuantity(), m = promo.getGetQuantity();
        consumer.addProduct(new PurchaseProduct(name, qty, getPrice(name, true)));
        consumer.addGift(new Gift(name, qty / (n + m) * m, getPrice(name, true)));
        subtractInventory(name, qty, true);
        consumer.setTotalQty(consumer.getTotalQty() + qty);
        consumer.setTotalAmount(consumer.getTotalAmount() + (getPrice(name, true) * qty));
        consumer.setEventDiscount(consumer.getEventDiscount() + (qty / (n + m) * m) * getPrice(name, true));
    }

    public Integer getProductQtyWithoutPromotion(String name, Integer qty) { // 일반 재고로 구매해야 하는 개수
        PromotionProduct pp = promotionProducts.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
        if (pp == null) return -1;
        int n = pp.getPromotion().getBuyQuantity(), m = pp.getPromotion().getGetQuantity();
        int promoSize = n + m, maxPromoQty = (pp.getQuantity() / promoSize) * n;
        int bonus = (pp.getQuantity() / promoSize) * m;
        return qty - maxPromoQty - bonus;
    }

    public void promotionProductWithProduct(String answer, String name, int qty, Consumer consumer) { // 프로모션 + 일반 재고
        Promotion promo = getPromotion(name);
        int promoSize = promo.getBuyQuantity() + promo.getGetQuantity();
        int promoInv = getPromotionInventory(name);
        int maxPromoUnits = calculateMaxPromoUnits(promoInv, promoSize, promo.getBuyQuantity());
        int totalBonusUnits = calculateTotalBonusUnits(promoInv, promoSize, promo.getGetQuantity());
        int remQty = qty - maxPromoUnits - totalBonusUnits;
        processConsumerPurchase(answer, name, maxPromoUnits, remQty, totalBonusUnits, consumer);
    }

    private int getPromotionInventory(String name) {
        return promotionProducts.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .map(PromotionProduct::getQuantity)
                .orElse(0);
    }

    private int calculateMaxPromoUnits(int promoInv, int promoSize, int buyQuantity) {
        return (promoInv / promoSize) * buyQuantity;
    }

    private int calculateTotalBonusUnits(int promoInv, int promoSize, int getQuantity) {
        return (promoInv / promoSize) * getQuantity;
    }

    private void processConsumerPurchase(String answer, String name, int maxPromoUnits, int remQty, int totalBonusUnits, Consumer consumer) {
        consumer.addGift(new Gift(name, totalBonusUnits, getPrice(name, true)));
        consumer.setEventDiscount(consumer.getEventDiscount() + totalBonusUnits * getPrice(name, true));
        if (answer.equals(Constants.YES.getConstant())) {
            processYesAnswer(name, maxPromoUnits, remQty, totalBonusUnits, consumer);
            return;
        }
        processNoAnswer(name, maxPromoUnits, totalBonusUnits, consumer);
    }

    private void processYesAnswer(String name, int maxPromoUnits, int remQty, int totalBonusUnits, Consumer consumer) {
        int totalQty = maxPromoUnits + remQty + totalBonusUnits;
        int totalAmount = getPrice(name, true) * totalQty;
        consumer.addProduct(new PurchaseProduct(name, totalQty, getPrice(name, true)));
        consumer.setTotalQty(consumer.getTotalQty() + totalQty);
        consumer.setTotalAmount(consumer.getTotalAmount() + totalAmount);
        consumer.setNonPromotion(consumer.getNonPromotion() + getPrice(name, true) * remQty);
        updateInventory(name, maxPromoUnits, remQty, totalBonusUnits);
    }

    private void processNoAnswer(String name, int maxPromoUnits, int totalBonusUnits, Consumer consumer) {
        consumer.addProduct(new PurchaseProduct(name, maxPromoUnits + totalBonusUnits, getPrice(name, true)));
        consumer.setTotalQty(consumer.getTotalQty() + totalBonusUnits + maxPromoUnits);
        consumer.setTotalAmount(consumer.getTotalAmount() + getPrice(name, true) * (maxPromoUnits + totalBonusUnits));
        subtractInventory(name, maxPromoUnits + totalBonusUnits, true);
    }

    private void updateInventory(String name, int maxPromoUnits, int remQty, int totalBonusUnits) {
        Integer pp = promotionProducts.stream()
                .filter(p -> p.getName().equals(name))
                .map(p -> p.getQuantity() - maxPromoUnits - totalBonusUnits)
                .findFirst().orElse(0);
        subtractInventory(name, maxPromoUnits + totalBonusUnits + pp, true);
        subtractInventory(name, remQty - pp, false);
    }


    public void purchaseProducts(String name, Integer qty, Consumer consumer) { // 일반 제품 구매
        consumer.addProduct(new PurchaseProduct(name, qty, getPrice(name, false)));
        subtractInventory(name, qty, false);
        consumer.setTotalQty(consumer.getTotalQty() + qty);
        consumer.setTotalAmount(consumer.getTotalAmount() + (getPrice(name, false) * qty));
        consumer.setNonPromotion(consumer.getNonPromotion() + (getPrice(name, false) * qty));
    }

    private Integer getPrice(String name, Boolean isPromotion){
        if(isPromotion){
            for(PromotionProduct promotionProduct: promotionProducts) {
                if(promotionProduct.getName().equals(name)) return promotionProduct.getPrice();
            }
        }
        for(Product product: products){
            if(product.getName().equals(name)) return product.getPrice();
        }
        return -1;
    }

    private void subtractInventory(String name, Integer qty, Boolean isPromotion) {
        inventories.stream().filter(inv -> inv.getName().equals(name) && (isPromotion == !inv.getPromotion().isEmpty()))
                .forEach(inv -> inv.setQuantity(inv.getQuantity() - qty));
        if (isPromotion) {
            promotionProducts.stream().filter(pp -> pp.getName().equals(name))
                    .forEach(pp -> pp.setQuantity(pp.getQuantity() - qty));
            return;
        }
        products.stream().filter(p -> p.getName().equals(name)).forEach(p -> p.setQuantity(p.getQuantity() - qty));
    }

    public void calculateMemberShipDiscount(Consumer consumer, String answer){
        if(answer.equals(Constants.NO.getConstant())){
            consumer.setMembershipDiscount(0);
            return;
        }
        Integer nonPromotion = consumer.getNonPromotion();
        int membershipDiscount = (int) (nonPromotion * MEMBERSHIP_DISCOUNT_RATE);
        membershipDiscount = Math.min(membershipDiscount, MAX_MEMBERSHIP_DISCOUNT);
        consumer.setMembershipDiscount(membershipDiscount);
    }

    public void calculateAmountToPay(Consumer consumer){
        Integer amountToPay = consumer.getTotalAmount() - consumer.getEventDiscount() - consumer.getMembershipDiscount();
        consumer.setAmountToPay(amountToPay);
    }
}
