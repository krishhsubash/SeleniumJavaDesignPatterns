package org.example;

import org.example.oauth.OAuth2PKCEClient;
import org.example.oauth.OAuthConfig;

/**
 * Complete example of OAuth 2.0 with PKCE flow using REST Assured
 * This example demonstrates the complete OAuth flow including:
 * 1. PKCE parameter generation
 * 2. Authorization URL generation  
 * 3. Token exchange
 * 4. Authenticated API calls
 * 5. Token refresh
 */
public class OAuth2PKCEExample {
    
    public static void main(String[] args) {
        // Example OAuth configuration (replace with your actual OAuth provider details)
        OAuthConfig config = new OAuthConfig(
            "your-client-id",                    // Client ID from your OAuth provider
            "your-client-secret",                // Client Secret (optional for public clients)
            "https://auth.example.com/oauth/authorize", // Authorization endpoint
            "https://auth.example.com/oauth/token",     // Token endpoint
            "http://localhost:8080/callback",           // Redirect URI
            "read write"                                // Requested scopes
        );
        
        System.out.println("=== OAuth 2.0 with PKCE Example ===\n");
        
        // Create OAuth client
        OAuth2PKCEClient oauthClient = new OAuth2PKCEClient(config);
        
        // Step 1: Generate PKCE parameters and authorization URL
        demonstratePKCEGeneration(oauthClient);
        
        // Step 2: Show how to handle the authorization flow
        demonstrateAuthorizationFlow(oauthClient);
        
        // Step 3: Example token exchange (this would use real authorization code in practice)
        demonstrateTokenExchange(oauthClient);
        
        // Step 4: Example token refresh
        demonstrateTokenRefresh(oauthClient);
        
        // Step 5: Example authenticated API call
        demonstrateAuthenticatedRequest(oauthClient);
        
        System.out.println("\n=== OAuth 2.0 with PKCE Example Complete ===");
    }
    
    private static void demonstratePKCEGeneration(OAuth2PKCEClient client) {
        System.out.println("1. PKCE Parameter Generation:");
        System.out.println("   Code Verifier: " + client.getCodeVerifier());
        System.out.println("   Code Challenge: " + client.getCodeChallenge());
        System.out.println("   Challenge Method: S256\n");
    }
    
    private static void demonstrateAuthorizationFlow(OAuth2PKCEClient client) {
        System.out.println("2. Authorization URL Generation:");
        String state = "random-state-" + System.currentTimeMillis();
        String authUrl = client.generateAuthorizationUrl(state);
        System.out.println("   Authorization URL:");
        System.out.println("   " + authUrl);
        System.out.println("   \n   Instructions:");
        System.out.println("   - Open this URL in a browser");
        System.out.println("   - Complete the login process");
        System.out.println("   - You'll be redirected to your redirect_uri with a 'code' parameter");
        System.out.println("   - Extract the authorization code from the callback URL\n");
    }
    
    private static void demonstrateTokenExchange(OAuth2PKCEClient client) {
        System.out.println("3. Token Exchange Example:");
        System.out.println("   Note: This would normally use a real authorization code from the callback");
        System.out.println("   For demonstration, we're showing what the request would look like:");
        System.out.println("   \n   Example usage:");
        System.out.println("   String authCode = \"authorization-code-from-callback\";");
        System.out.println("   TokenResponse tokens = client.exchangeCodeForToken(authCode);");
        System.out.println("   \n   This would make a POST request to the token endpoint with:");
        System.out.println("   - grant_type: authorization_code");
        System.out.println("   - client_id: " + "your-client-id");
        System.out.println("   - code: [authorization code]");
        System.out.println("   - redirect_uri: " + "http://localhost:8080/callback");
        System.out.println("   - code_verifier: " + client.getCodeVerifier().substring(0, 20) + "...");
        System.out.println();
    }
    
    private static void demonstrateTokenRefresh(OAuth2PKCEClient client) {
        System.out.println("4. Token Refresh Example:");
        System.out.println("   Example usage:");
        System.out.println("   String refreshToken = \"your-refresh-token\";");
        System.out.println("   TokenResponse newTokens = client.refreshToken(refreshToken);");
        System.out.println("   \n   This would make a POST request with:");
        System.out.println("   - grant_type: refresh_token");
        System.out.println("   - client_id: " + "your-client-id");
        System.out.println("   - refresh_token: [refresh token]");
        System.out.println();
    }
    
    private static void demonstrateAuthenticatedRequest(OAuth2PKCEClient client) {
        System.out.println("5. Authenticated API Request Example:");
        System.out.println("   Example usage:");
        System.out.println("   String accessToken = \"your-access-token\";");
        System.out.println("   Response response = client.makeAuthenticatedRequest(\"https://api.example.com/user\", accessToken);");
        System.out.println("   \n   This would make a GET request with:");
        System.out.println("   - Authorization: Bearer [access token]");
        System.out.println("   - Accept: application/json");
        System.out.println();
    }
    
    /**
     * Example method showing how to handle a complete OAuth flow with real tokens
     * Uncomment and modify this method to use with a real OAuth provider
     */
    /*
    private static void completeOAuthFlow() {
        OAuthConfig config = new OAuthConfig(
            "your-real-client-id",
            "your-real-client-secret", 
            "https://your-oauth-provider.com/oauth/authorize",
            "https://your-oauth-provider.com/oauth/token",
            "http://localhost:8080/callback",
            "read"
        );
        
        OAuth2PKCEClient client = new OAuth2PKCEClient(config);
        
        // Step 1: Get authorization URL
        String authUrl = client.generateAuthorizationUrl("my-state-123");
        System.out.println("Visit: " + authUrl);
        
        // Step 2: User completes auth and you get the code from callback
        // String authCode = "code-from-callback-url";
        
        // Step 3: Exchange code for tokens
        // TokenResponse tokens = client.exchangeCodeForToken(authCode);
        // System.out.println("Access Token: " + tokens.getAccessToken());
        
        // Step 4: Make authenticated requests
        // Response apiResponse = client.makeAuthenticatedRequest("https://api.example.com/user", tokens.getAccessToken());
        // System.out.println("API Response: " + apiResponse.getBody().asString());
        
        // Step 5: Refresh token when needed
        // if (tokens.getRefreshToken() != null) {
        //     TokenResponse refreshedTokens = client.refreshToken(tokens.getRefreshToken());
        //     System.out.println("New Access Token: " + refreshedTokens.getAccessToken());
        // }
    }
    */
}