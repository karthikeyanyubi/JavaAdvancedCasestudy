package com.auth_service.controller;


import com.auth_service.dto.UserLogoutDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.model.UserAccount;
import com.auth_service.service.LoginServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private LoginServiceInterface loginService;

    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> signUpUser(@Valid @RequestBody UserAccount user) {
        UserResponseDto signedUpUser = loginService.signUp(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(signedUpUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody UserAccount user) {
        UserResponseDto loggedInUser = loginService.login(user);
        return ResponseEntity.status(HttpStatus.OK).body(loggedInUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<UserResponseDto> logout(@Valid @RequestBody UserLogoutDto user) {
        UserResponseDto logOutUser = loginService.logout(user);
        return ResponseEntity.status(HttpStatus.OK).body(logOutUser);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<UserResponseDto> validateToken(@RequestHeader("token") String token) {
        UserResponseDto validatedUser = loginService.validateToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(validatedUser);
    }

}
