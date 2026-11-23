package com.micro.UserService.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.UserService.dto.request.CreateUserRequest;
import com.micro.UserService.dto.request.UpdateUserRequest;
import com.micro.UserService.dto.request.UpdateUserStatusRequest;
import com.micro.UserService.dto.response.UserResponse;
import com.micro.UserService.exception.EmailAlreadyExistsException;
import com.micro.UserService.exception.UserNotFoundException;
import com.micro.UserService.mapper.UserMapper;
import com.micro.UserService.repository.UserRepository;
import com.micro.UserService.entity.User;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {
private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // CREATE USER
    public UserResponse createUser(CreateUserRequest request) {

        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = userMapper.toEntity(request);

        User savedUser = userRepository.save(user);
        // Fuerza a Hibernate a sincronizar los cambios con Oracle
        userRepository.flush();

        return userMapper.toResponse(savedUser);
    }

    // GET ALL USERS
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    // GET USER BY ID
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {

        User user = findUserOrThrow(id);

        return userMapper.toResponse(user);
    }

    // GET USER BY EMAIL
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {

        User user = userRepository
                .findByEmailIgnoreCase(email.trim())
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "User not found with email: " + email
                    )
                );

        return userMapper.toResponse(user);
    }

    // UPDATE USER
    public UserResponse updateUser(
            UUID id,
            UpdateUserRequest request) {

        User user = findUserOrThrow(id);

        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCaseAndIdNot(
                email, id)) {

            throw new EmailAlreadyExistsException(email);
        }

        userMapper.updateEntity(user, request);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    // UPDATE USER STATUS
    public UserResponse updateUserStatus(
            UUID id,
            UpdateUserStatusRequest request) {

        User user = findUserOrThrow(id);

        user.setStatus(request.status());

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    // PRIVATE HELPER
    private User findUserOrThrow(UUID id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                    new UserNotFoundException(id)
                );
    }
}
