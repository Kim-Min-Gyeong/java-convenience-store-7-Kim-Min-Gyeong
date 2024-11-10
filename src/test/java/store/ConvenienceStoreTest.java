package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.Constants;
import store.constant.ErrorMessages;
import store.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConvenienceStoreTest {

    private ConvenienceStore store;
    private Consumer consumer;

    @BeforeEach
    void setUp() {
        Promotion promo = new Promotion("탄산2+1", 2, 1, LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"));
        Map<String, Promotion> promotions = Map.of("탄산2+1", promo);

        List<Inventory> inventories = List.of(
                new Inventory("콜라", 1000, 10, null),
                new Inventory("사이다", 2000, 6, promo)
        );

        store = new ConvenienceStore(promotions, inventories);
        store.assignProductAndPromotionProduct();
        consumer = new Consumer(Map.of("콜라", 3));
    }

    @Test
    @DisplayName("재고 리스트에서 일반 상품 가져오기")
    void testGetProducts() {
        List<Product> products = store.getProducts();
        assertEquals(1, products.size());
        assertEquals("콜라", products.get(0).getName());
        assertEquals(1000, products.get(0).getPrice());
        assertEquals(10, products.get(0).getQuantity());
    }

    @Test
    @DisplayName("재고 리스트에서 프로모션 상품 가져오기")
    void testGetPromotionProducts() {
        List<PromotionProduct> promoProducts = store.getPromotionProducts();
        assertEquals(1, promoProducts.size());
        assertEquals("사이다", promoProducts.get(0).getName());
        assertEquals(2000, promoProducts.get(0).getPrice());
        assertEquals(6, promoProducts.get(0).getQuantity());
    }

    @Test
    @DisplayName("구매하려는 상품이 수량이 충분한 경우 테스트")
    void testCheckPurchasePossible_withSufficientInventory() {
        Map<String, Integer> wishList = Map.of("콜라", 5);
        assertDoesNotThrow(() -> store.checkPurchasePossible(wishList));
    }

    @Test
    @DisplayName("구매하려는 상품이 수량이 불충분한 경우 테스트")
    void testCheckPurchasePossible_withInsufficientInventory() {
        Map<String, Integer> wishList = Map.of("콜라", 15);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> store.checkPurchasePossible(wishList));
        assertEquals(ErrorMessages.EXCEED_PRODUCT_QUANTITY.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("구매하려는 상품이 프로모션 행사 중인데, 1개 덜 가져온 경우. 손님이 1개 더 가져오겠다고 했을 때")
    void testPurchasePromotionProduct_withGiftIncluded() {
        store.purchasePromotionProduct(Constants.YES.getConstant(), "사이다", 2, consumer);
        List<PurchaseProduct> purchaseProducts = consumer.getPurchaseProducts();
        int totalQuantity = purchaseProducts.stream()
                .filter(product -> "사이다".equals(product.getName()))
                .mapToInt(PurchaseProduct::getQuantity)
                .sum();
        assertEquals(3, totalQuantity);

        List<Gift> gifts = consumer.getGifts();
        int gift = gifts.stream()
                .filter(gift1 -> "사이다".equals(gift1.getName()))
                .mapToInt(Gift::getQuantity)
                .sum();
        assertEquals(1, gift);
    }

    @Test
    @DisplayName("구매하려는 상품이 프로모션 행사 중인데, 1개 덜 가져온 경우. 손님이 1개 더 가져오지 않겠다고 했을 때")
    void testPurchasePromotionProduct_withoutGift() {
        store.purchasePromotionProduct(Constants.NO.getConstant(), "사이다", 2, consumer);
        List<PurchaseProduct> purchaseProducts = consumer.getPurchaseProducts();
        int totalQuantity = purchaseProducts.stream()
                .filter(product -> "사이다".equals(product.getName()))
                .mapToInt(PurchaseProduct::getQuantity)
                .sum();
        assertEquals(2, totalQuantity);

        List<Gift> gifts = consumer.getGifts();
        int gift = gifts.stream()
                .filter(gift1 -> "사이다".equals(gift1.getName()))
                .mapToInt(Gift::getQuantity)
                .sum();
        assertEquals(0, gift);
    }

    @Test
    @DisplayName("구매하려는 상품이 프로모션 행사 중인지")
    void testCheckPromotionProductPossible() {
        assertFalse(store.checkPromotionProductPossible("콜라", 3));
        assertTrue(store.checkPromotionProductPossible("사이다", 4));
    }

    @Test
    @DisplayName("일반 상품 구매 테스트")
    void testPurchaseProducts() {
        store.purchaseProducts("콜라", 5, consumer);

        List<PurchaseProduct> purchaseProducts = consumer.getPurchaseProducts();
        int totalQuantity = purchaseProducts.stream()
                .filter(product -> "콜라".equals(product.getName()))
                .mapToInt(PurchaseProduct::getQuantity)
                .sum();
        assertEquals(5, totalQuantity);
        assertEquals(5, store.getInventories().stream().filter(inv -> inv.getName().equals("콜라")).findFirst().get().getQuantity());
    }

    @Test
    @DisplayName("재고가 삭감 테스트")
    void testSubtractInventory() {
        store.purchaseProducts("콜라", 3, consumer);
        int remainingQty = store.getInventories().stream()
                .filter(inv -> inv.getName().equals("콜라"))
                .findFirst().get().getQuantity();

        assertEquals(7, remainingQty);
    }
}
