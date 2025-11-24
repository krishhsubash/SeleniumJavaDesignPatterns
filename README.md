# OAuth 2.0 with PKCE using REST Assured

This project demonstrates how to implement OAuth 2.0 authorization code flow with PKCE (Proof Key for Code Exchange) using REST Assured in Java.

## What is PKCE?

PKCE (Proof Key for Code Exchange) is a security extension to OAuth 2.0 designed to make the authorization code flow more secure, particularly for public clients like mobile apps and single-page applications.

### Key Benefits:
- Prevents authorization code interception attacks
- Eliminates the need for client secrets in public clients
- Provides additional security even for confidential clients
- Required by OAuth 2.1 specification

## Project Structure

```
src/main/java/org/example/
├── Main.java                    # Main entry point
├── OAuth2PKCEExample.java       # Complete OAuth flow demonstration
├── PKCEHelperDemo.java         # PKCE parameter generation demo
├── GoogleOAuthExample.java     # Real-world Google OAuth example
├── OAuth2PKCETests.java        # Test validation suite
└── oauth/
    ├── PKCEHelper.java         # PKCE parameter generation utilities
    ├── OAuthConfig.java        # OAuth configuration data class
    ├── TokenResponse.java      # Token response data class
    └── OAuth2PKCEClient.java   # Main OAuth client implementation
```

## Features

- **PKCE Parameter Generation**: Cryptographically secure code verifiers and challenges
- **Authorization URL Generation**: Complete OAuth authorization URLs with PKCE parameters
- **Token Exchange**: Exchange authorization codes for access tokens using PKCE
- **Token Refresh**: Refresh expired access tokens
- **Authenticated Requests**: Make API calls with Bearer token authentication
- **Google OAuth Example**: Ready-to-use Google OAuth integration
- **Comprehensive Testing**: Validation of all PKCE and OAuth components

## How to Use

### 1. Basic Usage

```java
// Configure your OAuth provider
OAuthConfig config = new OAuthConfig(
    "your-client-id",
    "your-client-secret",
    "https://auth.provider.com/oauth/authorize",
    "https://auth.provider.com/oauth/token", 
    "http://localhost:8080/callback",
    "read write"
);

// Create OAuth client
OAuth2PKCEClient client = new OAuth2PKCEClient(config);

// Generate authorization URL
String authUrl = client.generateAuthorizationUrl("random-state");
System.out.println("Visit: " + authUrl);

// After user completes auth and you get the code:
TokenResponse tokens = client.exchangeCodeForToken(authorizationCode);

// Make authenticated API calls
Response response = client.makeAuthenticatedRequest(
    "https://api.provider.com/user", 
    tokens.getAccessToken()
);
```

### 2. Google OAuth Example

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a project and enable APIs (Gmail, Google+, etc.)
3. Create OAuth 2.0 Client ID credentials
4. Add `http://localhost:8080/callback` to authorized redirect URIs
5. Use your client ID in `GoogleOAuthExample.java`

### 3. Running the Examples

```bash
# Compile the project
mvn compile

# Run the main example
mvn exec:java -Dexec.mainClass="org.example.Main"

# Run specific examples
mvn exec:java -Dexec.mainClass="org.example.OAuth2PKCEExample"
mvn exec:java -Dexec.mainClass="org.example.GoogleOAuthExample"
mvn exec:java -Dexec.mainClass="org.example.OAuth2PKCETests"
```

## OAuth Flow Steps

1. **Generate PKCE Parameters**
   - Create a random 128-character code verifier
   - Generate SHA256 hash of verifier (code challenge)

2. **Authorization Request**
   - Redirect user to authorization URL with PKCE parameters
   - User logs in and grants permissions

3. **Authorization Response**
   - User is redirected back with authorization code
   - Extract code from callback URL

4. **Token Exchange**
   - Exchange authorization code + code verifier for tokens
   - Receive access token, refresh token, etc.

5. **API Requests**
   - Use access token in Authorization header
   - Refresh token when needed

## Security Features

- **Cryptographically Random Code Verifiers**: 128 characters from secure character set
- **SHA256 Code Challenges**: Industry standard hashing with Base64URL encoding
- **State Parameter Support**: CSRF protection for authorization requests
- **RFC 7636 Compliance**: Follows official PKCE specification
- **URL Encoding**: Proper encoding of all URL parameters

## Dependencies

- **REST Assured 5.5.6**: HTTP client for API requests
- **Jackson**: JSON processing (included with REST Assured)
- **Java 21**: Modern Java features and security

## Testing

The project includes comprehensive tests that validate:
- PKCE parameter generation and consistency
- Authorization URL formatting
- RFC 7636 test vectors
- Configuration management

Run tests with:
```bash
mvn test
```

## Common OAuth Providers

### Google
- Authorization URL: `https://accounts.google.com/o/oauth2/v2/auth`
- Token URL: `https://oauth2.googleapis.com/token`
- Scopes: `openid email profile`

### Microsoft
- Authorization URL: `https://login.microsoftonline.com/common/oauth2/v2.0/authorize`
- Token URL: `https://login.microsoftonline.com/common/oauth2/v2.0/token`
- Scopes: `https://graph.microsoft.com/user.read`

### GitHub
- Authorization URL: `https://github.com/login/oauth/authorize`
- Token URL: `https://github.com/login/oauth/access_token`
- Scopes: `user:email repo`

## Error Handling

The implementation includes proper error handling for:
- Network failures during token exchange
- Invalid authorization codes
- Expired refresh tokens
- Malformed responses from OAuth providers

## License

This project is provided as an educational example for implementing OAuth 2.0 with PKCE using REST Assured.