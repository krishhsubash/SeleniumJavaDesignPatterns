package org.example;

import org.example.oauth.PKCEHelper;

/**
 * Demonstration of PKCE (Proof Key for Code Exchange) parameter generation
 */
public class PKCEHelperDemo {
    
    public static void main(String[] args) {
        System.out.println("=== PKCE Helper Demonstration ===\n");
        
        // Generate multiple PKCE parameter sets to show randomness
        for (int i = 1; i <= 3; i++) {
            System.out.println("PKCE Parameter Set #" + i + ":");
            
            // Generate code verifier
            String codeVerifier = PKCEHelper.generateCodeVerifier();
            System.out.println("  Code Verifier: " + codeVerifier);
            System.out.println("  Verifier Length: " + codeVerifier.length());
            
            // Generate code challenge
            String codeChallenge = PKCEHelper.generateCodeChallenge(codeVerifier);
            System.out.println("  Code Challenge: " + codeChallenge);
            System.out.println("  Challenge Method: " + PKCEHelper.getCodeChallengeMethod());
            
            // Show character composition
            System.out.println("  Verifier contains only safe chars: " + 
                    codeVerifier.matches("[A-Za-z0-9\\-._~]+"));
            
            System.out.println();
        }
        
        // Demonstrate that the same verifier always produces the same challenge
        System.out.println("Consistency Test:");
        String testVerifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
        String challenge1 = PKCEHelper.generateCodeChallenge(testVerifier);
        String challenge2 = PKCEHelper.generateCodeChallenge(testVerifier);
        System.out.println("  Same verifier produces same challenge: " + challenge1.equals(challenge2));
        System.out.println("  Test Verifier: " + testVerifier);
        System.out.println("  Generated Challenge: " + challenge1);
        
        System.out.println("\n=== PKCE Security Information ===");
        System.out.println("- Code Verifier: 128 character random string");
        System.out.println("- Allowed characters: A-Z, a-z, 0-9, -, ., _, ~");
        System.out.println("- Code Challenge: SHA256 hash of verifier, Base64URL encoded");
        System.out.println("- Challenge Method: S256 (SHA256)");
        System.out.println("- Purpose: Prevents authorization code interception attacks");
    }
}