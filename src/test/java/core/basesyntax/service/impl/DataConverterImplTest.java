package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.DataConverter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataConverterImplTest {
    private static final String HEADER = "type,fruit,quantity";
    private DataConverter dataConverter;

    @BeforeEach
    void setUp() {
        dataConverter = new DataConverterImpl();
    }

    @Test
    void convertToTransaction_validLines_ok() {
        List<FruitTransaction> actual = dataConverter.convertToTransaction(
                List.of(HEADER, "b,banana,20", "p,apple,5"));
        assertEquals(2, actual.size());
        assertTransaction(FruitTransaction.Operation.BALANCE, "banana", 20, actual.get(0));
        assertTransaction(FruitTransaction.Operation.PURCHASE, "apple", 5, actual.get(1));
    }

    @Test
    void convertToTransaction_linesWithSpaces_ok() {
        List<FruitTransaction> actual = dataConverter.convertToTransaction(
                List.of(HEADER, "  s , banana , 7  "));
        assertTransaction(FruitTransaction.Operation.SUPPLY, "banana", 7, actual.get(0));
    }

    @Test
    void convertToTransaction_blankLines_skipped() {
        List<FruitTransaction> actual = dataConverter.convertToTransaction(
                List.of(HEADER, "", "r,apple,3", "   "));
        assertEquals(1, actual.size());
        assertTransaction(FruitTransaction.Operation.RETURN, "apple", 3, actual.get(0));
    }

    @Test
    void convertToTransaction_onlyHeader_ok() {
        assertTrue(dataConverter.convertToTransaction(List.of(HEADER)).isEmpty());
    }

    @Test
    void convertToTransaction_emptyList_ok() {
        assertTrue(dataConverter.convertToTransaction(List.of()).isEmpty());
    }

    @Test
    void convertToTransaction_zeroQuantity_ok() {
        List<FruitTransaction> actual = dataConverter.convertToTransaction(
                List.of(HEADER, "b,banana,0"));
        assertEquals(0, actual.get(0).getQuantity());
    }

    @Test
    void convertToTransaction_tooFewParts_notOk() {
        List<String> lines = List.of(HEADER, "b,banana");
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(lines));
    }

    @Test
    void convertToTransaction_tooManyParts_notOk() {
        List<String> lines = List.of(HEADER, "b,banana,20,1");
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(lines));
    }

    @Test
    void convertToTransaction_unknownOperation_notOk() {
        List<String> lines = List.of(HEADER, "x,banana,20");
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(lines));
    }

    @Test
    void convertToTransaction_nonNumericQuantity_notOk() {
        List<String> lines = List.of(HEADER, "b,banana,ten");
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(lines));
    }

    @Test
    void convertToTransaction_negativeQuantity_notOk() {
        List<String> lines = List.of(HEADER, "b,banana,-5");
        assertThrows(IllegalArgumentException.class,
                () -> dataConverter.convertToTransaction(lines));
    }

    private void assertTransaction(FruitTransaction.Operation operation, String fruit,
                                   int quantity, FruitTransaction actual) {
        assertEquals(operation, actual.getOperation());
        assertEquals(fruit, actual.getFruit());
        assertEquals(quantity, actual.getQuantity());
    }
}
