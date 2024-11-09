package store.constant;

public enum Constants {
    COMMA(","),
    NULL("null"),
    YES("Y"),
    NO("N");

    private final String constant;

    Constants(String constant) {
        this.constant = constant;
    }

    public String getConstant(){
        return this.constant;
    }
}
