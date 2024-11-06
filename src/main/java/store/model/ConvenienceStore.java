package store.model;

import java.util.List;
import java.util.Map;

public class ConvenienceStore {

    private final Map<String, Promotion> promotions;

    private final List<Product> products;

    public ConvenienceStore(Map<String, Promotion> promotions, List<Product> products){
        this.promotions = promotions;
        this.products = products;
    }

    public Map<String, Promotion> getPromotions() {
        return promotions;
    }

    public List<Product> getProducts() {
        return products;
    }
}
