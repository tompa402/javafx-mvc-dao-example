package hr.java.vjezbe.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public final class InputValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputValidator.class);

    public InputValidator() {
        // disable instantiation
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static boolean isBigDecimal(String input) {
        try {
            new BigDecimal(input);
        } catch (Exception ex) {
            LOGGER.warn("Failed to validate input: " + ex.getMessage() + "\nGiven input was: " + input);
            return false;
        }
        return true;
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (Exception ex) {
            LOGGER.warn("Failed to validate input: " + ex.getMessage() + "\nGiven input was: " + input);
            return false;
        }
        return true;
    }
}
