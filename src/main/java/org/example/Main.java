package org.example;

/**
 * Main class - Choose which example to run
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Java REST Assured OAuth 2.0 with PKCE Project ===\n");
        
        System.out.println("Available examples:");
        System.out.println("1. OAuth 2.0 with PKCE Example");
        System.out.println("2. PKCE Helper Demonstration");
        System.out.println();
        
        // Run the OAuth PKCE example
        System.out.println("Running OAuth 2.0 with PKCE Example:\n");
        OAuth2PKCEExample.main(args);
        
        System.out.println("\n" + "=".repeat(50));
        
        // Run the PKCE helper demonstration  
        System.out.println("Running PKCE Helper Demonstration:\n");
        PKCEHelperDemo.main(args);
    }
}