package core.basesyntax.strategy;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.strategy.handler.BalanceOperation;
import core.basesyntax.strategy.handler.PurchaseOperation;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationStrategyImplTest {
    private OperationHandler balanceHandler;
    private OperationHandler purchaseHandler;
    private OperationStrategy operationStrategy;

    @BeforeEach
    void setUp() {
        balanceHandler = new BalanceOperation();
        purchaseHandler = new PurchaseOperation();
        Map<FruitTransaction.Operation, OperationHandler> handlers = new HashMap<>();
        handlers.put(FruitTransaction.Operation.BALANCE, balanceHandler);
        handlers.put(FruitTransaction.Operation.PURCHASE, purchaseHandler);
        operationStrategy = new OperationStrategyImpl(handlers);
    }

    @Test
    void get_existingOperation_ok() {
        assertSame(balanceHandler, operationStrategy.get(FruitTransaction.Operation.BALANCE));
        assertSame(purchaseHandler, operationStrategy.get(FruitTransaction.Operation.PURCHASE));
    }

    @Test
    void get_operationWithoutHandler_notOk() {
        assertThrows(RuntimeException.class,
                () -> operationStrategy.get(FruitTransaction.Operation.RETURN));
    }

    @Test
    void get_nullOperation_notOk() {
        assertThrows(RuntimeException.class, () -> operationStrategy.get(null));
    }
}
