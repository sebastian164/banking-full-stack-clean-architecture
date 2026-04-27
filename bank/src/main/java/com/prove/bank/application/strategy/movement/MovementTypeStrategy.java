package com.prove.bank.application.strategy.movement;

import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.model.MovementModel;
import java.math.BigDecimal;

public interface MovementTypeStrategy {
    boolean supports(String type);
    BigDecimal calculateValue(AccountModel account, MovementModel movement);
}
