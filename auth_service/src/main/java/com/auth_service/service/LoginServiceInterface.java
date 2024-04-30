package com.auth_service.service;

import com.auth_service.dto.UserLogoutDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.model.UserAccount;

public interface LoginServiceInterface {

    public UserResponseDto signUp(UserAccount user);

    public UserResponseDto login(UserAccount user);

    public UserResponseDto validateToken(String token);

    public UserResponseDto logout(UserLogoutDto user);
}
