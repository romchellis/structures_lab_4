package hashtable;


import static hashtable.Contracts.requirePattern;
import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Формат - цББББц
 * ц - цифра,Б - большие буквы ascii
 * Пример - 1AFBV5
 */
public class Key {
    static final String pattern = "(\\d[A-Z]{4}\\d)";

    public String getValue() {
        return value;
    }

    private final String value;

    public Key(String value) {
        requireNonNull(value);
        requirePattern(pattern, value);
        this.value = value;
    }

    public static Key key(String value) {
        return new Key(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;
        Key key = (Key) o;
        return Objects.equals(value, key.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
