package com.auth_service.repository;

import com.auth_service.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<UserAccount,Integer> {

    Optional<UserAccount> findByLoginId(String loginId);

    Optional<UserAccount> findByToken(String token);
}
