package store.controller;

import store.model.Consumer;
import store.model.ConvenienceStore;
import store.model.Inventory;
import store.model.Promotion;
import store.util.Parser;
import store.util.Reader;
import store.util.Validator;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.Map;

public class ConvenienceStoreController {

    private final OutputView outputView;
    private final InputView inputView;
    private ConvenienceStore convenienceStore;
    private Consumer consumer;

    public ConvenienceStoreController() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
    }

    public void printWelcomeMessage() { //환영 인사 출력
        outputView.printWelcomeMessage();
    }

    public void printProductInfo() { //상품 안내 출력
        loadProductInfo();
        outputView.printProductInfo(convenienceStore.getInventories());
    }

    private void loadProductInfo() { //상품, 프로모션 정보 읽어오기
        Map<String, Promotion> promotions = Reader.readPromotions();
        List<Inventory> inventories = Reader.readInventory(promotions);
        convenienceStore = new ConvenienceStore(promotions, inventories);
        convenienceStore.assignProductAndPromotionProduct();
    }

    public void printInputMessage() { //구매 상품명 및 수량 입력 메시지 출력
        outputView.printInputMessage();
    }

    public void getProductNameAndQuantity() { //구매 상품명 및 수량 입력
        while (true) {
            String input = inputView.getInput();
            if (tryProcessInput(input)) break;
        }
    }

    private boolean tryProcessInput(String input) { //입력 처리 시도
        try {
            Map<String, Integer> wishList = Parser.parsingInput(input);
            convenienceStore.checkPurchasePossible(wishList);
            consumer = new Consumer(wishList);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println();
            return false;
        }
    }

    public void purchaseProduct() { //상품 구매
        Map<String, Integer> wishList = consumer.getWishList();
        for (Map.Entry<String, Integer> entry : wishList.entrySet()) {
            processProductPurchase(entry.getKey(), entry.getValue());
        }
    }

    private void processProductPurchase(String name, Integer qty) { //상품 구매 처리
        if (convenienceStore.checkPromotionProductPossible(name, qty)) { //구매하려는 제품이 프로모션 재고가 있는지
            handlePromotionProduct(name, qty);
            return;
        }
        handleNonPromotionProduct(name, qty); //일반 제품 구매
    }

    private void handlePromotionProduct(String name, Integer qty) { //프로모션 상품 처리
        if (convenienceStore.checkBringOneMore(name, qty)) { //증정 상품 1개 부족하게 가져왔다
            String answer = printOneMore(name);
            convenienceStore.purchasePromotionProduct(answer, name, qty, consumer);
            return;
        }
        convenienceStore.purchasePromotionProduct(name, qty, consumer); //맞게 가져옴.
    }

    private void handleNonPromotionProduct(String name, Integer qty) { //일반 상품 처리
        Integer remain = convenienceStore.getProductQtyWithoutPromotion(name, qty);
        if (remain.equals(-1)) {
            convenienceStore.purchaseProducts(name, qty, consumer);
        } else {
            String answer = printWithoutPromotion(name, remain);
            convenienceStore.promotionProductWithProduct(answer, name, qty, consumer);
        }
    }

    private String printOneMore(String name){ //한개 더 가져올지 말지
        String input = "";
        while(true){
            outputView.printOneMoreMessage(name);
            input = inputView.getInput();
            if(getValidatedInput(input)) break;
        }
        return input;
    }

    private String printWithoutPromotion(String name, Integer quantity){ //프로모션
        String input = "";
        while(true){
            outputView.printWithoutPromotionMessage(name, quantity);
            input = inputView.getInput();
            if(getValidatedInput(input)) break;
        }
        return input;
    }

    private boolean getValidatedInput(String input){
        try {
            Validator.validate(input);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void calculateMemberShip(){
        String answer = printMemberShipMessage();
        convenienceStore.calculateMemberShipDiscount(consumer, answer);
    }

    private String printMemberShipMessage(){
        String input = "";
        while(true){
            outputView.printMemberShipMessage();
            input = inputView.getInput();
            if(getValidatedInput(input)) break;
        }
        return input;
    }

    public void printReceipt(){
        convenienceStore.calculateAmountToPay(consumer);
        outputView.printReceipt(consumer);
    }
}
