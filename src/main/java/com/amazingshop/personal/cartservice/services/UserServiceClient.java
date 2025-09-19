package com.amazingshop.personal.cartservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    @Value("user.service.url:http://localhost:8081}")
    private String userServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean userExists(Long userId){
        try {
            String url = userServiceUrl + "/api/v1/users/admin/" + userId;
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

            return response.getStatusCode().is2xxSuccessful();
        }catch (Exception e){
            return false;
        }
    }
}