package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.ShopService;
import core.basesyntax.strategy.OperationStrategyImpl;
import core.basesyntax.strategy.handler.BalanceOperation;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopServiceImplTest {
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        shopService = new ShopServiceImpl(new OperationStrategyImpl(
                Map.of(FruitTransaction.Operation.BALANCE, new BalanceOperation())));
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    void process_balanceTransactions_ok() {
        shopService.process(List.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "banana", 20),
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "apple", 100)));
        assertEquals(20, Storage.get("banana"));
        assertEquals(100, Storage.get("apple"));
    }

    @Test
    void process_emptyList_storageUnchanged() {
        shopService.process(List.of());
        assertTrue(Storage.getAll().isEmpty());
    }

    @Test
    void process_operationWithoutHandler_notOk() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, "banana", 10));
        assertThrows(RuntimeException.class, () -> shopService.process(transactions));
    }
}
