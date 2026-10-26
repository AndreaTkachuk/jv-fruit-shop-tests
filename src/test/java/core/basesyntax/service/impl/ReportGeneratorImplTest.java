package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.db.Storage;
import core.basesyntax.service.ReportGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private static final String HEADER = "fruit,quantity";
    private ReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGeneratorImpl();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    void getReport_emptyStorage_onlyHeader() {
        assertEquals(HEADER, reportGenerator.getReport());
    }

    @Test
    void getReport_oneFruit_ok() {
        Storage.put("banana", 152);
        String expected = HEADER + System.lineSeparator() + "banana,152";
        assertEquals(expected, reportGenerator.getReport());
    }

    @Test
    void getReport_severalFruits_ok() {
        Storage.put("banana", 152);
        Storage.put("apple", 90);
        String actual = reportGenerator.getReport();
        String[] lines = actual.split(System.lineSeparator());
        assertEquals(3, lines.length);
        assertEquals(HEADER, lines[0]);
        assertTrue(actual.contains(System.lineSeparator() + "banana,152"));
        assertTrue(actual.contains(System.lineSeparator() + "apple,90"));
    }

    @Test
    void getReport_zeroQuantity_ok() {
        Storage.put("apple", 0);
        String expected = HEADER + System.lineSeparator() + "apple,0";
        assertEquals(expected, reportGenerator.getReport());
    }
}
