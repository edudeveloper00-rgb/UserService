package com.micro.UserService.dto.response;

import java.time.Instant;
import java.util.UUID;
import com.micro.UserService.enums.UserStatus;

public record UserResponse( 
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phone,
    UserStatus status,
    Instant createdAt,
    Instant updatedAt) {

}
