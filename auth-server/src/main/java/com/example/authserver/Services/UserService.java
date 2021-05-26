package com.example.authserver.Services;

import com.example.authserver.Entities.Otp;
import com.example.authserver.Entities.User;
import com.example.authserver.Repositories.OtpRepository;
import com.example.authserver.Repositories.UserRepository;
import com.example.authserver.Utils.GenerateCodeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public void addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info(String.format("User with username: %s has registered", user.getUsername()));
    }

    public void auth(User user){
        Optional<User> optionalUser = userRepository.findUserByUsername(user.getUsername());
        if(!optionalUser.isPresent()){
            log.info("Couldn't find user with username: " + user.getUsername());
            throw new BadCredentialsException("Bad credentials");
        }
        User foundUser = optionalUser.get();
        if(passwordEncoder.matches(user.getPassword(), foundUser.getPassword())){
            log.info("User was authenticated with username: " + foundUser.getUsername());
            generateNewOtp(foundUser);
        } else{
            log.info(String.format("User was found with username %s, but with wrong password",user.getUsername()));
            throw new BadCredentialsException("Bad credentials");
        }
    }

    public boolean checkAuth(Otp otpToValidate){
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());
        if(userOtp.isPresent()) {
            Otp otp = userOtp.get();
            if(otp.getCode().equals(otpToValidate.getCode()))
                return true;
        }
        return false;
    }

    private void generateNewOtp(User user){
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(user.getUsername());
        if(userOtp.isPresent()){
            Otp otp = userOtp.get();
            otp.setCode(code);
        } else{
            Otp otp = new Otp();
            otp.setUsername(user.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
    }


}
