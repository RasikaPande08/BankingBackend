package com.example.banking.ivr;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpCallService {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }
    
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType){
    	return restTemplate.exchange(url, HttpMethod.POST, null, responseType);
    }


}
