package com.prove.bank.application.service;

import com.prove.bank.application.dto.MovementDto;
import com.prove.bank.application.mapper.ApplicationDtoMapper;
import com.prove.bank.application.port.in.MovementUseCase;
import com.prove.bank.application.strategy.movement.MovementTypeStrategy;
import com.prove.bank.application.strategy.movement.MovementTypeStrategyResolver;
import com.prove.bank.domain.exception.NotFoundException;
import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.model.MovementModel;
import com.prove.bank.domain.spi.AccountRepositoryPort;
import com.prove.bank.domain.spi.MovementRepositoryPort;
import com.prove.bank.util.constants.messages.AccountMessages;
import com.prove.bank.util.constants.messages.MovementMessages;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovementService implements MovementUseCase {
    private final MovementRepositoryPort movementRepository;
    private final AccountRepositoryPort accountRepository;
    private final MovementTypeStrategyResolver movementTypeStrategyResolver;
    private final ApplicationDtoMapper mapper;

    @Transactional
    public MovementDto create(MovementDto movementDto) {
        MovementModel movement = mapper.toModel(movementDto);
        AccountModel account = accountRepository.findByNumber(movement.getAccountNumber())
                .orElseThrow(() -> new NotFoundException(AccountMessages.NOT_FOUND));
        MovementTypeStrategy strategy = movementTypeStrategyResolver.resolve(movement.getType());
        BigDecimal value = strategy.calculateValue(account, movement);
        BigDecimal newBalance = account.getCurrentBalance().add(value);
        movement.setValue(value);
        movement.setBalance(newBalance);
        movement.setDate(movement.getDate() == null ? LocalDate.now() : movement.getDate());
        MovementModel saved = movementRepository.save(movement);
        accountRepository.updateCurrentBalance(account.getNumber(), newBalance);
        return mapper.toDto(saved);
    }

    public List<MovementDto> findAll() {
        return movementRepository.findAll().stream().map(mapper::toDto).toList();
    }

    public MovementDto findById(Long id) {
        return mapper.toDto(findModelById(id));
    }

    public MovementDto update(Long id, MovementDto movementDto) {
        findModelById(id);
        MovementModel movement = mapper.toModel(movementDto);
        movement.setId(id);
        return mapper.toDto(movementRepository.save(movement));
    }

    public MovementDto patch(Long id, MovementDto patchDto) {
        MovementModel current = findModelById(id);
        MovementModel patch = mapper.toModel(patchDto);
        if (patch.getDate() != null) current.setDate(patch.getDate());
        if (patch.getType() != null) current.setType(patch.getType());
        if (patch.getValue() != null) current.setValue(patch.getValue());
        if (patch.getBalance() != null) current.setBalance(patch.getBalance());
        if (patch.getAccountNumber() != null) current.setAccountNumber(patch.getAccountNumber());
        return mapper.toDto(movementRepository.save(current));
    }

    public void delete(Long id) {
        findModelById(id);
        movementRepository.deleteById(id);
    }

    private MovementModel findModelById(Long id) {
        return movementRepository.findById(id).orElseThrow(() -> new NotFoundException(MovementMessages.NOT_FOUND));
    }
}
