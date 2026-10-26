package core.basesyntax.strategy.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.strategy.OperationHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReturnOperationTest {
    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ReturnOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    void handle_existingFruit_addsQuantity() {
        Storage.put("apple", 90);
        handler.handle(new FruitTransaction(FruitTransaction.Operation.RETURN, "apple", 10));
        assertEquals(100, Storage.get("apple"));
    }

    @Test
    void handle_newFruit_ok() {
        handler.handle(new FruitTransaction(FruitTransaction.Operation.RETURN, "apple", 10));
        assertEquals(10, Storage.get("apple"));
    }
}
