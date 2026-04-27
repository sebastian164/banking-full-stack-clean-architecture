package com.prove.bank.infrastructure.output.adapter.persistence.repository;

import com.prove.bank.infrastructure.output.adapter.persistence.entity.AccountEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByNumber(String number);
    boolean existsByNumber(String number);
    boolean existsByClientId(Long clientId);
    List<AccountEntity> findByClientId(Long clientId);

    @Modifying
    @Query("delete from AccountEntity a where a.client.id = :clientId")
    void deleteByClientId(@Param("clientId") Long clientId);
}
