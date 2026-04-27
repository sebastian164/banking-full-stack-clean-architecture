package com.prove.bank.infrastructure.input.adapter.rest;

import com.prove.bank.application.dto.AccountDto;
import com.prove.bank.application.port.in.AccountUseCase;
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
@RequestMapping(ApiRoutes.Accounts.ROOT)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {
    private final AccountUseCase service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto create(@Valid @RequestBody AccountDto account) {
        return service.create(account);
    }

    @GetMapping
    public List<AccountDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AccountDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public AccountDto update(@PathVariable Long id, @Valid @RequestBody AccountDto account) {
        return service.update(id, account);
    }

    @PatchMapping("/{id}")
    public AccountDto patch(@PathVariable Long id, @RequestBody AccountDto account) {
        return service.patch(id, account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
