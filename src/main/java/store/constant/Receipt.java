package store.constant;

public enum Receipt {
    HEADER("==============W 편의점================"),
    PRODUCT_TITLE("상품명\t\t\t\t수량\t\t금액\n"),
    PRODUCT("%s\t\t\t\t%d\t\t%s\n"),
    GIFT_TITLE("=============증\t\t정==============="),
    GIFT("%s\t\t\t\t\t%d\n"),
    SEPARATOR("===================================="),
    TOTAL_AMOUNT("총구매액\t\t\t\t%d\t\t%s\n"),
    PROMOTION_DISCOUNT("행사할인\t\t\t\t\t\t-%s\n"),
    MEMBERSHIP_DISCOUNT("멤버십할인\t\t\t\t\t-%s\n"),
    FINAL_PAYMENT("내실돈\t\t\t\t\t\t%s\n");

    private final String text;

    Receipt(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
