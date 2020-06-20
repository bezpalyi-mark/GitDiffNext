package com.diffreviewer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword implements PasswordEncoder {

    public static final Logger LOGGER = LoggerFactory.getLogger(HashPassword.class);

    public HashPassword() {
    }

    @Override
    public String encode(CharSequence charSequence) {
        MessageDigest sha256 = null;    // Chose algorithm
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return "";
        }

        byte[] bytes = sha256.digest(charSequence.toString().getBytes()); // Get hash code
        StringBuilder builder = new StringBuilder();        // Convert to 16 number system
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }

        return builder.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return false;
        }

        byte[] bytes = sha256.digest(charSequence.toString().getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }

        return builder.toString().equals(s);
    }
}
