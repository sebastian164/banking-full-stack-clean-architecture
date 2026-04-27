package com.prove.bank.application.strategy.movement;

import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.model.MovementModel;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class CreditMovementStrategy implements MovementTypeStrategy {
    @Override
    public boolean supports(String type) {
        return "CREDITO".equalsIgnoreCase(type) || "DEPOSITO".equalsIgnoreCase(type);
    }

    @Override
    public BigDecimal calculateValue(AccountModel account, MovementModel movement) {
        return movement.getValue().abs();
    }
}
