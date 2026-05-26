package com.finance.service;

import com.finance.dto.AccountRequest;
import com.finance.dto.AccountResponse;
import com.finance.exception.InvalidDataException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.exception.UserNotFoundException;
import com.finance.model.Account;
import com.finance.model.User;
import com.finance.repository.AccountRepository;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<AccountResponse> getAll() {
        return accountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AccountResponse getById(Long id) {
        validateId(id);
        return toResponse(findExisting(id));
    }

    public AccountResponse create(AccountRequest request) {
        validateAccount(request);

        Account account = new Account();
        account.setName(request.name());
        account.setBalance(request.balance());
        account.setUser(findUser(request.userId()));

        return toResponse(accountRepository.save(account));
    }

    public AccountResponse update(Long id, AccountRequest request) {
        validateId(id);
        validateAccount(request);

        Account account = findExisting(id);
        account.setName(request.name());
        account.setBalance(request.balance());
        account.setUser(findUser(request.userId()));

        return toResponse(accountRepository.save(account));
    }

    public void delete(Long id) {
        validateId(id);
        accountRepository.delete(findExisting(id));
    }

    private Account findExisting(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    private User findUser(Long id) {
        validateRelatedId(id, "userId");
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getBalance(),
                account.getUser().getId()
        );
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateRelatedId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(fieldName + " must be greater than 0");
        }
    }

    private void validateAccount(AccountRequest account) {
        if (account == null) {
            throw new InvalidDataException("Account data is required");
        }
    }
}
