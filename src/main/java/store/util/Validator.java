package store.util;

import store.constant.Constants;
import store.constant.ErrorMessages;

public class Validator {
    public static void validate(String input){
        if (input.isEmpty()) throw new IllegalArgumentException(ErrorMessages.EMPTY_INPUT.getMessage());
        if (!input.equals(Constants.YES.getConstant()) && !input.equals(Constants.NO.getConstant())) throw new IllegalArgumentException(ErrorMessages.EMPTY_INPUT.getMessage());
    }
}
