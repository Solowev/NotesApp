package com.example.authserver.Services;

import com.example.authserver.Entities.Otp;
import com.example.authserver.Entities.Role;
import com.example.authserver.Entities.User;
import com.example.authserver.Enums.ERole;
import com.example.authserver.Exceptions.OtpNotFoundException;
import com.example.authserver.Payload.UserResponse;
import com.example.authserver.Repositories.OtpRepository;
import com.example.authserver.Repositories.RoleRepository;
import com.example.authserver.Repositories.UserRepository;
import com.example.authserver.Utils.GenerateCodeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Log
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OtpService otpService;


    public void addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findRoleByRole(ERole.ROLE_USER)
                .orElseThrow(()->new RuntimeException("Role isn't found"));
        user.setRoles(roles);
        userRepository.save(user);
        log.info(String.format("User with username: %s has registered", user.getUsername()));
    }

    public boolean auth(User user){
        User foundUser = userRepository.findUserByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with username " + user.getUsername()));
        if(!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            log.info("User password is invalid");
            return false;
        }
        log.info("User was authenticated with username: " + foundUser.getUsername());
        String code = generateNewOtp(foundUser);
        new Thread(()->sendCode(foundUser.getEmail(), code))
                .start();
        return true;
    }

    public boolean checkAuth(Otp otpToValidate){
        Otp otp = otpRepository.findOtpByUsername(otpToValidate.getUsername())
                .orElseThrow(() -> new OtpNotFoundException("Couldn't find otp for user: " + otpToValidate.getUsername()));
        Duration duration = Duration.between(otp.getUpdatedAt(),LocalDateTime.now());
        if(!(otp.getCode().equals(otpToValidate.getCode()) && duration.toMinutes() < 5)) {
            return false;
        }
        log.info("found code for " + otp.getUsername() + "code user " + otp.getCode() + "otpToValidate " +otpToValidate.getCode());
        return true;
    }

    public Set<Role> findRoles(String username){
        Set<Role> roles = userRepository.findUserByUsername(username)
                .orElseThrow(()->new RuntimeException("couldn't find user"))
                .getRoles();
        return roles;
    }


    public void sendCode(String email, String code){
        otpService.sendOtp(email, code);
    }

    private String generateNewOtp(User user){
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(user.getUsername());
        Otp otp;
        if(userOtp.isPresent()){
            otp = userOtp.get();
        } else{
            otp = new Otp();
            otp.setUsername(user.getUsername());
        }
        otp.setCode(code);
        otpRepository.save(otp);
        return code;
    }


}
