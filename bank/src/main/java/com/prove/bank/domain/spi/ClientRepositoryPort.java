package com.prove.bank.domain.spi;

import com.prove.bank.domain.model.ClientModel;
import java.util.List;
import java.util.Optional;

public interface ClientRepositoryPort {
    ClientModel save(ClientModel client);
    List<ClientModel> findAll();
    Optional<ClientModel> findById(Long id);
    boolean existsByIdentification(String identification);
    void deleteById(Long id);
}
