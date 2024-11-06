package store.constant;

public enum GuideMessage {
    WELCOME_MESSAGE("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다."),
    NEXT_LINE("\n");

    private final String message;

    GuideMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
