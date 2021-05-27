package com.example.authserver.Utils;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.security.SecureRandom;

@NoArgsConstructor
@Log
public final class GenerateCodeUtil {

    public static String generateCode() {
        SecureRandom random = new SecureRandom();
        String code = String.valueOf(random.nextInt(9000) + 1000);
        log.info("new generated value: " + code);
        return code;
    }
}
