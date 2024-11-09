package store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ErrorMessages;
import store.util.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    @DisplayName("빈 문자열 입력한 경우")
    void validate_emptyInput(){
        String input = "";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validate(input);
        });

        assertEquals(ErrorMessages.EMPTY_INPUT.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Y이나 N가 아닌 입력을 한 경우")
    void validate_wrongInput(){
        String input = "1234";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validate(input);
        });

        assertEquals(ErrorMessages.EMPTY_INPUT.getMessage(), exception.getMessage());
    }
}
