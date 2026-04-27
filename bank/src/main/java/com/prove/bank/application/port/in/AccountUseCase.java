package com.prove.bank.application.port.in;

import com.prove.bank.application.dto.AccountDto;
import java.util.List;

public interface AccountUseCase {
    AccountDto create(AccountDto account);
    List<AccountDto> findAll();
    AccountDto findById(Long id);
    AccountDto update(Long id, AccountDto account);
    AccountDto patch(Long id, AccountDto account);
    void delete(Long id);
}
