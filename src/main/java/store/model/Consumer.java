package store.model;

import java.util.Map;

public class Consumer {
    private final Map<String, Integer> purchaseList;

    public Consumer(Map<String, Integer> purchaseList) {
        this.purchaseList = purchaseList;
    }
}
