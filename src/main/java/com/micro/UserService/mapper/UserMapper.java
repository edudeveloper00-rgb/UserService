package com.micro.UserService.mapper;

import org.springframework.stereotype.Component;

import com.micro.UserService.dto.request.CreateUserRequest;
import com.micro.UserService.dto.request.UpdateUserRequest;
import com.micro.UserService.dto.response.UserResponse;
import com.micro.UserService.entity.User;
import com.micro.UserService.enums.UserStatus;

@Component
public class UserMapper {
public User toEntity(CreateUserRequest request) {

        User user = new User();

        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPhone(request.phone());

        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    public void updateEntity(User user,UpdateUserRequest request) {

        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPhone(request.phone());
    }

    public UserResponse toResponse(User user) {

        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getStatus(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
