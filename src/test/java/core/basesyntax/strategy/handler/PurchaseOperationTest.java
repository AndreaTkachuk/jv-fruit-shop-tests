package core.basesyntax.strategy.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.strategy.OperationHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseOperationTest {
    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new PurchaseOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    void handle_enoughFruit_subtractsQuantity() {
        Storage.put("banana", 120);
        handler.handle(new FruitTransaction(FruitTransaction.Operation.PURCHASE, "banana", 13));
        assertEquals(107, Storage.get("banana"));
    }

    @Test
    void handle_allFruit_leavesZero() {
        Storage.put("banana", 13);
        handler.handle(new FruitTransaction(FruitTransaction.Operation.PURCHASE, "banana", 13));
        assertEquals(0, Storage.get("banana"));
    }

    @Test
    void handle_notEnoughFruit_notOk() {
        Storage.put("banana", 5);
        FruitTransaction transaction =
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "banana", 13);
        assertThrows(RuntimeException.class, () -> handler.handle(transaction));
        assertEquals(5, Storage.get("banana"));
    }

    @Test
    void handle_missingFruit_notOk() {
        FruitTransaction transaction =
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "banana", 1);
        assertThrows(RuntimeException.class, () -> handler.handle(transaction));
    }
}
