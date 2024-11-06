package store;

import store.controller.ConvenienceStoreController;

public class Application {
    public static void main(String[] args) {
        ConvenienceStoreController convenienceStoreController = new ConvenienceStoreController();

        convenienceStoreController.printWelcomeMessage(); //환영 인사 출력
        convenienceStoreController.printProductInfo(); //상품 안내
        convenienceStoreController.printInputMessage(); //상품명, 수량 입력 메시지 출력

    }
}
