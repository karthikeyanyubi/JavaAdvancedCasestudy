package com.auth_service.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLogoutDto {
    @Column(unique = true, name = "user_login_id")
    @NotBlank(message = "Login ID cannot be blank")
    private String loginId;

}
