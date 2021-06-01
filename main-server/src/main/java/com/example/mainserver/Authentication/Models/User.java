package com.example.mainserver.Authentication.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class User {
    private String username;
    private String password;
    private String code;
    private String Email;
}
