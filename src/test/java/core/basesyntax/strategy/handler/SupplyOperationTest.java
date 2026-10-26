package core.basesyntax.strategy.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.strategy.OperationHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplyOperationTest {
    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SupplyOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    void handle_existingFruit_addsQuantity() {
        Storage.put("banana", 20);
        handler.handle(new FruitTransaction(FruitTransaction.Operation.SUPPLY, "banana", 100));
        assertEquals(120, Storage.get("banana"));
    }

    @Test
    void handle_newFruit_ok() {
        handler.handle(new FruitTransaction(FruitTransaction.Operation.SUPPLY, "apple", 30));
        assertEquals(30, Storage.get("apple"));
    }
}
