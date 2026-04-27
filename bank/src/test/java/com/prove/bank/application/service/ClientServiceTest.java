package com.prove.bank.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prove.bank.application.dto.ClientDto;
import com.prove.bank.application.mapper.ApplicationDtoMapper;
import com.prove.bank.domain.exception.BusinessException;
import com.prove.bank.domain.exception.NotFoundException;
import com.prove.bank.domain.model.ClientModel;
import com.prove.bank.domain.spi.AccountRepositoryPort;
import com.prove.bank.domain.spi.ClientRepositoryPort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepositoryPort repository;
    @Mock
    private AccountRepositoryPort accountRepository;
    @Spy
    private ApplicationDtoMapper mapper = new ApplicationDtoMapper();

    @InjectMocks
    private ClientService service;

    @Test
    void shouldRejectDuplicatedIdentification() {
        ClientDto client = ClientDto.builder().identification("123").build();
        when(repository.existsByIdentification("123")).thenReturn(true);

        assertThatThrownBy(() -> service.create(client))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cliente ya existe");
    }

    @Test
    void shouldPatchOnlyProvidedFields() {
        ClientModel current = ClientModel.builder()
                .id(1L)
                .name("Jose")
                .address("Old")
                .status(true)
                .build();
        ClientDto patch = ClientDto.builder().address("New").build();
        when(repository.findById(1L)).thenReturn(Optional.of(current));
        when(repository.save(current)).thenReturn(current);

        ClientDto updated = service.patch(1L, patch);

        assertThat(updated.getName()).isEqualTo("Jose");
        assertThat(updated.getAddress()).isEqualTo("New");
        verify(repository).save(current);
    }

    @Test
    void shouldThrowWhenClientDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Cliente no encontrado");
    }

    @Test
    void shouldRejectDeleteWhenClientHasAccounts() {
        when(repository.findById(1L)).thenReturn(Optional.of(ClientModel.builder().id(1L).build()));
        when(accountRepository.existsByClientId(1L)).thenReturn(true);

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("No se puede eliminar el cliente porque tiene cuentas asociadas");
    }
}
