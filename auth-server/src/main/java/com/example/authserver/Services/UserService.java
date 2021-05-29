package com.example.authserver.Services;

import com.example.authserver.Entities.Otp;
import com.example.authserver.Entities.User;
import com.example.authserver.Enums.Role;
import com.example.authserver.Repositories.OtpRepository;
import com.example.authserver.Repositories.UserRepository;
import com.example.authserver.Utils.GenerateCodeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

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
    private OtpService otpService;


    public void addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        log.info(String.format("User with username: %s has registered", user.getUsername()));
    }

    public void auth(User user){
        Optional<User> optionalUser = userRepository.findUserByUsername(user.getUsername());
        if(optionalUser.isEmpty()){
            log.info("Couldn't find user with username: " + user.getUsername());
            throw new BadCredentialsException("Unknown user " + user.getUsername());
        }
        User foundUser = optionalUser.get();
        if(passwordEncoder.matches(user.getPassword(), foundUser.getPassword())){
            log.info("User was authenticated with username: " + foundUser.getUsername());
            String code = generateNewOtp(foundUser.getUsername());
            sendMail(foundUser.getEmail(), code);

        } else{
            log.info(String.format("User was found with username %s, but with wrong password",user.getUsername()));
            throw new BadCredentialsException("Bad credentials");
        }
    }

    public boolean checkAuth(Otp otpToValidate){
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());
        log.info(otpToValidate.getUsername() + otpToValidate.getCode());
        if(userOtp.isPresent()) {
            Otp otp = userOtp.get();
            Duration duration = Duration.between(otp.getUpdatedAt(),LocalDateTime.now());
            if(duration.toMinutes() >= 5){
                log.info("user's code has expired");
                return false;
            }
            if(otp.getCode().equals(otpToValidate.getCode())) {
                log.info("found code for " + otp.getUsername() + "code user " + otp.getCode() + "otpToValidate " +otpToValidate.getCode());
                return true;
            }
        }
        log.info("user not found");
        return false;
    }

    protected void sendMail(String email, String code){
        otpService.sendOtp(email, code);
    }

    protected String generateNewOtp(String username){
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(username);
        Otp otp;
        if(userOtp.isPresent()){
            otp = userOtp.get();
        } else{
            otp = new Otp();
            otp.setUsername(username);
        }
        otp.setCode(code);
        otpRepository.save(otp);
        return code;
    }


}
