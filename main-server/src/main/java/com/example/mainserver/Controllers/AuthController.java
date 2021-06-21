package com.example.mainserver.Controllers;

import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class AuthController {

    @GetMapping("/signin")
    public String authenticateWithUsernamePassword(){
        log.info("doing controller");
        return "test";
    }
}
