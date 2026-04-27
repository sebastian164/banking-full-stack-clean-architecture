package com.prove.bank.application.port.in;

import com.prove.bank.application.dto.MovementDto;
import java.util.List;

public interface MovementUseCase {
    MovementDto create(MovementDto movement);
    List<MovementDto> findAll();
    MovementDto findById(Long id);
    MovementDto update(Long id, MovementDto movement);
    MovementDto patch(Long id, MovementDto movement);
    void delete(Long id);
}
