package com.example.authserver.Repositories;

import com.example.authserver.Entities.Role;
import com.example.authserver.Entities.User;
import com.example.authserver.Enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRole(ERole role);
}
