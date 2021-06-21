package com.example.authserver.Controllers;

import com.example.authserver.Entities.Otp;
import com.example.authserver.Entities.Role;
import com.example.authserver.Entities.User;
import com.example.authserver.Payload.UserResponse;
import com.example.authserver.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;
import java.util.Set;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/add")
    public ResponseEntity<?> addUser(@RequestBody User user){
        userService.addUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/auth")
    public ResponseEntity<?> authUser(@RequestBody User user){
        if(userService.auth(user)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/otp/check")
    public ResponseEntity<?> checkAuth(@RequestBody Otp otp){
        if(userService.checkAuth(otp)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/user/role")
    public ResponseEntity<?> getUserRole(@RequestBody User user){
        return ResponseEntity.ok(new UserResponse(user.getUsername(), userService.findRoles(user.getUsername())));
    }
}
