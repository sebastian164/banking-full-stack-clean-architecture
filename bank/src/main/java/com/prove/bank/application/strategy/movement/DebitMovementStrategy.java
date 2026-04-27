package com.prove.bank.application.strategy.movement;

import com.prove.bank.domain.exception.BusinessException;
import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.model.MovementModel;
import com.prove.bank.domain.spi.MovementRepositoryPort;
import com.prove.bank.util.constants.messages.MovementMessages;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebitMovementStrategy implements MovementTypeStrategy {
    private final MovementRepositoryPort movementRepository;

    @Value("${bank.daily-withdrawal-limit:1000}")
    private BigDecimal dailyWithdrawalLimit;

    @Override
    public boolean supports(String type) {
        return "DEBITO".equalsIgnoreCase(type) || "RETIRO".equalsIgnoreCase(type);
    }

    @Override
    public BigDecimal calculateValue(AccountModel account, MovementModel movement) {
        BigDecimal debitAmount = movement.getValue().abs();
        validateBalance(account, debitAmount);
        validateDailyLimit(account, movement.getDate(), debitAmount);
        return debitAmount.negate();
    }

    private void validateBalance(AccountModel account, BigDecimal debitAmount) {
        if (account.getCurrentBalance().compareTo(BigDecimal.ZERO) <= 0
                || account.getCurrentBalance().subtract(debitAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(MovementMessages.INSUFFICIENT_BALANCE);
        }
    }

    private void validateDailyLimit(AccountModel account, LocalDate date, BigDecimal debitAmount) {
        BigDecimal used = movementRepository.sumDebitsByAccountAndDate(
                account.getNumber(), date == null ? LocalDate.now() : date);
        if (used.add(debitAmount).compareTo(dailyWithdrawalLimit) > 0) {
            throw new BusinessException(MovementMessages.DAILY_LIMIT_EXCEEDED);
        }
    }
}
