package core.basesyntax.strategy.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.strategy.OperationHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BalanceOperationTest {
    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new BalanceOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    void handle_newFruit_ok() {
        handler.handle(new FruitTransaction(FruitTransaction.Operation.BALANCE, "banana", 20));
        assertEquals(20, Storage.get("banana"));
    }

    @Test
    void handle_existingFruit_overwritesQuantity() {
        Storage.put("banana", 50);
        handler.handle(new FruitTransaction(FruitTransaction.Operation.BALANCE, "banana", 20));
        assertEquals(20, Storage.get("banana"));
    }

    @Test
    void handle_zeroQuantity_ok() {
        handler.handle(new FruitTransaction(FruitTransaction.Operation.BALANCE, "apple", 0));
        assertEquals(0, Storage.get("apple"));
    }
}
