package com.example.mainserver.Authentication.Proxy;

import com.example.mainserver.Authentication.Models.ERole;
import com.example.mainserver.Authentication.Models.Role;
import com.example.mainserver.Authentication.Models.User;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Log
public class AuthenticationServerProxy {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.server.url}")
    private String BASE_URL;

    public boolean sendAuth(String username, String password){
        String url = BASE_URL.concat("/user/auth");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        HttpEntity<User> request = new HttpEntity<>(user);
        try{
            ResponseEntity<?> response = restTemplate.postForEntity(url, request, Void.class);
            return response.getStatusCode().equals(HttpStatus.OK);
        } catch(RestClientException ex){
            log.warning(ex.getMessage());
        }
        return false;
    }
    public Set<Role> getUserRoles(String username){
        String url = BASE_URL.concat("/user/role");

        User user = new User();
        user.setUsername(username);
        HttpEntity<User> request = new HttpEntity<>(user);

        Set<Role> roles = new HashSet<>();

        try{
            ResponseEntity<User> response = restTemplate.postForEntity(url, request, User.class);
            if(response.getStatusCode().equals(HttpStatus.OK)){
                roles = response.getBody().getRoles();
            } else{
                throw new RuntimeException("Couldn't find role for user " + username);
            }
        }catch(RestClientException ex){
            ex.printStackTrace();
        }
        return roles;
    }

    public boolean sendOTP(String username, String code){
        String url = BASE_URL.concat("/otp/check");

        User user = new User();
        user.setUsername(username);
        user.setCode(code);
        HttpEntity<User> request = new HttpEntity<>(user);

        try {
            ResponseEntity<User> response = restTemplate.postForEntity(url, request, User.class);
            return response.getStatusCode().equals(HttpStatus.OK);
        }catch(RestClientException ex){
            ex.printStackTrace();
        }
        return false;
    }

}
