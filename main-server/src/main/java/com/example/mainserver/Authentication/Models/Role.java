package com.example.mainserver.Authentication.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role {
    private Long id;
    private ERole role;

    public Role(ERole role){
        this.role = role;
    }
}
