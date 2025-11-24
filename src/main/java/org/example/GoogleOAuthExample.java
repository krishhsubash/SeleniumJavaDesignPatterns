package org.example;

import org.example.oauth.OAuth2PKCEClient;
import org.example.oauth.OAuthConfig;

/**
 * Practical example showing OAuth 2.0 with PKCE for Google OAuth
 * This demonstrates real-world usage patterns
 */
public class GoogleOAuthExample {
    
    public static void main(String[] args) {
        System.out.println("=== Google OAuth 2.0 with PKCE Example ===\n");
        
        // Google OAuth configuration
        // To use this example:
        // 1. Go to Google Cloud Console
        // 2. Create a new project or select existing
        // 3. Enable Google+ API or Gmail API
        // 4. Create OAuth 2.0 credentials
        // 5. Add http://localhost:8080/callback to authorized redirect URIs
        // 6. Replace the client_id below with your actual client ID
        
        OAuthConfig googleConfig = new OAuthConfig(
            "your-google-client-id.apps.googleusercontent.com",  // Replace with your Google client ID
            "",  // No client secret needed for PKCE with public clients
            "https://accounts.google.com/o/oauth2/v2/auth",       // Google authorization endpoint
            "https://oauth2.googleapis.com/token",                // Google token endpoint  
            "http://localhost:8080/callback",                     // Your redirect URI
            "openid email profile"                               // Requested scopes
        );
        
        OAuth2PKCEClient googleClient = new OAuth2PKCEClient(googleConfig);
        
        // Generate authorization URL
        String state = "google-oauth-" + System.currentTimeMillis();
        String authUrl = googleClient.generateAuthorizationUrl(state);
        
        System.out.println("Step 1: Authorization URL");
        System.out.println("Copy and paste this URL into your browser:");
        System.out.println(authUrl);
        System.out.println();
        
        System.out.println("Step 2: Complete the OAuth Flow");
        System.out.println("1. Open the URL above in your browser");
        System.out.println("2. Sign in to your Google account");
        System.out.println("3. Grant permissions to your application");
        System.out.println("4. You'll be redirected to: http://localhost:8080/callback?code=...&state=" + state);
        System.out.println("5. Extract the 'code' parameter from the callback URL");
        System.out.println();
        
        System.out.println("Step 3: Token Exchange (Uncomment code below to use)");
        System.out.println("Once you have the authorization code, uncomment and run:");
        System.out.println("String authCode = \"your-authorization-code-here\";");
        System.out.println("TokenResponse tokens = googleClient.exchangeCodeForToken(authCode);");
        System.out.println();
        
        // Uncomment the following lines and add your authorization code to test
        /*
        System.out.println("Exchanging authorization code for tokens...");
        String authCode = "your-authorization-code-here"; // Replace with actual code
        try {
            TokenResponse tokens = googleClient.exchangeCodeForToken(authCode);
            System.out.println("Success! Received tokens:");
            System.out.println(tokens);
            
            // Test API call to get user info
            Response userInfo = googleClient.makeAuthenticatedRequest(
                "https://www.googleapis.com/oauth2/v2/userinfo", 
                tokens.getAccessToken()
            );
            
            System.out.println("User info from Google:");
            System.out.println(userInfo.getBody().asString());
            
        } catch (Exception e) {
            System.err.println("Error during token exchange: " + e.getMessage());
        }
        */
        
        System.out.println("=== Configuration Notes ===");
        System.out.println("Google OAuth 2.0 Setup:");
        System.out.println("1. Visit: https://console.cloud.google.com/");
        System.out.println("2. Create/select a project");
        System.out.println("3. Enable APIs (Gmail, Google+, etc.)");
        System.out.println("4. Create OAuth 2.0 Client ID credentials");
        System.out.println("5. Set redirect URI: http://localhost:8080/callback");
        System.out.println("6. Use client ID in the configuration above");
        System.out.println();
        
        System.out.println("=== Why PKCE? ===");
        System.out.println("- Prevents authorization code interception attacks");
        System.out.println("- Particularly important for mobile and single-page applications");
        System.out.println("- Eliminates need for client secret in public clients");
        System.out.println("- RFC 7636 standard for OAuth 2.0 security enhancement");
    }
}