package com.prove.bank.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prove.bank.application.dto.MovementDto;
import com.prove.bank.application.mapper.ApplicationDtoMapper;
import com.prove.bank.application.strategy.movement.CreditMovementStrategy;
import com.prove.bank.application.strategy.movement.DebitMovementStrategy;
import com.prove.bank.application.strategy.movement.MovementTypeStrategyResolver;
import com.prove.bank.domain.exception.BusinessException;
import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.domain.spi.AccountRepositoryPort;
import com.prove.bank.domain.spi.MovementRepositoryPort;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {
    @Mock
    private MovementRepositoryPort movementRepository;
    @Mock
    private AccountRepositoryPort accountRepository;

    private MovementService service;

    @BeforeEach
    void setUp() {
        DebitMovementStrategy debitStrategy = new DebitMovementStrategy(movementRepository);
        ReflectionTestUtils.setField(debitStrategy, "dailyWithdrawalLimit", new BigDecimal("1000"));
        MovementTypeStrategyResolver resolver = new MovementTypeStrategyResolver(
                List.of(new CreditMovementStrategy(), debitStrategy));
        service = new MovementService(movementRepository, accountRepository, resolver, new ApplicationDtoMapper());
    }

    @Test
    void shouldCreateCreditAndUpdateAccountBalance() {
        AccountModel account = AccountModel.builder()
                .number("100")
                .currentBalance(new BigDecimal("100"))
                .build();
        MovementDto movement = MovementDto.builder()
                .date(LocalDate.of(2026, 4, 26))
                .type("CREDITO")
                .value(new BigDecimal("50"))
                .accountNumber("100")
                .build();
        when(accountRepository.findByNumber("100")).thenReturn(Optional.of(account));
        when(movementRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));

        MovementDto saved = service.create(movement);

        assertThat(saved.getValue()).isEqualByComparingTo("50");
        assertThat(saved.getBalance()).isEqualByComparingTo("150");
        verify(accountRepository).updateCurrentBalance("100", new BigDecimal("150"));
    }

    @Test
    void shouldRejectDebitWhenBalanceIsInsufficient() {
        AccountModel account = AccountModel.builder()
                .number("100")
                .currentBalance(new BigDecimal("30"))
                .build();
        MovementDto movement = MovementDto.builder()
                .date(LocalDate.of(2026, 4, 26))
                .type("DEBITO")
                .value(new BigDecimal("50"))
                .accountNumber("100")
                .build();
        when(accountRepository.findByNumber("100")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> service.create(movement))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Saldo no disponible");
    }

    @Test
    void shouldRejectDebitWhenDailyLimitIsExceeded() {
        AccountModel account = AccountModel.builder()
                .number("100")
                .currentBalance(new BigDecimal("2000"))
                .build();
        MovementDto movement = MovementDto.builder()
                .date(LocalDate.of(2026, 4, 26))
                .type("DEBITO")
                .value(new BigDecimal("200"))
                .accountNumber("100")
                .build();
        when(accountRepository.findByNumber("100")).thenReturn(Optional.of(account));
        when(movementRepository.sumDebitsByAccountAndDate("100", LocalDate.of(2026, 4, 26)))
                .thenReturn(new BigDecimal("900"));

        assertThatThrownBy(() -> service.create(movement))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cupo diario excedido");
    }
}
