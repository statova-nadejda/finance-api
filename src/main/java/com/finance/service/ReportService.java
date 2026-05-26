package com.finance.service;

import com.finance.dto.SummaryReportResponse;
import com.finance.exception.InvalidDataException;
import com.finance.exception.UserNotFoundException;
import com.finance.model.CategoryType;
import com.finance.model.Transaction;
import com.finance.repository.TransactionRepository;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public SummaryReportResponse getSummary(Long userId) {
        validateUserId(userId);

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        List<Transaction> transactions = transactionRepository.findByAccount_User_Id(userId);

        double income = 0;
        double expense = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getCategory().getType() == CategoryType.INCOME) {
                income += transaction.getAmount();
            }

            if (transaction.getCategory().getType() == CategoryType.EXPENSE) {
                expense += transaction.getAmount();
            }
        }

        return new SummaryReportResponse(income, expense, income - expense);
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new InvalidDataException("userId must be greater than 0");
        }
    }
}
