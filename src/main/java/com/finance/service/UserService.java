package com.finance.service;

import com.finance.exception.InvalidDataException;
import com.finance.exception.UserNotFoundException;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        validateId(id);
        return findExisting(id);
    }

    public User create(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    public User update(Long id, User userDetails) {
        validateId(id);
        validateUser(userDetails);

        User user = findExisting(id);

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());

        return userRepository.save(user);
    }

    public void delete(Long id) {
        validateId(id);
        userRepository.delete(findExisting(id));
    }

    private User findExisting(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidDataException("User data is required");
        }
    }
}
