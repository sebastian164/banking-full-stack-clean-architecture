package com.prove.bank.infrastructure.output.adapter.persistence;

import com.prove.bank.domain.exception.NotFoundException;
import com.prove.bank.domain.model.MovementModel;
import com.prove.bank.domain.spi.MovementRepositoryPort;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.AccountEntity;
import com.prove.bank.infrastructure.output.adapter.persistence.mapper.MovementPersistenceMapper;
import com.prove.bank.infrastructure.output.adapter.persistence.repository.AccountJpaRepository;
import com.prove.bank.infrastructure.output.adapter.persistence.repository.MovementJpaRepository;
import com.prove.bank.util.constants.messages.AccountMessages;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class MovementRepositoryAdapter implements MovementRepositoryPort {
    private final MovementJpaRepository repository;
    private final AccountJpaRepository accountRepository;
    private final MovementPersistenceMapper mapper;

    @Override
    public MovementModel save(MovementModel movement) {
        AccountEntity account = accountRepository.findByNumber(movement.getAccountNumber())
                .orElseThrow(() -> new NotFoundException(AccountMessages.NOT_FOUND));
        return mapper.toModel(repository.save(mapper.toEntity(movement, account)));
    }

    @Override
    public List<MovementModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<MovementModel> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<MovementModel> findByAccountNumberAndDateBetween(String accountNumber, LocalDate startDate, LocalDate endDate) {
        return repository.findByAccount_NumberAndDateBetween(accountNumber, startDate, endDate)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public BigDecimal sumDebitsByAccountAndDate(String accountNumber, LocalDate date) {
        return repository.sumDebitsByAccountAndDate(accountNumber, date);
    }

    @Override
    public boolean existsByAccountId(Long accountId) {
        return repository.existsByAccount_Id(accountId);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
