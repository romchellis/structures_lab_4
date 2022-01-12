package hashtable;

import java.util.regex.Pattern;

public class Contracts {

    public static <T> T requireNotNull(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value is null !");
        }
        return value;
    }

    public static String requireNotEmpty(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(text + " is null or empty!");
        }
        return text;
    }

    public static String requirePattern(String pattern, String text) {
        requireNotEmpty(text);
        requireNotEmpty(pattern);

        boolean matches = Pattern.matches(pattern, text);
        if (!matches) {
            throw new IllegalArgumentException("text: " + text + " is not matching pattern: " + pattern);
        }
        return text;
    }
}
