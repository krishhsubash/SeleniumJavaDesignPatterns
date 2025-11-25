package api.client;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * REST Assured API Client - Singleton Pattern
 * Provides centralized API testing capabilities with request/response logging
 * Demonstrates Singleton Pattern for API client management
 */
public class RestApiClient {

    private static RestApiClient instance;
    private RequestSpecification baseRequestSpec;
    private String baseUri;

    private RestApiClient() {
        // Private constructor for Singleton
    }

    /**
     * Get singleton instance of RestApiClient
     * @return RestApiClient instance
     */
    public static synchronized RestApiClient getInstance() {
        if (instance == null) {
            instance = new RestApiClient();
        }
        return instance;
    }

    /**
     * Configure base URI and default settings
     * @param baseUri base URI for API requests
     * @return this client for chaining
     */
    public RestApiClient configure(String baseUri) {
        this.baseUri = baseUri;
        RestAssured.baseURI = baseUri;

        // Build base request specification
        baseRequestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        return this;
    }

    /**
     * Get base request specification
     * @return RequestSpecification
     */
    public RequestSpecification getBaseRequestSpec() {
        if (baseRequestSpec == null) {
            throw new IllegalStateException("RestApiClient not configured. Call configure() first.");
        }
        return RestAssured.given().spec(baseRequestSpec);
    }

    /**
     * Perform GET request
     * @param endpoint API endpoint
     * @return Response
     */
    public Response get(String endpoint) {
        return getBaseRequestSpec()
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform GET request with path parameters
     * @param endpoint API endpoint with path parameters
     * @param pathParams path parameters map
     * @return Response
     */
    public Response get(String endpoint, Map<String, Object> pathParams) {
        return getBaseRequestSpec()
                .pathParams(pathParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform GET request with query parameters
     * @param endpoint API endpoint
     * @param queryParams query parameters map
     * @return Response
     */
    public Response getWithQueryParams(String endpoint, Map<String, Object> queryParams) {
        return getBaseRequestSpec()
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform POST request with body
     * @param endpoint API endpoint
     * @param body request body
     * @return Response
     */
    public Response post(String endpoint, Object body) {
        return getBaseRequestSpec()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform POST request without body
     * @param endpoint API endpoint
     * @return Response
     */
    public Response post(String endpoint) {
        return getBaseRequestSpec()
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform PUT request
     * @param endpoint API endpoint
     * @param body request body
     * @return Response
     */
    public Response put(String endpoint, Object body) {
        return getBaseRequestSpec()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform PATCH request
     * @param endpoint API endpoint
     * @param body request body
     * @return Response
     */
    public Response patch(String endpoint, Object body) {
        return getBaseRequestSpec()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform DELETE request
     * @param endpoint API endpoint
     * @return Response
     */
    public Response delete(String endpoint) {
        return getBaseRequestSpec()
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform DELETE request with path parameters
     * @param endpoint API endpoint
     * @param pathParams path parameters map
     * @return Response
     */
    public Response delete(String endpoint, Map<String, Object> pathParams) {
        return getBaseRequestSpec()
                .pathParams(pathParams)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Set authentication header
     * @param token authentication token
     * @return RequestSpecification with auth header
     */
    public RequestSpecification withBearerAuth(String token) {
        return getBaseRequestSpec()
                .header("Authorization", "Bearer " + token);
    }

    /**
     * Set basic authentication
     * @param username username
     * @param password password
     * @return RequestSpecification with basic auth
     */
    public RequestSpecification withBasicAuth(String username, String password) {
        return getBaseRequestSpec()
                .auth()
                .preemptive()
                .basic(username, password);
    }

    /**
     * Add custom header
     * @param headerName header name
     * @param headerValue header value
     * @return RequestSpecification with custom header
     */
    public RequestSpecification withHeader(String headerName, String headerValue) {
        return getBaseRequestSpec()
                .header(headerName, headerValue);
    }

    /**
     * Reset client configuration
     */
    public void reset() {
        RestAssured.reset();
        baseRequestSpec = null;
        baseUri = null;
    }

    /**
     * Get current base URI
     * @return base URI
     */
    public String getBaseUri() {
        return baseUri;
    }
}

