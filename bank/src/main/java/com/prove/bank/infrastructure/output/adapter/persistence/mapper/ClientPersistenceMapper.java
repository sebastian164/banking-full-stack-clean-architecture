package com.prove.bank.infrastructure.output.adapter.persistence.mapper;

import com.prove.bank.domain.model.ClientModel;
import com.prove.bank.infrastructure.output.adapter.persistence.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientPersistenceMapper {
    public ClientModel toModel(ClientEntity entity) {
        return ClientModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .gender(entity.getGender())
                .age(entity.getAge())
                .identification(entity.getIdentification())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .password(entity.getPassword())
                .status(entity.getStatus())
                .build();
    }

    public ClientEntity toEntity(ClientModel model) {
        ClientEntity entity = new ClientEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setGender(model.getGender());
        entity.setAge(model.getAge());
        entity.setIdentification(model.getIdentification());
        entity.setAddress(model.getAddress());
        entity.setPhone(model.getPhone());
        entity.setPassword(model.getPassword());
        entity.setStatus(model.getStatus());
        return entity;
    }
}
