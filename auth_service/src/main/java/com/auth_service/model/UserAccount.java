package com.auth_service.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Getter
@Setter
@Table(name = "login")
public class UserAccount {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(unique = true, name = "user_login_id")
    @NotBlank(message = "Login ID cannot be blank")
    private String loginId;

    @Column(name = "user_pass")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Hidden
    @Column(name = "token")
    private String token;


    @Hidden
    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    @JsonSerialize(using = DateSerializer.class)
    Date createdDate;

    @Hidden
    @UpdateTimestamp
    @Column(name = "last_modified_date", nullable = false)
    @JsonSerialize(using = DateSerializer.class)
    Date lastModifiedDate;

    @Hidden
    @Column(name = "token_creation_time")
    @JsonSerialize(using = DateSerializer.class)
    LocalDateTime tokenCreationTime;

}