package store;

import store.controller.ConvenienceStoreController;

public class Application {
    public static void main(String[] args) {
        ConvenienceStoreController convenienceStoreController = new ConvenienceStoreController();

        convenienceStoreController.printWelcomeMessage(); //환영 인사 출력
        convenienceStoreController.printProductInfo(); //상품 안내
        convenienceStoreController.printInputMessage(); //상품명, 수량 입력 메시지 출력
        convenienceStoreController.getProductNameAndQuantity(); //상품명, 수량 입력
        convenienceStoreController.purchaseProduct(); //상품 구매
        convenienceStoreController.calculateMemberShip(); //멤버십 할인
        convenienceStoreController.printReceipt(); //영수증 출력

    }
}
