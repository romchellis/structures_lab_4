package hashtable;

import static hashtable.Contracts.requireNotNull;
import static hashtable.Pair.pair;
import static java.util.Arrays.stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class QuadraticProbingHashTable implements HashTable<Integer> {

    static final String FILE_SAVE_LOCATION = "C:\\reports\\";
    static final String FILE_NAME = "UserReport.xlsx";

    private int segmentCount;
    private Pair<Key, Integer>[] segments;

    private int size = 0;

    public QuadraticProbingHashTable() {
        segmentCount = 2000;
        this.segments = (Pair<Key, Integer>[]) Array.newInstance(Pair.class, segmentCount);
    }

    QuadraticProbingHashTable(Pair<Key, Integer>[] segments) {
        this.segments = segments;
        this.segmentCount = segments.length;
    }

    @Override
    public void put(Key key, Integer value) {
        requireNotNull(value);
        requireNotNull(key);

        if (size == segmentCount) {
            grow();
        }

        int tmp = key.hashCode() % segmentCount;
        int i = tmp, h = 1;
        do {
            if (segments[i] == null || segments[i].getKey().isRemoved()) {
                segments[i] = pair(key, value);
                size++;
                return;
            }
            if (segments[i].getKey().equals(key)) {
                segments[i] = pair(key, value);
                return;
            }
            i = (i + h * h++) % segmentCount;
        } while (i != tmp);
    }

    private void grow() {
        segmentCount *= 2;
        var extendedSegments = (Pair<Key, Integer>[]) Array.newInstance(Pair.class, segmentCount);
        var tmpArray = segments;
        segments = extendedSegments;
        size = 0;
        stream(tmpArray).forEach(it -> put(it.getKey(), it.getValue()));
        System.out.println(size);
    }

    @Override
    public void forEach(Consumer<Pair<Key, Integer>> consumer) {
        stream(segments).filter(Objects::nonNull).forEach(consumer);
    }

    @Override
    public Integer get(Key key) {
        requireNotNull(key);

        int index = lookup(key);
        return index > -1
                ? segments[index].getValue()
                : null;
    }

    @Override
    public void intoFile() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");
        createHeader(sheet);
        AtomicInteger rowCount = new AtomicInteger();
        stream(segments).filter(Objects::nonNull).forEach(segment -> {
            Row row = sheet.createRow(rowCount.incrementAndGet());
            Cell keyCell = row.createCell(0);
            keyCell.setCellValue(segment.getKey().getValue());
            Cell valueCell = row.createCell(1);
            valueCell.setCellValue(segment.getValue());
        });
        try (var outputStream = new FileOutputStream(FILE_SAVE_LOCATION + FILE_NAME)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean remove(Key key) {
        requireNotNull(key);

        int index = lookup(key);
        if (index > -1) {
            segments[index].getKey().setRemoved();
            size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Key key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    private int lookup(Key searchingKey) {
        int i = searchingKey.hashCode() % segmentCount;
        int h = 1;
        while (segments[i] != null) {
            var foundKey = segments[i].getKey();
            if (foundKey.equals(searchingKey)) {
                return foundKey.isRemoved() ? -1 : i;
            }
            i = (i + h * h++) % segmentCount;
        }
        return -1;
    }

    private void createHeader(XSSFSheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Key");
        headerRow.createCell(1).setCellValue("Value");
    }
}
