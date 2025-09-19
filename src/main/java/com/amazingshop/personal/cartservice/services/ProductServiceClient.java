package com.amazingshop.personal.cartservice.services;

import com.amazingshop.personal.cartservice.dto.requests.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceClient {

    @Value("product.service.url:http://localhost:8082}")
    private String productServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ProductDTO productExists(Long productId){
        try {
            String url = productServiceUrl + "/api/v1/products/" + productId;
            ResponseEntity<ProductDTO> response = restTemplate.getForEntity(url, ProductDTO.class);
            return response.getBody();
        }catch (Exception e){
            return null;
        }
    }
}