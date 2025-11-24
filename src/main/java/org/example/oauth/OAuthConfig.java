package org.example.oauth;

/**
 * Data class to hold OAuth 2.0 configuration parameters
 */
public class OAuthConfig {
    private final String clientId;
    private final String clientSecret;
    private final String authorizationUrl;
    private final String tokenUrl;
    private final String redirectUri;
    private final String scope;
    
    public OAuthConfig(String clientId, String clientSecret, String authorizationUrl, 
                      String tokenUrl, String redirectUri, String scope) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationUrl = authorizationUrl;
        this.tokenUrl = tokenUrl;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }
    
    public String getClientId() { return clientId; }
    public String getClientSecret() { return clientSecret; }
    public String getAuthorizationUrl() { return authorizationUrl; }
    public String getTokenUrl() { return tokenUrl; }
    public String getRedirectUri() { return redirectUri; }
    public String getScope() { return scope; }
}