package com.prove.bank.infrastructure.output.adapter.persistence.repository;

import com.prove.bank.infrastructure.output.adapter.persistence.entity.MovementEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long> {
    List<MovementEntity> findByAccount_NumberAndDateBetween(String accountNumber, LocalDate startDate, LocalDate endDate);
    boolean existsByAccount_Id(Long accountId);

    @Query("""
            select coalesce(sum(abs(m.value)), 0)
            from MovementEntity m
            where m.account.number = :accountNumber
              and m.date = :date
              and m.value < 0
            """)
    BigDecimal sumDebitsByAccountAndDate(@Param("accountNumber") String accountNumber, @Param("date") LocalDate date);

    @Modifying
    @Query("delete from MovementEntity m where m.account.client.id = :clientId")
    void deleteByClientId(@Param("clientId") Long clientId);

    @Modifying
    @Query("delete from MovementEntity m where m.account.id = :accountId")
    void deleteByAccountId(@Param("accountId") Long accountId);
}
