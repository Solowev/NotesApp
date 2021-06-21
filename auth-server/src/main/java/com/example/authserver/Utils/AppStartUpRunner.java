package com.example.authserver.Utils;

import com.example.authserver.Entities.Role;
import com.example.authserver.Enums.ERole;
import com.example.authserver.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppStartUpRunner implements ApplicationRunner {


    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        roleRepository.saveAll(List.of(new Role(ERole.ROLE_USER), new Role(ERole.ROLE_ADMIN)));
    }
}
