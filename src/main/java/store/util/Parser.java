package store.util;

import store.constant.ErrorMessages;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final String INPUT_REGEX = "^\\[([\\p{L}]+)-\\d+\\](\\s*,\\s*\\[([\\p{L}]+)-\\d+\\])*\\s*$";
    private static final String PARSING_REGEX = "\\[([\\p{L}]+)-(\\d+)]";

    private static final Integer PRODUCT_NAME = 1;
    private static final Integer PRODUCT_QUANTITY = 2;

    public static Map<String, Integer> parsingInput(String input){
        validate(input);
        Map<String, Integer> purchaseList = new HashMap<>();
        Pattern pattern = Pattern.compile(PARSING_REGEX);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String name = matcher.group(PRODUCT_NAME);
            int quantity = Integer.parseInt(matcher.group(PRODUCT_QUANTITY));
            purchaseList.put(name, quantity);
        }
        return purchaseList;
    }

    private static void validate(String input){
        if(input.isEmpty()) throw new IllegalArgumentException(ErrorMessages.EMPTY_INPUT.getMessage());
        Pattern pattern = Pattern.compile(INPUT_REGEX);
        if(!pattern.matcher(input).matches()) throw new IllegalArgumentException(ErrorMessages.NOT_RIGHT_FORM.getMessage());
    }
}