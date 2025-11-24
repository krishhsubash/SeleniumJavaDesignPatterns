package org.example;

import org.example.oauth.OAuth2PKCEClient;
import org.example.oauth.OAuthConfig;
import org.example.oauth.PKCEHelper;

/**
 * Test class to validate OAuth 2.0 with PKCE implementation
 */
public class OAuth2PKCETests {
    
    public static void main(String[] args) {
        System.out.println("=== OAuth 2.0 with PKCE Tests ===\n");
        
        runPKCETests();
        runOAuthConfigTests();
        runAuthUrlGenerationTests();
        
        System.out.println("=== All Tests Completed ===");
    }
    
    private static void runPKCETests() {
        System.out.println("1. PKCE Helper Tests:");
        
        // Test code verifier generation
        String verifier1 = PKCEHelper.generateCodeVerifier();
        String verifier2 = PKCEHelper.generateCodeVerifier();
        
        System.out.println("   ✓ Code verifiers are unique: " + !verifier1.equals(verifier2));
        System.out.println("   ✓ Code verifier length is 128: " + (verifier1.length() == 128));
        System.out.println("   ✓ Code verifier uses safe characters: " + 
                verifier1.matches("[A-Za-z0-9\\-._~]+"));
        
        // Test code challenge generation
        String challenge1 = PKCEHelper.generateCodeChallenge(verifier1);
        String challenge2 = PKCEHelper.generateCodeChallenge(verifier1);
        
        System.out.println("   ✓ Same verifier produces same challenge: " + challenge1.equals(challenge2));
        System.out.println("   ✓ Challenge method is S256: " + "S256".equals(PKCEHelper.getCodeChallengeMethod()));
        
        // Test with known values (RFC 7636 example)
        String testVerifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
        String expectedChallenge = "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM";
        String actualChallenge = PKCEHelper.generateCodeChallenge(testVerifier);
        System.out.println("   ✓ RFC 7636 test vector passes: " + expectedChallenge.equals(actualChallenge));
        
        System.out.println();
    }
    
    private static void runOAuthConfigTests() {
        System.out.println("2. OAuth Configuration Tests:");
        
        OAuthConfig config = new OAuthConfig(
            "test-client-id",
            "test-client-secret", 
            "https://auth.example.com/oauth/authorize",
            "https://auth.example.com/oauth/token",
            "http://localhost:8080/callback",
            "read write"
        );
        
        System.out.println("   ✓ Client ID: " + config.getClientId());
        System.out.println("   ✓ Authorization URL: " + config.getAuthorizationUrl());
        System.out.println("   ✓ Token URL: " + config.getTokenUrl());
        System.out.println("   ✓ Redirect URI: " + config.getRedirectUri());
        System.out.println("   ✓ Scope: " + config.getScope());
        System.out.println();
    }
    
    private static void runAuthUrlGenerationTests() {
        System.out.println("3. Authorization URL Generation Tests:");
        
        OAuthConfig config = new OAuthConfig(
            "test-client",
            "",
            "https://auth.example.com/authorize",
            "https://auth.example.com/token",
            "http://localhost:8080/callback",
            "openid profile"
        );
        
        OAuth2PKCEClient client = new OAuth2PKCEClient(config);
        String authUrl = client.generateAuthorizationUrl("test-state-123");
        
        System.out.println("   Generated Authorization URL:");
        System.out.println("   " + authUrl);
        System.out.println();
        
        // Validate URL components
        System.out.println("   URL Validation:");
        System.out.println("   ✓ Contains response_type=code: " + authUrl.contains("response_type=code"));
        System.out.println("   ✓ Contains client_id: " + authUrl.contains("client_id=test-client"));
        System.out.println("   ✓ Contains redirect_uri: " + authUrl.contains("redirect_uri="));
        System.out.println("   ✓ Contains scope: " + authUrl.contains("scope="));
        System.out.println("   ✓ Contains code_challenge: " + authUrl.contains("code_challenge="));
        System.out.println("   ✓ Contains code_challenge_method=S256: " + authUrl.contains("code_challenge_method=S256"));
        System.out.println("   ✓ Contains state: " + authUrl.contains("state=test-state-123"));
        System.out.println();
        
        // Show PKCE parameters
        System.out.println("   PKCE Parameters for this session:");
        System.out.println("   Code Verifier: " + client.getCodeVerifier());
        System.out.println("   Code Challenge: " + client.getCodeChallenge());
        System.out.println();
    }
}