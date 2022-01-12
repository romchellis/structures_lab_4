package hashtable;

import static hashtable.Key.key;
import static hashtable.Pair.pair;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class QuadraticProbingHashTableTest {

    private HashTable<Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new QuadraticProbingHashTable();
    }

    @Test
    void should_contain_only_two_elements() {
        hashTable.put(key("1AAAA6"), 1);
        hashTable.put(key("1AAAA6"), 2);
        hashTable.put(key("1ABCD2"), 3);
        hashTable.put(key("5ABCB3"), 3);

        assertThat(hashTable.size()).isEqualTo(3);
    }

    @Test
    void get_should_return_expected_elements() {
        var objects = (Pair<Key, Integer>[]) Stream.of(
                pair(key("1AAAA6"), 1),
                pair(key("1BBBB1"), 3)
        ).toArray(Pair[]::new);
        hashTable = new QuadraticProbingHashTable(objects);

        assertThat(hashTable.get(key("1AAAA6"))).isEqualTo(1);
        assertThat(hashTable.get(key("1BBBB1"))).isEqualTo(3);
    }

    @Test
    void should_remove_element_if_exists_in_table() {
        var objects = (Pair<Key, Integer>[]) Stream.of(
                pair(key("1BBBB1"), 3),
                pair(key("1AAAA6"), 1)
        ).toArray(Pair[]::new);
        hashTable = new QuadraticProbingHashTable(objects);

        boolean firstRemoved = hashTable.remove(key("1AAAA6"));
        boolean secondRemoved = hashTable.remove(key("1CCCC1"));

        assertTrue(firstRemoved);
        assertFalse(secondRemoved);
        assertThat(hashTable.contains(key("1AAAA6"))).isFalse();
        assertThat(hashTable.contains(key("1BBBB1"))).isTrue();
    }

    @Test
    void load_into_file_test() {
        hashTable = new QuadraticProbingHashTable();
        for (int i = 0; i < 10; i++) {
            var number = i % 9;
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            Random random = new Random();
            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(4)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            var string = generatedString.toUpperCase();
            hashTable.put(key(number + string + number), i);
        }
        hashTable.intoFile();
    }
}