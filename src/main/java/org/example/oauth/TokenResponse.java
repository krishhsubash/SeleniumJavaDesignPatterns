package org.example.oauth;

/**
 * Data class to hold token response from OAuth provider
 */
public class TokenResponse {
    private final String accessToken;
    private final String tokenType;
    private final String refreshToken;
    private final long expiresIn;
    private final String scope;
    
    public TokenResponse(String accessToken, String tokenType, String refreshToken, 
                        long expiresIn, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }
    
    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public String getRefreshToken() { return refreshToken; }
    public long getExpiresIn() { return expiresIn; }
    public String getScope() { return scope; }
    
    @Override
    public String toString() {
        return "TokenResponse{" +
                "accessToken='" + (accessToken != null ? accessToken.substring(0, Math.min(10, accessToken.length())) + "..." : "null") + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", refreshToken='" + (refreshToken != null ? refreshToken.substring(0, Math.min(10, refreshToken.length())) + "..." : "null") + '\'' +
                ", expiresIn=" + expiresIn +
                ", scope='" + scope + '\'' +
                '}';
    }
}