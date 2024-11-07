package store.controller;

import store.model.Consumer;
import store.model.ConvenienceStore;
import store.model.Inventory;
import store.model.Promotion;
import store.util.Parser;
import store.util.Reader;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.Map;

public class ConvenienceStoreController {

    private final OutputView outputView;
    private final InputView inputView;
    private ConvenienceStore convenienceStore;

    private Consumer consumer;


    public ConvenienceStoreController(){
        this.outputView = new OutputView();
        this.inputView = new InputView();
    }

    public void printWelcomeMessage(){ //환영 인사 출력
        outputView.printWelcomeMessage();
    }

    public void printProductInfo(){ //상품 안내 출력
        loadProductInfo(); //(처음에만 실행, 두번째부터는 그냥 사용하면 됨.- 수정 필요)
        outputView.printProductInfo(convenienceStore.getInventories());
    }

    public void loadProductInfo(){ //상품, 프로모션 정보 읽어오기
        Map<String, Promotion> promotions = Reader.readPromotions();
        List<Inventory> inventories = Reader.readInventory(promotions); //재고 읽어오기
        convenienceStore = new ConvenienceStore(promotions, inventories);
        convenienceStore.assignProductAndPromotionProduct();

    }

    public void printInputMessage(){ //구매 상품명 및 수량 입력 메시지 출력
        outputView.printInputMessage();
    }

    public void getProductNameAndQuantity(){ //구매 상품명 및 수량 입력
        boolean check = false;
        while(!check) {
            try {
                String input = inputView.getInput();
                Map<String, Integer> purchaseList = Parser.parsingInput(input);
                convenienceStore.checkPurchasePossible(purchaseList);
                consumer = new Consumer(purchaseList);
                check = true;
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                System.out.println();
            }
        }
//        purchaseList.forEach((name, quantity) ->
//                System.out.println("Product: " + name + ", Quantity: " + quantity)
//        );
    }
}
