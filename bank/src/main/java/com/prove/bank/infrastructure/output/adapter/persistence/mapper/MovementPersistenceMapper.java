package com.prove.bank.infrastructure.output.adapter.persistence.mapper;

import com.prove.bank.domain.model.MovementModel;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.AccountEntity;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.MovementEntity;
import org.springframework.stereotype.Component;

@Component
public class MovementPersistenceMapper {
    public MovementModel toModel(MovementEntity entity) {
        return MovementModel.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .type(entity.getType())
                .value(entity.getValue())
                .balance(entity.getBalance())
                .accountNumber(entity.getAccount().getNumber())
                .build();
    }

    public MovementEntity toEntity(MovementModel model, AccountEntity account) {
        MovementEntity entity = new MovementEntity();
        entity.setId(model.getId());
        entity.setDate(model.getDate());
        entity.setType(model.getType());
        entity.setValue(model.getValue());
        entity.setBalance(model.getBalance());
        entity.setAccount(account);
        return entity;
    }
}
