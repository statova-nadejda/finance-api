package com.finance.service;

import com.finance.dto.TransactionRequest;
import com.finance.dto.TransactionResponse;
import com.finance.exception.InvalidDataException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.Account;
import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.repository.AccountRepository;
import com.finance.repository.CategoryRepository;
import com.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public List<TransactionResponse> getAll() {
        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TransactionResponse> getAll(Long accountId, Long categoryId) {
        validateOptionalId(accountId, "accountId");
        validateOptionalId(categoryId, "categoryId");

        List<Transaction> transactions;

        if (accountId != null && categoryId != null) {
            transactions = transactionRepository.findByAccount_IdAndCategory_Id(accountId, categoryId);
        } else if (accountId != null) {
            transactions = transactionRepository.findByAccount_Id(accountId);
        } else if (categoryId != null) {
            transactions = transactionRepository.findByCategory_Id(categoryId);
        } else {
            transactions = transactionRepository.findAll();
        }

        return transactions.stream()
                .map(this::toResponse)
                .toList();
    }

    public TransactionResponse getById(Long id) {
        validateId(id);
        return toResponse(findExisting(id));
    }

    public TransactionResponse create(TransactionRequest request) {
        validateTransaction(request);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setDescription(request.description());
        transaction.setAccount(findAccount(request.accountId()));
        transaction.setCategory(findCategory(request.categoryId()));

        return toResponse(transactionRepository.save(transaction));
    }

    public TransactionResponse update(Long id, TransactionRequest request) {
        validateId(id);
        validateTransaction(request);

        Transaction transaction = findExisting(id);
        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setDescription(request.description());
        transaction.setAccount(findAccount(request.accountId()));
        transaction.setCategory(findCategory(request.categoryId()));

        return toResponse(transactionRepository.save(transaction));
    }

    public void delete(Long id) {
        validateId(id);
        transactionRepository.delete(findExisting(id));
    }

    private Transaction findExisting(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    private Account findAccount(Long id) {
        validateRelatedId(id, "accountId");
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    private Category findCategory(Long id) {
        validateRelatedId(id, "categoryId");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAccount().getId(),
                transaction.getCategory().getId()
        );
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateOptionalId(Long id, String fieldName) {
        if (id != null && id <= 0) {
            throw new InvalidDataException(fieldName + " must be greater than 0");
        }
    }

    private void validateRelatedId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(fieldName + " must be greater than 0");
        }
    }

    private void validateTransaction(TransactionRequest transaction) {
        if (transaction == null) {
            throw new InvalidDataException("Transaction data is required");
        }
    }
}
