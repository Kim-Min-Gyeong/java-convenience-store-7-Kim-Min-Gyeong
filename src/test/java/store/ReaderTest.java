package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Inventory;
import store.model.Promotion;
import store.util.Reader;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {
    private Map<String, Promotion> promotions;

    @BeforeEach
    void setUp() {
        promotions = Reader.readPromotions();
    }

    @Test
    @DisplayName("promotions.md 파일 제대로 읽어 왔는지 테스트")
    void readPromotions_ShouldReturnCorrectPromotionDetails() {
        assertEquals(3, promotions.size());

        Promotion promotion = promotions.get("탄산2+1");
        assertNotNull(promotion);
        assertEquals(2, promotion.getBuyQuantity());
        assertEquals(1, promotion.getGetQuantity());
        assertEquals(LocalDate.of(2024, 1, 1), promotion.getStartDate());
        assertEquals(LocalDate.of(2024, 12, 31), promotion.getEndDate());
    }

    @Test
    @DisplayName("products.md 파일 제대로 읽어 오는지 테스트")
    void readInventory_ShouldAddPromotionToInventory_WhenPromotionIsActive() {
        List<Inventory> inventories = Reader.readInventory(promotions);
        Inventory inventoryWithPromotion = inventories.stream()
                .filter(inv -> inv.getName().equals("감자칩"))
                .findFirst()
                .orElse(null);

        assertNotNull(inventoryWithPromotion);
        assertTrue(inventoryWithPromotion.getPromotion().isPresent());
        assertEquals("반짝할인", inventoryWithPromotion.getPromotion().get().getName());
    }

}
