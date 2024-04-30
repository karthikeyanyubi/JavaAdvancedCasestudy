package com.employee_service.constants;


import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ConstantService {

    @Getter
    private static final String AUTH_SERVICE_TOKEN_VALIDATION_ENDPOINT = "auth/validateToken";

    @Getter
    private static final String AUTH_SERVICE_URL = "http://auth-service/";
}
