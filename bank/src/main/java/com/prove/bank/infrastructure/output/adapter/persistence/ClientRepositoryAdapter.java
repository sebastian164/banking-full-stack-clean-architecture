package com.prove.bank.infrastructure.output.adapter.persistence;

import com.prove.bank.domain.model.ClientModel;
import com.prove.bank.domain.spi.ClientRepositoryPort;
import com.prove.bank.infrastructure.output.adapter.persistence.mapper.ClientPersistenceMapper;
import com.prove.bank.infrastructure.output.adapter.persistence.repository.ClientJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepositoryPort {
    private final ClientJpaRepository repository;
    private final ClientPersistenceMapper mapper;

    @Override
    public ClientModel save(ClientModel client) {
        return mapper.toModel(repository.save(mapper.toEntity(client)));
    }

    @Override
    public List<ClientModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<ClientModel> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public boolean existsByIdentification(String identification) {
        return repository.existsByIdentification(identification);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
