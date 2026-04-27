package com.prove.bank.application.service;

import com.prove.bank.application.dto.ClientDto;
import com.prove.bank.application.mapper.ApplicationDtoMapper;
import com.prove.bank.application.port.in.ClientUseCase;
import com.prove.bank.domain.exception.BusinessException;
import com.prove.bank.domain.exception.NotFoundException;
import com.prove.bank.domain.model.ClientModel;
import com.prove.bank.domain.spi.AccountRepositoryPort;
import com.prove.bank.domain.spi.ClientRepositoryPort;
import com.prove.bank.util.constants.messages.ClientMessages;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientUseCase {
    private final ClientRepositoryPort repository;
    private final AccountRepositoryPort accountRepository;
    private final ApplicationDtoMapper mapper;

    public ClientDto create(ClientDto clientDto) {
        ClientModel client = mapper.toModel(clientDto);
        if (repository.existsByIdentification(client.getIdentification())) {
            throw new BusinessException(ClientMessages.ALREADY_EXISTS);
        }
        return mapper.toDto(repository.save(client));
    }

    public List<ClientDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public ClientDto findById(Long id) {
        return mapper.toDto(findModelById(id));
    }

    public ClientDto update(Long id, ClientDto clientDto) {
        findModelById(id);
        ClientModel client = mapper.toModel(clientDto);
        client.setId(id);
        return mapper.toDto(repository.save(client));
    }

    public ClientDto patch(Long id, ClientDto patchDto) {
        ClientModel current = findModelById(id);
        ClientModel patch = mapper.toModel(patchDto);
        if (patch.getName() != null) current.setName(patch.getName());
        if (patch.getGender() != null) current.setGender(patch.getGender());
        if (patch.getAge() != null) current.setAge(patch.getAge());
        if (patch.getIdentification() != null) current.setIdentification(patch.getIdentification());
        if (patch.getAddress() != null) current.setAddress(patch.getAddress());
        if (patch.getPhone() != null) current.setPhone(patch.getPhone());
        if (patch.getPassword() != null) current.setPassword(patch.getPassword());
        if (patch.getStatus() != null) current.setStatus(patch.getStatus());
        return mapper.toDto(repository.save(current));
    }

    public void delete(Long id) {
        findModelById(id);
        if (accountRepository.existsByClientId(id)) {
            throw new BusinessException(ClientMessages.DELETE_WITH_ACCOUNTS);
        }
        repository.deleteById(id);
    }

    private ClientModel findModelById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(ClientMessages.NOT_FOUND));
    }
}
