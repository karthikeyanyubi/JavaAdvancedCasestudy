package com.auth_service.service;


import com.auth_service.dto.UserLogoutDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.model.UserAccount;
import com.auth_service.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginServiceImplementation implements LoginServiceInterface {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @Override
    public UserResponseDto signUp(UserAccount user) {


        // Encrypt the password with Bcrypt
        String loginIdLower = user.getLoginId().toLowerCase();
        Optional<UserAccount> fetchUser = loginRepository.findByLoginId(loginIdLower);

        if(fetchUser.isPresent())
        {
            throw new RuntimeException("User already signed up");
        }
        else
        {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            // Save the user to the database
            user.setPassword(encodedPassword);

            loginRepository.save(user);

            // Create and return the response DTO
            UserResponseDto responseDto = new UserResponseDto();
            responseDto.setMessage("User created successfully");

            return responseDto;
        }
    }

    @Override
    public UserResponseDto login(UserAccount user) {
        String loginIdLower = user.getLoginId().toLowerCase();
        Optional<UserAccount>fetchUser = loginRepository.findByLoginId(loginIdLower);
        if(fetchUser.isEmpty())
        {
            throw new RuntimeException("User not found");
        }
        else {
            UserAccount userAccount = fetchUser.get();

            // Check if the password matches
            boolean isPasswordMatched = passwordEncoder.matches(user.getPassword(), userAccount.getPassword());
            LocalDateTime currentTime = LocalDateTime.now();

            if (isPasswordMatched) {
                // Generate a token
                UUID token = UUID.randomUUID();

                // Save the token to the database
                userAccount.setToken(token.toString());
                userAccount.setTokenCreationTime(currentTime);

                loginRepository.save(userAccount);

                // Create and return the response DTO
                UserResponseDto responseDto = new UserResponseDto();
                responseDto.setMessage("User logged in successfully");
                responseDto.setLoginId(userAccount.getLoginId());
                responseDto.setToken(token.toString());

                return responseDto;
            } else {
                throw new RuntimeException("Password do not match");
            }
        }
    }



    @Override
    public UserResponseDto validateToken(String token) {
        LocalDateTime currentTime = LocalDateTime.now();
        String tokenLower = token.toLowerCase();
        if(tokenLower==null || tokenLower.trim().isBlank())
            throw new RuntimeException("Token should be provided");
        Optional<UserAccount>fetchUser = loginRepository.findByToken(token);
        if(fetchUser.isEmpty())
            throw new RuntimeException("Not a valid token! Please login again");
        LocalDateTime tokenCreationTime = fetchUser.get().getTokenCreationTime();
        Duration duration = Duration.between(currentTime, tokenCreationTime); // Reversed the order of parameters
        long minutesDifference = Math.abs(duration.toMinutes());
        if (minutesDifference>30) {
            throw new RuntimeException("Token Expired! Please login again");
        }
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setMessage("Authentication Successful");
        return responseDto;
    }

    @Override
    public UserResponseDto logout(UserLogoutDto user) {
        String loginIdLower = user.getLoginId().toLowerCase();
        Optional<UserAccount>fetchUser = loginRepository.findByLoginId(loginIdLower);
        if(fetchUser.isEmpty())
        {
            throw new RuntimeException("User not found");
        }
        else {
            UserAccount userAccount = fetchUser.get();
            userAccount.setToken("");
            // Save the modified user account back to the repository
            loginRepository.save(userAccount);
            UserResponseDto responseDto = new UserResponseDto();
            responseDto.setMessage("User logged out successfully");
            return responseDto;
        }
    }
}
