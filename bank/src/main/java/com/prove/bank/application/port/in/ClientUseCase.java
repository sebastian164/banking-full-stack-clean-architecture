package com.prove.bank.application.port.in;

import com.prove.bank.application.dto.ClientDto;
import java.util.List;

public interface ClientUseCase {
    ClientDto create(ClientDto client);
    List<ClientDto> findAll();
    ClientDto findById(Long id);
    ClientDto update(Long id, ClientDto client);
    ClientDto patch(Long id, ClientDto client);
    void delete(Long id);
}
