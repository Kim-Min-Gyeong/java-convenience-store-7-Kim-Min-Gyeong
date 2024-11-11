package store.util;

import store.constant.ErrorMessages;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final String INPUT_REGEX = "^\\[([\\p{L}]+)-\\d+\\](\\s*,\\s*\\[([\\p{L}]+)-\\d+\\])*\\s*$";
    private static final String PARSING_REGEX = "\\[([\\p{L}]+)-(\\d+)]";

    private static final Integer PRODUCT_NAME = 1;
    private static final Integer PRODUCT_QUANTITY = 2;

    public static Map<String, Integer> parsingInput(String input) {
        validate(input);
        return parseProducts(input);
    }

    private static void validate(String input) {
        if (input.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.EMPTY_INPUT.getMessage());
        if (!Pattern.compile(INPUT_REGEX).matcher(input).matches())
            throw new IllegalArgumentException(ErrorMessages.NOT_RIGHT_FORM.getMessage());
    }

    private static Map<String, Integer> parseProducts(String input) {
        Map<String, Integer> purchaseList = new LinkedHashMap<>();
        Matcher matcher = Pattern.compile(PARSING_REGEX).matcher(input);
        while (matcher.find()) {
            addProduct(purchaseList, matcher);
        }
        return purchaseList;
    }

    private static void addProduct(Map<String, Integer> purchaseList, Matcher matcher) {
        String name = matcher.group(PRODUCT_NAME);
        int quantity = parseQuantity(matcher.group(PRODUCT_QUANTITY));
        purchaseList.put(name, purchaseList.getOrDefault(name, 0) + quantity);
    }

    private static int parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessages.NOT_RIGHT_FORM.getMessage());
        }
    }
}
