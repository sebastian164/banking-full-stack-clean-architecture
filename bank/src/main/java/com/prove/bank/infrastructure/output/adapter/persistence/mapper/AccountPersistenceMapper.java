package com.prove.bank.infrastructure.output.adapter.persistence.mapper;

import com.prove.bank.domain.model.AccountModel;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.AccountEntity;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {
    public AccountModel toModel(AccountEntity entity) {
        return AccountModel.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .type(entity.getType())
                .initialBalance(entity.getInitialBalance())
                .currentBalance(entity.getCurrentBalance())
                .status(entity.getStatus())
                .clientId(entity.getClient().getId())
                .clientName(entity.getClient().getName())
                .build();
    }

    public AccountEntity toEntity(AccountModel model, ClientEntity client) {
        AccountEntity entity = new AccountEntity();
        entity.setId(model.getId());
        entity.setNumber(model.getNumber());
        entity.setType(model.getType());
        entity.setInitialBalance(model.getInitialBalance());
        entity.setCurrentBalance(model.getCurrentBalance() == null ? model.getInitialBalance() : model.getCurrentBalance());
        entity.setStatus(model.getStatus());
        entity.setClient(client);
        return entity;
    }
}
