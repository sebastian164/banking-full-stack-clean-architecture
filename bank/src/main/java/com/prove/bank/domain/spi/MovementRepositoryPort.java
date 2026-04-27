package com.prove.bank.domain.spi;

import com.prove.bank.domain.model.MovementModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovementRepositoryPort {
    MovementModel save(MovementModel movement);
    List<MovementModel> findAll();
    Optional<MovementModel> findById(Long id);
    List<MovementModel> findByAccountNumberAndDateBetween(String accountNumber, LocalDate startDate, LocalDate endDate);
    BigDecimal sumDebitsByAccountAndDate(String accountNumber, LocalDate date);
    boolean existsByAccountId(Long accountId);
    void deleteById(Long id);
}
