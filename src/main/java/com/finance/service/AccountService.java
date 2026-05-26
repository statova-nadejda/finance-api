package com.finance.service;

import com.finance.exception.InvalidDataException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.Account;
import com.finance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account getById(Long id) {
        validateId(id);
        return findExisting(id);
    }

    public Account create(Account account) {
        validateAccount(account);
        return accountRepository.save(account);
    }

    public Account update(Long id, Account accountDetails) {
        validateId(id);
        validateAccount(accountDetails);

        Account account = findExisting(id);

        account.setName(accountDetails.getName());
        account.setBalance(accountDetails.getBalance());
        account.setUser(accountDetails.getUser());

        return accountRepository.save(account);
    }

    public void delete(Long id) {
        validateId(id);
        accountRepository.delete(findExisting(id));
    }

    private Account findExisting(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateAccount(Account account) {
        if (account == null) {
            throw new InvalidDataException("Account data is required");
        }
    }
}
