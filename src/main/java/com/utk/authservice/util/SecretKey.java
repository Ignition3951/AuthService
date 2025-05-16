package com.utk.authservice.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKey {

    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] secretBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(secretBytes);
        String secret = Base64.getEncoder().encodeToString(secretBytes);
        System.out.println("Generated HS256 Secret: " + secret);
    }
}
