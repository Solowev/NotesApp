package com.example.authserver.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String code;
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){
        setCreatedAt(LocalDateTime.now());
    }
}
