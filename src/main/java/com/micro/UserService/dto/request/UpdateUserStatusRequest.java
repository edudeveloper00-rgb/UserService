package com.micro.UserService.dto.request;

import com.micro.UserService.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
public record UpdateUserStatusRequest( 
    @NotNull
    UserStatus status) {}
