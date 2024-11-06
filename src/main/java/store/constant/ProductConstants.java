package store.constant;

public enum ProductConstants {
    ATTRIBUTE(1),
    NAME(0),
    PRICE(1),
    QUANTITY(2),
    PROMOTION(3),
    SIZE(3);

    private final Integer constant;

    ProductConstants(Integer constant) {
        this.constant = constant;
    }

    public Integer getConstant(){return this.constant;}
}
