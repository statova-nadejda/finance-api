package com.finance.repository;

import com.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_Id(Long accountId);

    List<Transaction> findByCategory_Id(Long categoryId);

    List<Transaction> findByAccount_IdAndCategory_Id(Long accountId, Long categoryId);

    List<Transaction> findByAccount_User_Id(Long userId);
}
