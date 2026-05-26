package com.finance.service;

import com.finance.dto.UserRequest;
import com.finance.dto.UserResponse;
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

    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getById(Long id) {
        validateId(id);
        return toResponse(findExisting(id));
    }

    public UserResponse create(UserRequest request) {
        validateUser(request);

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());

        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UserRequest request) {
        validateId(id);
        validateUser(request);

        User user = findExisting(id);
        user.setName(request.name());
        user.setEmail(request.email());

        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        validateId(id);
        userRepository.delete(findExisting(id));
    }

    private User findExisting(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidDataException("Id must be greater than 0");
        }
    }

    private void validateUser(UserRequest user) {
        if (user == null) {
            throw new InvalidDataException("User data is required");
        }
    }
}
