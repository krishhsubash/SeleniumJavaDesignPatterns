package org.example.oauth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Helper class for generating PKCE (Proof Key for Code Exchange) parameters
 * Used in OAuth 2.0 authorization code flow with PKCE
 */
public class PKCEHelper {
    
    private static final String CODE_VERIFIER_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~";
    private static final int CODE_VERIFIER_LENGTH = 128;
    
    /**
     * Generates a cryptographically random code verifier
     * @return Base64URL-encoded code verifier
     */
    public static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder codeVerifier = new StringBuilder();
        
        for (int i = 0; i < CODE_VERIFIER_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(CODE_VERIFIER_CHARSET.length());
            codeVerifier.append(CODE_VERIFIER_CHARSET.charAt(randomIndex));
        }
        
        return codeVerifier.toString();
    }
    
    /**
     * Generates a code challenge from the code verifier using SHA256
     * @param codeVerifier The code verifier
     * @return Base64URL-encoded code challenge
     */
    public static String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Gets the code challenge method
     * @return "S256" for SHA256
     */
    public static String getCodeChallengeMethod() {
        return "S256";
    }
}