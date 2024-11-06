package store.constants;

public enum GuideMessage {
    WELCOME_MESSAGE("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.");

    private final String message;

    GuideMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
