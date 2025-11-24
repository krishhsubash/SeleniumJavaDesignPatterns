package org.example.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

/**
 * OAuth 2.0 with PKCE client implementation using REST Assured
 */
public class OAuth2PKCEClient {
    
    private final OAuthConfig config;
    private String codeVerifier;
    private String codeChallenge;
    
    public OAuth2PKCEClient(OAuthConfig config) {
        this.config = config;
        generatePKCEParameters();
    }
    
    /**
     * Generate PKCE parameters (code verifier and challenge)
     */
    private void generatePKCEParameters() {
        this.codeVerifier = PKCEHelper.generateCodeVerifier();
        this.codeChallenge = PKCEHelper.generateCodeChallenge(codeVerifier);
    }
    
    /**
     * Generate the authorization URL for the OAuth flow
     * @param state Optional state parameter for CSRF protection
     * @return Authorization URL
     */
    public String generateAuthorizationUrl(String state) {
        try {
            StringBuilder url = new StringBuilder(config.getAuthorizationUrl());
            url.append("?response_type=code");
            url.append("&client_id=").append(URLEncoder.encode(config.getClientId(), StandardCharsets.UTF_8.toString()));
            url.append("&redirect_uri=").append(URLEncoder.encode(config.getRedirectUri(), StandardCharsets.UTF_8.toString()));
            url.append("&scope=").append(URLEncoder.encode(config.getScope(), StandardCharsets.UTF_8.toString()));
            url.append("&code_challenge=").append(URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8.toString()));
            url.append("&code_challenge_method=").append(PKCEHelper.getCodeChallengeMethod());
            
            if (state != null && !state.isEmpty()) {
                url.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8.toString()));
            }
            
            return url.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }
    
    /**
     * Exchange authorization code for access token using PKCE
     * @param authorizationCode The authorization code received from the authorization server
     * @return TokenResponse containing access token and other details
     */
    public TokenResponse exchangeCodeForToken(String authorizationCode) {
        System.out.println("Exchanging authorization code for access token...");
        
        Response response = given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .formParam("grant_type", "authorization_code")
                .formParam("client_id", config.getClientId())
                .formParam("client_secret", config.getClientSecret())
                .formParam("code", authorizationCode)
                .formParam("redirect_uri", config.getRedirectUri())
                .formParam("code_verifier", codeVerifier)
                .when()
                .post(config.getTokenUrl())
                .then()
                .log().all()
                .extract()
                .response();
        
        if (response.getStatusCode() == 200) {
            return parseTokenResponse(response);
        } else {
            throw new RuntimeException("Failed to exchange code for token. Status: " + 
                    response.getStatusCode() + ", Body: " + response.getBody().asString());
        }
    }
    
    /**
     * Refresh an access token using a refresh token
     * @param refreshToken The refresh token
     * @return New TokenResponse
     */
    public TokenResponse refreshToken(String refreshToken) {
        System.out.println("Refreshing access token...");
        
        Response response = given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .formParam("grant_type", "refresh_token")
                .formParam("client_id", config.getClientId())
                .formParam("client_secret", config.getClientSecret())
                .formParam("refresh_token", refreshToken)
                .when()
                .post(config.getTokenUrl())
                .then()
                .log().all()
                .extract()
                .response();
        
        if (response.getStatusCode() == 200) {
            return parseTokenResponse(response);
        } else {
            throw new RuntimeException("Failed to refresh token. Status: " + 
                    response.getStatusCode() + ", Body: " + response.getBody().asString());
        }
    }
    
    /**
     * Make an authenticated API call using the access token
     * @param apiUrl The API endpoint URL
     * @param accessToken The access token
     * @return Response from the API
     */
    public Response makeAuthenticatedRequest(String apiUrl, String accessToken) {
        System.out.println("Making authenticated API request to: " + apiUrl);
        
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .when()
                .get(apiUrl)
                .then()
                .log().all()
                .extract()
                .response();
    }
    
    /**
     * Parse the token response from the OAuth provider
     * @param response The HTTP response
     * @return TokenResponse object
     */
    private TokenResponse parseTokenResponse(Response response) {
        String accessToken = response.jsonPath().getString("access_token");
        String tokenType = response.jsonPath().getString("token_type");
        String refreshToken = response.jsonPath().getString("refresh_token");
        Long expiresIn = response.jsonPath().getLong("expires_in");
        String scope = response.jsonPath().getString("scope");
        
        return new TokenResponse(
                accessToken,
                tokenType != null ? tokenType : "Bearer",
                refreshToken,
                expiresIn != null ? expiresIn : 3600,
                scope
        );
    }
    
    // Getters for testing purposes
    public String getCodeVerifier() { return codeVerifier; }
    public String getCodeChallenge() { return codeChallenge; }
}