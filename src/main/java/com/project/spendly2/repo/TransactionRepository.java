package com.project.spendly2.repo;

import com.project.spendly2.models.entities.Transactions;
import com.project.spendly2.models.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    List<Transactions> findByTransactionStatus(TransactionStatus transactionStatus);

    Transactions findByTime(LocalDateTime now);
}
