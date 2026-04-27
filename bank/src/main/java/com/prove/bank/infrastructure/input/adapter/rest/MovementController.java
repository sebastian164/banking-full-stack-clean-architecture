package com.prove.bank.infrastructure.input.adapter.rest;

import com.prove.bank.application.dto.MovementDto;
import com.prove.bank.application.port.in.MovementUseCase;
import com.prove.bank.util.constants.ApiRoutes;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.Movements.ROOT)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MovementController {
    private final MovementUseCase service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovementDto create(@Valid @RequestBody MovementDto movement) {
        return service.create(movement);
    }

    @GetMapping
    public List<MovementDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MovementDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public MovementDto update(@PathVariable Long id, @Valid @RequestBody MovementDto movement) {
        return service.update(id, movement);
    }

    @PatchMapping("/{id}")
    public MovementDto patch(@PathVariable Long id, @RequestBody MovementDto movement) {
        return service.patch(id, movement);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
