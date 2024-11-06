package store;

import store.controller.ConvenienceStoreController;

public class Application {
    public static void main(String[] args) {
        ConvenienceStoreController convenienceStoreController = new ConvenienceStoreController();

        convenienceStoreController.printWelcomeMessage(); //환영 인사 출력

    }
}
