package com.example.authserver.Entities;

import com.example.authserver.Enums.ERole;
import lombok.*;

import javax.persistence.*;


@Entity(name="roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private ERole role;

    public Role(ERole role){
        this.role = role;
    }

}
