package store.model;

import store.constant.Constants;
import store.constant.ErrorMessages;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvenienceStore {

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
        Promotion promo = getPromotion(name);
        int n = promo.getBuyQuantity(), m = promo.getGetQuantity();
        consumer.addPurchase(name, qty / (n + m) * n + qty % (n + m));
        int bonus = (qty / (n + m) * m);
        if(answer.equals(Constants.YES.getConstant())) {
            bonus = bonus+1;
        }
        subtractInventory(name, bonus, true);
//        System.out.println("상품명: "+name+" 구매 수량 " + (qty / (n + m) * n + qty % (n + m)));
//        System.out.println("상품명: "+name+" 증정 수량 " + bonus);
    }

    private Promotion getPromotion(String name) { // 프로모션 제품 리턴
        return promotionProducts.stream().filter(p -> p.getName().equals(name))
                .map(PromotionProduct::getPromotion).findFirst().orElse(null);
    }

    public void purchasePromotionProduct(String name, Integer qty, Consumer consumer) { // 프로모션 재고 내에서 구매 가능
        Promotion promo = getPromotion(name);
        int n = promo.getBuyQuantity(), m = promo.getGetQuantity();
        consumer.addPurchase(name, qty / (n + m) * n + qty % (n + m));
        consumer.addGift(name, qty / (n + m) * m);
        subtractInventory(name, qty, true);
//        System.out.println("상품명: "+name+" 구매 수량 " + (qty / (n + m) * n + qty % (n + m)));
//        System.out.println("상품명: "+name+" 증정 수량 " + (qty / (n + m) * m));
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
        consumer.addGift(name, totalBonusUnits);
//        System.out.println("상품명: "+name+" 증정 수량 " + totalBonusUnits);
        if (answer.equals(Constants.YES.getConstant())) {
            consumer.addPurchase(name, maxPromoUnits + remQty);
//            System.out.println("상품명: "+name+" 구매 수량 " + (maxPromoUnits + remQty));
            subtractInventory(name, maxPromoUnits, true);
            subtractInventory(name, remQty, false);
            return;
        }
        consumer.addPurchase(name, maxPromoUnits);
//        System.out.println("상품명: "+name+" 구매 수량 " + maxPromoUnits);
        subtractInventory(name, maxPromoUnits + totalBonusUnits, true);

    }

    public void purchaseProducts(String name, Integer qty, Consumer consumer) { // 일반 제품 구매
        consumer.addPurchase(name, qty);
        subtractInventory(name, qty, false);
//        System.out.println("상품명: "+name+" 구매 수량 " + qty);
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
}
