package com.example.mainserver.Authentication.Models;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private String username;
    private String password;
    private String code;
    private String email;
    private Set<Role> roles;
}
