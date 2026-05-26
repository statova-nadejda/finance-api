package com.finance.service;

import com.finance.exception.InvalidDataException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.Transaction;
import com.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction getById(Long id) {
        validateId(id);
        return findExisting(id);
    }

    public Transaction create(Transaction transaction) {
        validateTransaction(transaction);
        return transactionRepository.save(transaction);
    }

    public Transaction update(Long id, Transaction transactionDetails) {
        validateId(id);
        validateTransaction(transactionDetails);

        Transaction transaction = findExisting(id);

        transaction.setAmount(transactionDetails.getAmount());
        transaction.setDate(transactionDetails.getDate());
        transaction.setDescription(transactionDetails.getDescription());
        transaction.setAccount(transactionDetails.getAccount());
        transaction.setCategory(transactionDetails.getCategory());

        return transactionRepository.save(transaction);
    }

    public void delete(Long id) {
        validateId(id);
        transactionRepository.delete(findExisting(id));
    }

    private Transaction findExisting(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new InvalidDataException("Transaction data is required");
        }
    }
}
