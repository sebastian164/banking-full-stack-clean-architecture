package com.prove.bank.application.service;

import com.prove.bank.application.dto.AccountDto;
import com.prove.bank.application.mapper.ApplicationDtoMapper;
import com.prove.bank.application.port.in.AccountUseCase;
import com.prove.bank.domain.exception.BusinessException;
import com.prove.bank.domain.exception.NotFoundException;
import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.spi.AccountRepositoryPort;
import com.prove.bank.domain.spi.ClientRepositoryPort;
import com.prove.bank.domain.spi.MovementRepositoryPort;
import com.prove.bank.util.constants.messages.AccountMessages;
import com.prove.bank.util.constants.messages.ClientMessages;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {
    private final AccountRepositoryPort repository;
    private final ClientRepositoryPort clientRepository;
    private final MovementRepositoryPort movementRepository;
    private final ApplicationDtoMapper mapper;

    public AccountDto create(AccountDto accountDto) {
        AccountModel account = mapper.toModel(accountDto);
        clientRepository.findById(account.getClientId()).orElseThrow(() -> new NotFoundException(ClientMessages.NOT_FOUND));
        if (repository.existsByNumber(account.getNumber())) {
            throw new BusinessException(AccountMessages.ALREADY_EXISTS);
        }
        if (account.getCurrentBalance() == null) {
            account.setCurrentBalance(account.getInitialBalance());
        }
        return mapper.toDto(repository.save(account));
    }

    public List<AccountDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public AccountDto findById(Long id) {
        return mapper.toDto(findModelById(id));
    }

    public AccountDto update(Long id, AccountDto accountDto) {
        findModelById(id);
        AccountModel account = mapper.toModel(accountDto);
        account.setId(id);
        return mapper.toDto(repository.save(account));
    }

    public AccountDto patch(Long id, AccountDto patchDto) {
        AccountModel current = findModelById(id);
        AccountModel patch = mapper.toModel(patchDto);
        if (patch.getNumber() != null) current.setNumber(patch.getNumber());
        if (patch.getType() != null) current.setType(patch.getType());
        if (patch.getInitialBalance() != null) current.setInitialBalance(patch.getInitialBalance());
        if (patch.getCurrentBalance() != null) current.setCurrentBalance(patch.getCurrentBalance());
        if (patch.getStatus() != null) current.setStatus(patch.getStatus());
        if (patch.getClientId() != null) current.setClientId(patch.getClientId());
        return mapper.toDto(repository.save(current));
    }

    public void delete(Long id) {
        findModelById(id);
        if (movementRepository.existsByAccountId(id)) {
            throw new BusinessException(AccountMessages.DELETE_WITH_MOVEMENTS);
        }
        repository.deleteById(id);
    }

    private AccountModel findModelById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(AccountMessages.NOT_FOUND));
    }
}
