package store;

import store.constant.Constants;
import store.controller.ConvenienceStoreController;

public class Application {
    public static void main(String[] args) {
        ConvenienceStoreController convenienceStoreController = new ConvenienceStoreController();
        firstExecute(convenienceStoreController);
        boolean check = true;
        if (convenienceStoreController.printAdditionalPurchase().equals(Constants.YES.getConstant())) check = false;
        while (!check) {
            executeStoreProcess(convenienceStoreController, false);
            if (convenienceStoreController.printAdditionalPurchase().equals(Constants.NO.getConstant())) {
                check = true;
            }
        }
    }

    private static void firstExecute(ConvenienceStoreController convenienceStoreController) {
        executeStoreProcess(convenienceStoreController, true);
    }

    private static void executeStoreProcess(ConvenienceStoreController convenienceStoreController, boolean isFirstExecution) {
        convenienceStoreController.printWelcomeMessage(); // 환영 메시지 출력
        convenienceStoreController.printProductInfo(isFirstExecution); // 첫 실행 시 true, 그 이후엔 false 넘기기
        convenienceStoreController.printInputMessage();
        convenienceStoreController.getProductNameAndQuantity(); // 상품명과 수량 입력 받기
        convenienceStoreController.purchaseProduct(); // 상품 구매 처리
        convenienceStoreController.calculateMemberShip(); // 멤버십 할인 계산
        convenienceStoreController.printReceipt(); // 영수증 출력
    }
}
