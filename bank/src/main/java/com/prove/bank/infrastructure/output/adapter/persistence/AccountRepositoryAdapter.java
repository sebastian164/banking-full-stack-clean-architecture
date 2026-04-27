package com.prove.bank.infrastructure.output.adapter.persistence;

import com.prove.bank.domain.exception.NotFoundException;
import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.spi.AccountRepositoryPort;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.AccountEntity;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.ClientEntity;
import com.prove.bank.infrastructure.output.adapter.persistence.mapper.AccountPersistenceMapper;
import com.prove.bank.infrastructure.output.adapter.persistence.repository.AccountJpaRepository;
import com.prove.bank.infrastructure.output.adapter.persistence.repository.ClientJpaRepository;
import com.prove.bank.util.constants.messages.AccountMessages;
import com.prove.bank.util.constants.messages.ClientMessages;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class AccountRepositoryAdapter implements AccountRepositoryPort {
    private final AccountJpaRepository repository;
    private final ClientJpaRepository clientRepository;
    private final AccountPersistenceMapper mapper;

    @Override
    public AccountModel save(AccountModel account) {
        ClientEntity client = clientRepository.findById(account.getClientId())
                .orElseThrow(() -> new NotFoundException(ClientMessages.NOT_FOUND));
        return mapper.toModel(repository.save(mapper.toEntity(account, client)));
    }

    @Override
    public List<AccountModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public List<AccountModel> findByClientId(Long clientId) {
        return repository.findByClientId(clientId).stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<AccountModel> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<AccountModel> findByNumber(String number) {
        return repository.findByNumber(number).map(mapper::toModel);
    }

    @Override
    public boolean existsByNumber(String number) {
        return repository.existsByNumber(number);
    }

    @Override
    public boolean existsByClientId(Long clientId) {
        return repository.existsByClientId(clientId);
    }

    @Override
    public void updateCurrentBalance(String number, BigDecimal currentBalance) {
        AccountEntity account = repository.findByNumber(number)
                .orElseThrow(() -> new NotFoundException(AccountMessages.NOT_FOUND));
        account.setCurrentBalance(currentBalance);
        repository.save(account);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
