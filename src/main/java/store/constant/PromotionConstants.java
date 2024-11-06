package store.constant;

public enum PromotionConstants {
    NAME(0),
    BUY_QUANTITY(1),
    GET_QUANTITY(2),

    START_DATE(3),
    END_DATE(4);

    private final Integer constant;

    PromotionConstants(Integer constant) {
        this.constant = constant;
    }

    public Integer getConstant(){return this.constant;}
}
