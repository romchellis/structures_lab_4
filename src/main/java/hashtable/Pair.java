package hashtable;

import static hashtable.Contracts.requireNotNull;

import java.util.Objects;

public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = requireNotNull(key);
        this.value = requireNotNull(value);
    }

    public static <K,V> Pair<K,V> pair(K key, V value) {
        return new Pair<>(key, value);
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }


    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!getKey().equals(pair.getKey())) return false;
        return getValue().equals(pair.getValue());
    }
}
