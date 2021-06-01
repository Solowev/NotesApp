package com.example.mainserver.Authentication.Proxy;

import com.example.mainserver.Authentication.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationServerProxy {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.server.url}")
    private String BASE_URL;

    public void sendAuth(String username, String password){
        String url = BASE_URL.concat("/user/auth");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        HttpEntity<User> request = new HttpEntity<>(user);
        try{
            restTemplate.postForEntity(url, request, Void.class);
        } catch(RestClientException e){
            e.printStackTrace();
        }
    }

    public boolean sendOTP(String username, String code){
        String url = BASE_URL.concat("/otp/check");
        User user = new User();
        user.setUsername(username);
        user.setCode(code);
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }

}
