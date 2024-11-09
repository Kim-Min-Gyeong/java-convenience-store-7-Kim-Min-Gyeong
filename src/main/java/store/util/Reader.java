package store.util;
import camp.nextstep.edu.missionutils.DateTimes;
import store.constant.Constants;
import store.constant.ProductConstants;
import store.constant.PromotionConstants;
import store.model.Inventory;
import store.model.Promotion;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Reader {

    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    public static Map<String, Promotion> readPromotions() {
        Map<String, Promotion> promotions = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PROMOTION_FILE_PATH))) {
            br.readLine();
            br.lines().forEach(line -> {
                String[] values = line.split(Constants.COMMA.getConstant());
                promotions.put(values[PromotionConstants.NAME.getConstant()], savePromotionInfo(values[PromotionConstants.NAME.getConstant()],
                        values[PromotionConstants.BUY_QUANTITY.getConstant()], values[PromotionConstants.GET_QUANTITY.getConstant()],
                        values[PromotionConstants.START_DATE.getConstant()], values[PromotionConstants.END_DATE.getConstant()]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return promotions;
    }

    private static Promotion savePromotionInfo(String name, String buyQuantity, String getQuantity, String startDate, String endDate){
        int buy = Integer.parseInt(buyQuantity);
        int get = Integer.parseInt(getQuantity);
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return new Promotion(name, buy, get, start, end);
    }

    public static List<Inventory> readInventory(Map<String, Promotion> promotions) { //products.md에서 읽기
        List<Inventory> inventories = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCT_FILE_PATH))) {
            br.lines()
                    .skip(ProductConstants.ATTRIBUTE.getConstant()) //첫 줄 건너뛰기
                    .forEach(line -> processLine(line, promotions, inventories));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inventories;
    }

    private static void processLine(String line, Map<String, Promotion> promotions, List<Inventory> inventories) {
        String[] values = line.split(Constants.COMMA.getConstant()); // ,를 기준으로 파싱
        Promotion promotion = getPromotion(values, promotions); //프로모션 제품인지 확인하고, 프로모션 정보 가져오기
        if (promotion == null && updateExistingInventory(values, inventories)) { //프로모션 기간이 끝난 제품이면
            return;  // 기존 Inventory가 업데이트된 경우, 새로 추가하지 않음
        }
        inventories.add(createInventory(values, promotion)); //재고 리스트에 없는 제품은 새로 추가
    }

    private static Promotion getPromotion(String[] values, Map<String, Promotion> promotions) { //프로모션 정보 가져오기
        if (isPromotion(values, promotions)) { //프로모션 제품이면
            return promotions.get(values[ProductConstants.PROMOTION.getConstant()]);
        }
        return null;
    }

    private static boolean isPromotion(String[] values, Map<String, Promotion> promotions){ //프로모션 제품인지 확인
        if(values.length > ProductConstants.SIZE.getConstant() && !values[ProductConstants.PROMOTION.getConstant()].equals(Constants.NULL.getConstant())){
            Promotion promotion = promotions.get(values[ProductConstants.PROMOTION.getConstant()]);
            return promotion.getStartDate().isBefore(DateTimes.now().toLocalDate()) &&
                    promotion.getEndDate().isAfter(DateTimes.now().toLocalDate()); //진행중인 프로모션이면 true 반환
        }
        return false;
    }

    private static boolean updateExistingInventory(String[] values, List<Inventory> inventories) {
        Inventory existingInventory = inventories.stream()
                .filter(inv -> inv.getName().equals(values[ProductConstants.NAME.getConstant()]) &&
                        inv.getPromotion().isEmpty())
                .findFirst()
                .orElse(null); //같은 이름을 가진 일반 상품이 있는지 확인
        if (existingInventory != null) { //있으면
            int newQuantity = existingInventory.getQuantity() + Integer.parseInt(values[ProductConstants.QUANTITY.getConstant()]); //재고 수량 갱신
            existingInventory.setQuantity(newQuantity);
            return true; //업데이트 함.
        }
        return false; //업데이트x
    }

    private static Inventory createInventory(String[] values, Promotion promotion) {
        return saveInventory(
                values[ProductConstants.NAME.getConstant()],
                values[ProductConstants.PRICE.getConstant()],
                values[ProductConstants.QUANTITY.getConstant()],
                Optional.ofNullable(promotion));
    }

    private static Inventory saveInventory(String name, String price, String quantity, Optional<Promotion> promotion){
        int priceNum = Integer.parseInt(price);
        int quantityNum = Integer.parseInt(quantity);
        return promotion.map(value -> new Inventory(name, priceNum, quantityNum, value)).orElseGet(() -> new Inventory(name, priceNum, quantityNum, null));
    }
}
