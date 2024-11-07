package store.constant;

public enum GuideMessages {
    WELCOME_MESSAGE("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다."),

    PRODUCT_NAME_QUANTITY_MESSAGE("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    NEXT_LINE("\n");

    private final String message;

    GuideMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
