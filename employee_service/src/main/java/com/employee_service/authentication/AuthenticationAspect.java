package com.employee_service.authentication;

import com.employee_service.constants.ConstantService;
import com.employee_service.dto.ResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class AuthenticationAspect {

    private final RestTemplate restTemplate;

    @Autowired
    public AuthenticationAspect(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Before("execution(* com.employee_service.controller.*.*(..))")
    public void authenticateBeforeMethodCall() throws JsonProcessingException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // Get the token from the request header
        String token = request.getHeader("token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token); // Set token in the header
        headers.set("accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ResponseDto> responseEntity = restTemplate.exchange(
                ConstantService.getAUTH_SERVICE_URL() + ConstantService.getAUTH_SERVICE_TOKEN_VALIDATION_ENDPOINT(),
                HttpMethod.POST,
                requestEntity,
                ResponseDto.class
        );
        // Check response status
        ObjectMapper objectMapper = new ObjectMapper();
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            ResponseDto receivedResponse = responseEntity.getBody();
            System.out.println("Hi");
            System.out.println(receivedResponse.getError());
            throw new RuntimeException("Authentication failed: " + receivedResponse.getError());
        }
    }
}
