package com.goodlab.server.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password hashing helper.
 *
 * <p>New passwords use salted PBKDF2-HMAC-SHA256. Legacy 32-character MD5
 * digests are accepted only so an existing account can be upgraded after a
 * successful login.</p>
 */
public final class PasswordUtil {
    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }

        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] hash = derive(rawPassword.toCharArray(), salt, ITERATIONS);
        return String.join("$",
                PREFIX,
                Integer.toString(ITERATIONS),
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(hash));
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        if (encodedPassword.startsWith(PREFIX + "$")) {
            String[] parts = encodedPassword.split("\\$", -1);
            if (parts.length != 4) {
                return false;
            }
            try {
                int iterations = Integer.parseInt(parts[1]);
                byte[] salt = Base64.getDecoder().decode(parts[2]);
                byte[] expected = Base64.getDecoder().decode(parts[3]);
                byte[] actual = derive(rawPassword.toCharArray(), salt, iterations);
                return MessageDigest.isEqual(expected, actual);
            } catch (IllegalArgumentException exception) {
                return false;
            }
        }

        return encodedPassword.matches("[0-9a-fA-F]{32}")
                && MessageDigest.isEqual(
                        encodedPassword.toLowerCase().getBytes(StandardCharsets.US_ASCII),
                        legacyMd5(rawPassword).getBytes(StandardCharsets.US_ASCII));
    }

    public static boolean needsRehash(String encodedPassword) {
        return encodedPassword == null || !encodedPassword.startsWith(PREFIX + "$" + ITERATIONS + "$");
    }

    private static byte[] derive(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (Exception exception) {
            throw new IllegalStateException("PBKDF2 is unavailable", exception);
        } finally {
            spec.clearPassword();
        }
    }

    private static String legacyMd5(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte item : digest) {
                hex.append(String.format("%02x", item & 0xff));
            }
            return hex.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Legacy password verification is unavailable", exception);
        }
    }
}
