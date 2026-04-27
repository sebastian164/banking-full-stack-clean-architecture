package com.prove.bank.infrastructure.config;

import com.prove.bank.infrastructure.output.adapter.persistence.entity.AccountEntity;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.ClientEntity;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.MovementEntity;
import com.prove.bank.infrastructure.output.adapter.persistence.repository.AccountJpaRepository;
import com.prove.bank.infrastructure.output.adapter.persistence.repository.ClientJpaRepository;
import com.prove.bank.infrastructure.output.adapter.persistence.repository.MovementJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ClientJpaRepository clientRepository;
    private final AccountJpaRepository accountRepository;
    private final MovementJpaRepository movementRepository;

    @Override
    public void run(String... args) {
        if (clientRepository.count() > 0) {
            return;
        }
        ClientEntity jose = client("Jose Lema", "Masculino", 30, "1234567890", "Otavalo sn y principal", "098254785", "1234");
        ClientEntity marianela = client("Marianela Montalvo", "Femenino", 28, "0955555555", "Amazonas y NNUU", "097548965", "5678");
        ClientEntity juan = client("Juan Osorio", "Masculino", 35, "1717171717", "13 junio y Equinoccial", "098874587", "1245");
        clientRepository.save(jose);
        clientRepository.save(marianela);
        clientRepository.save(juan);
        AccountEntity joseSavings = accountRepository.save(account("478758", "Ahorro", new BigDecimal("1425"), new BigDecimal("2000"), jose));
        AccountEntity marianelaCurrent = accountRepository.save(account("225487", "Corriente", new BigDecimal("700"), new BigDecimal("100"), marianela));
        AccountEntity juanSavings = accountRepository.save(account("495878", "Ahorros", new BigDecimal("150"), BigDecimal.ZERO, juan));
        AccountEntity marianelaSavings = accountRepository.save(account("496825", "Ahorros", BigDecimal.ZERO, new BigDecimal("540"), marianela));
        accountRepository.save(account("585545", "Corriente", new BigDecimal("1000"), jose));

        movementRepository.save(movement(LocalDate.of(2026, 2, 10), "Retiro", new BigDecimal("-575"), new BigDecimal("1425"), joseSavings));
        movementRepository.save(movement(LocalDate.of(2026, 2, 10), "Deposito", new BigDecimal("600"), new BigDecimal("700"), marianelaCurrent));
        movementRepository.save(movement(LocalDate.of(2026, 2, 10), "Deposito", new BigDecimal("150"), new BigDecimal("150"), juanSavings));
        movementRepository.save(movement(LocalDate.of(2026, 2, 8), "Retiro", new BigDecimal("-540"), BigDecimal.ZERO, marianelaSavings));
    }

    private ClientEntity client(String name, String gender, Integer age, String identification, String address, String phone, String password) {
        ClientEntity client = new ClientEntity();
        client.setName(name);
        client.setGender(gender);
        client.setAge(age);
        client.setIdentification(identification);
        client.setAddress(address);
        client.setPhone(phone);
        client.setPassword(password);
        client.setStatus(true);
        return client;
    }

    private AccountEntity account(String number, String type, BigDecimal balance, ClientEntity client) {
        return account(number, type, balance, balance, client);
    }

    private AccountEntity account(String number, String type, BigDecimal currentBalance, BigDecimal initialBalance, ClientEntity client) {
        AccountEntity account = new AccountEntity();
        account.setNumber(number);
        account.setType(type);
        account.setInitialBalance(initialBalance);
        account.setCurrentBalance(currentBalance);
        account.setStatus(true);
        account.setClient(client);
        return account;
    }

    private MovementEntity movement(LocalDate date, String type, BigDecimal value, BigDecimal balance, AccountEntity account) {
        MovementEntity movement = new MovementEntity();
        movement.setDate(date);
        movement.setType(type);
        movement.setValue(value);
        movement.setBalance(balance);
        movement.setAccount(account);
        return movement;
    }
}
