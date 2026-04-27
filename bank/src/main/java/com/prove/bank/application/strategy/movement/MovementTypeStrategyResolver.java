package com.prove.bank.application.strategy.movement;

import com.prove.bank.domain.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovementTypeStrategyResolver {
    private final List<MovementTypeStrategy> strategies;

    public MovementTypeStrategy resolve(String type) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(type))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Tipo de movimiento no soportado"));
    }
}
