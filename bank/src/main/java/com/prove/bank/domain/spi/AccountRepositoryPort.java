package com.prove.bank.domain.spi;

import com.prove.bank.domain.model.AccountModel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {
    AccountModel save(AccountModel account);
    List<AccountModel> findAll();
    List<AccountModel> findByClientId(Long clientId);
    Optional<AccountModel> findById(Long id);
    Optional<AccountModel> findByNumber(String number);
    boolean existsByNumber(String number);
    boolean existsByClientId(Long clientId);
    void updateCurrentBalance(String number, BigDecimal currentBalance);
    void deleteById(Long id);
}
