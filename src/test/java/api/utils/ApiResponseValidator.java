package api.utils;

import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API Response Validator
 * Provides fluent validation methods for REST API responses
 */
public class ApiResponseValidator {

    private final Response response;

    public ApiResponseValidator(Response response) {
        this.response = response;
    }

    /**
     * Create validator from response
     * @param response REST response
     * @return ApiResponseValidator
     */
    public static ApiResponseValidator of(Response response) {
        return new ApiResponseValidator(response);
    }

    /**
     * Validate status code
     * @param expectedStatusCode expected HTTP status code
     * @return this validator for chaining
     */
    public ApiResponseValidator hasStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode,
                String.format("Expected status code %d but got %d", expectedStatusCode, actualStatusCode));
        return this;
    }

    /**
     * Validate response time
     * @param maxResponseTimeMs maximum response time in milliseconds
     * @return this validator for chaining
     */
    public ApiResponseValidator responseTimeLessThan(long maxResponseTimeMs) {
        long actualTime = response.getTime();
        assertTrue(actualTime < maxResponseTimeMs,
                String.format("Response time %d ms exceeds maximum %d ms", actualTime, maxResponseTimeMs));
        return this;
    }

    /**
     * Validate response contains header
     * @param headerName header name
     * @return this validator for chaining
     */
    public ApiResponseValidator hasHeader(String headerName) {
        assertNotNull(response.getHeader(headerName),
                "Response should contain header: " + headerName);
        return this;
    }

    /**
     * Validate header value
     * @param headerName header name
     * @param expectedValue expected header value
     * @return this validator for chaining
     */
    public ApiResponseValidator hasHeaderWithValue(String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        assertEquals(expectedValue, actualValue,
                String.format("Header '%s' should have value '%s' but got '%s'",
                        headerName, expectedValue, actualValue));
        return this;
    }

    /**
     * Validate content type
     * @param expectedContentType expected content type
     * @return this validator for chaining
     */
    public ApiResponseValidator hasContentType(String expectedContentType) {
        String actualContentType = response.getContentType();
        assertTrue(actualContentType.contains(expectedContentType),
                String.format("Expected content type to contain '%s' but got '%s'",
                        expectedContentType, actualContentType));
        return this;
    }

    /**
     * Validate JSON path exists
     * @param jsonPath JSON path expression
     * @return this validator for chaining
     */
    public ApiResponseValidator hasJsonPath(String jsonPath) {
        assertNotNull(response.jsonPath().get(jsonPath),
                "JSON path should exist: " + jsonPath);
        return this;
    }

    /**
     * Validate JSON path value
     * @param jsonPath JSON path expression
     * @param expectedValue expected value
     * @return this validator for chaining
     */
    public ApiResponseValidator hasJsonPathWithValue(String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        assertEquals(expectedValue, actualValue,
                String.format("JSON path '%s' should have value '%s' but got '%s'",
                        jsonPath, expectedValue, actualValue));
        return this;
    }

    /**
     * Validate response body contains text
     * @param expectedText expected text in response body
     * @return this validator for chaining
     */
    public ApiResponseValidator bodyContains(String expectedText) {
        String body = response.getBody().asString();
        assertTrue(body.contains(expectedText),
                "Response body should contain: " + expectedText);
        return this;
    }

    /**
     * Validate response body matches regex
     * @param regex regular expression pattern
     * @return this validator for chaining
     */
    public ApiResponseValidator bodyMatches(String regex) {
        String body = response.getBody().asString();
        assertTrue(body.matches(regex),
                "Response body should match regex: " + regex);
        return this;
    }

    /**
     * Validate response is successful (2xx status code)
     * @return this validator for chaining
     */
    public ApiResponseValidator isSuccessful() {
        int statusCode = response.getStatusCode();
        assertTrue(statusCode >= 200 && statusCode < 300,
                String.format("Expected successful status code (2xx) but got %d", statusCode));
        return this;
    }

    /**
     * Get the response for further processing
     * @return Response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Print response details for debugging
     * @return this validator for chaining
     */
    public ApiResponseValidator printResponse() {
        System.out.println("\n========== API Response Details ==========");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Time: " + response.getTime() + " ms");
        System.out.println("Content Type: " + response.getContentType());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("Body: " + response.getBody().asString());
        System.out.println("==========================================\n");
        return this;
    }
}

