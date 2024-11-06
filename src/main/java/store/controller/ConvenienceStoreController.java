package store.controller;

import store.model.ConvenienceStore;
import store.model.Product;
import store.model.Promotion;
import store.util.Reader;
import store.view.OutputView;

import java.util.List;
import java.util.Map;

public class ConvenienceStoreController {

    private final OutputView outputView;
    private ConvenienceStore convenienceStore;


    public ConvenienceStoreController(){
        this.outputView = new OutputView();
    }

    public void printWelcomeMessage(){ //환영 인사 출력
        outputView.printWelcomeMessage();
    }

    public void printProductInfo(){ //상품 안내 출력
        loadProductInfo();
        outputView.printProductInfo(convenienceStore.getProducts());
    }

    public void loadProductInfo(){ //상품, 프로모션 정보 읽어오기
        Map<String, Promotion> promotions = Reader.readPromotions();
        List<Product> products = Reader.readProducts(promotions);
        convenienceStore = new ConvenienceStore(promotions, products);
    }
}
