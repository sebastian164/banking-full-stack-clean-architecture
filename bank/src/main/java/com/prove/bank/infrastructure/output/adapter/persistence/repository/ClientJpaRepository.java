package com.prove.bank.infrastructure.output.adapter.persistence.repository;

import com.prove.bank.infrastructure.output.adapter.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {
    boolean existsByIdentification(String identification);
}
