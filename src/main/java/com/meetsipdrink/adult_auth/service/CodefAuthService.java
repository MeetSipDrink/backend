package com.meetsipdrink.adult_auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class CodefAuthService {

    @Value("${codef.client-id}")
    private String clientId;

    @Value("${codef.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        String tokenUrl = "https://oauth.codef.io/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Base64 인코딩된 Authorization 헤더 추가
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        // Body에 grant_type 추가 (client_credentials 방식)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        System.out.println("Requesting Token from URL: " + tokenUrl);
        System.out.println("Request Body: " + body);
        System.out.println("Request Headers: " + headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});

            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("access_token")) {
                return (String) responseBody.get("access_token");
            } else {
                throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Error occurred: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            System.out.println("Request URL: " + tokenUrl);
            System.out.println("Request Body: " + body);
            System.out.println("Request Headers: " + headers);
            throw e;
        }
    }
}
