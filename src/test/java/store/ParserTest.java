package store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ErrorMessages;
import store.util.Parser;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    @DisplayName("올바르게 입력한 경우")
    void parsingInput_ValidInput_ShouldReturnCorrectMap() {
        String input = "[콜라-2],[사이다-5], [물-3]";

        Map<String, Integer> result = Parser.parsingInput(input);

        assertEquals(3, result.size());
        assertEquals(2, result.get("콜라"));
        assertEquals(5, result.get("사이다"));
        assertEquals(3, result.get("물"));
    }

    @Test
    @DisplayName("빈 문자열 입력한 경우")
    void parsingInput_EmptyInput_ShouldThrowException() {
        String input = "";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Parser.parsingInput(input);
        });
        assertEquals(ErrorMessages.EMPTY_INPUT.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("잘못된 형식으로 입력한 경우")
    void parsingInput_InvalidFormat_ShouldThrowException() {
        String input = "[콜라-2], 사이다-5, [물-3]";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Parser.parsingInput(input);
        });

        assertEquals(ErrorMessages.NOT_RIGHT_FORM.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("수량 입력을 정수로 하지 않은 경우")
    void parsingInput_NonNumericQuantity_ShouldThrowException() {
        String input = "[콜라-2], [사이다-다섯]";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Parser.parsingInput(input);
        });
        assertTrue(exception.getMessage().contains(ErrorMessages.NOT_RIGHT_FORM.getMessage()));
    }

    @Test
    @DisplayName("중복되는 상품을 입력한 경우")
    void parsingInput_DuplicateProductNames_ShouldOverrideWithLastQuantity() {
        String input = "[콜라-2], [사이다-4], [콜라-5]";
        Map<String, Integer> result = Parser.parsingInput(input);
        assertEquals(2, result.size());
        assertEquals(7, result.get("콜라"));
        assertEquals(4, result.get("사이다"));
    }
}
