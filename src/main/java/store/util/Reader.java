package store.util;
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
import java.util.stream.Collectors;

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

    public static List<Inventory> readInventory(Map<String, Promotion> promotions) {
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCT_FILE_PATH))) {
            return br.lines().skip(ProductConstants.ATTRIBUTE.getConstant()).map(line -> {
                String[] values = line.split(Constants.COMMA.getConstant());
                Promotion promotion = null;
                if(!isProduct(values)) promotion = promotions.get(values[ProductConstants.PROMOTION.getConstant()]);
                return saveInventory(values[ProductConstants.NAME.getConstant()],
                        values[ProductConstants.PRICE.getConstant()], values[ProductConstants.QUANTITY.getConstant()], Optional.ofNullable(promotion));
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static boolean isProduct(String[] values){
        return values.length <= ProductConstants.SIZE.getConstant() || values[ProductConstants.PROMOTION.getConstant()].equals(Constants.NULL.getConstant());
    }

    private static Inventory saveInventory(String name, String price, String quantity, Optional<Promotion> promotion){
        int priceNum = Integer.parseInt(price);
        int quantityNum = Integer.parseInt(quantity);
        return promotion.map(value -> new Inventory(name, priceNum, quantityNum, value)).orElseGet(() -> new Inventory(name, priceNum, quantityNum, null));
    }
}
