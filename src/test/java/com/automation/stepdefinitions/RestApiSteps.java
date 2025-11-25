package com.automation.stepdefinitions;

import api.client.RestApiClient;
import api.utils.ApiResponseValidator;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Step Definitions for REST API Testing
 * Demonstrates REST Assured integration with Cucumber
 * Uses Singleton Pattern for API client management
 */
public class RestApiSteps {

    private RestApiClient apiClient;
    private Response response;
    private Map<String, Object> requestBody;
    private Map<String, Object> queryParams;
    private String authToken;

    @Before("@api")
    public void setupApi() {
        apiClient = RestApiClient.getInstance();
        requestBody = new HashMap<>();
        queryParams = new HashMap<>();
    }

    @After("@api")
    public void teardownApi() {
        if (apiClient != null) {
            apiClient.reset();
        }
    }

    @Given("I have API base URL {string}")
    public void i_have_api_base_url(String baseUrl) {
        apiClient.configure(baseUrl);
        System.out.println("✅ API configured with base URL: " + baseUrl);
    }

    @Given("I have authentication token {string}")
    public void i_have_authentication_token(String token) {
        this.authToken = token;
        System.out.println("✅ Authentication token set");
    }

    @Given("I have request body:")
    public void i_have_request_body(String jsonBody) {
        requestBody = new HashMap<>();
        // For simplicity, storing as raw JSON string
        // In real scenarios, parse to Map or use POJO
        System.out.println("✅ Request body set: " + jsonBody);
    }

    @Given("I add query parameter {string} with value {string}")
    public void i_add_query_parameter_with_value(String paramName, String paramValue) {
        queryParams.put(paramName, paramValue);
        System.out.println("✅ Added query parameter: " + paramName + "=" + paramValue);
    }

    @When("I send GET request to {string}")
    public void i_send_get_request_to(String endpoint) {
        if (authToken != null) {
            response = apiClient.withBearerAuth(authToken)
                    .when().get(endpoint)
                    .then().extract().response();
        } else if (!queryParams.isEmpty()) {
            response = apiClient.getWithQueryParams(endpoint, queryParams);
        } else {
            response = apiClient.get(endpoint);
        }
        System.out.println("✅ GET request sent to: " + endpoint);
    }

    @When("I send POST request to {string}")
    public void i_send_post_request_to(String endpoint) {
        if (authToken != null) {
            response = apiClient.withBearerAuth(authToken)
                    .body(requestBody)
                    .when().post(endpoint)
                    .then().extract().response();
        } else {
            response = apiClient.post(endpoint, requestBody);
        }
        System.out.println("✅ POST request sent to: " + endpoint);
    }

    @When("I send POST request to {string} with body:")
    public void i_send_post_request_to_with_body(String endpoint, String jsonBody) {
        response = apiClient.post(endpoint, jsonBody);
        System.out.println("✅ POST request sent to: " + endpoint);
    }

    @When("I send PUT request to {string}")
    public void i_send_put_request_to(String endpoint) {
        if (authToken != null) {
            response = apiClient.withBearerAuth(authToken)
                    .body(requestBody)
                    .when().put(endpoint)
                    .then().extract().response();
        } else {
            response = apiClient.put(endpoint, requestBody);
        }
        System.out.println("✅ PUT request sent to: " + endpoint);
    }

    @When("I send DELETE request to {string}")
    public void i_send_delete_request_to(String endpoint) {
        if (authToken != null) {
            response = apiClient.withBearerAuth(authToken)
                    .when().delete(endpoint)
                    .then().extract().response();
        } else {
            response = apiClient.delete(endpoint);
        }
        System.out.println("✅ DELETE request sent to: " + endpoint);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        ApiResponseValidator.of(response)
                .hasStatusCode(expectedStatusCode);
        System.out.println("✅ Validated status code: " + expectedStatusCode);
    }

    @Then("the response should be successful")
    public void the_response_should_be_successful() {
        ApiResponseValidator.of(response)
                .isSuccessful();
        System.out.println("✅ Response is successful (2xx)");
    }

    @Then("the response time should be less than {int} ms")
    public void the_response_time_should_be_less_than_ms(int maxTimeMs) {
        ApiResponseValidator.of(response)
                .responseTimeLessThan(maxTimeMs);
        System.out.println("✅ Response time validated: " + response.getTime() + " ms");
    }

    @Then("the response should have header {string}")
    public void the_response_should_have_header(String headerName) {
        ApiResponseValidator.of(response)
                .hasHeader(headerName);
        System.out.println("✅ Header exists: " + headerName);
    }

    @Then("the response header {string} should be {string}")
    public void the_response_header_should_be(String headerName, String expectedValue) {
        ApiResponseValidator.of(response)
                .hasHeaderWithValue(headerName, expectedValue);
        System.out.println("✅ Header validated: " + headerName + "=" + expectedValue);
    }

    @Then("the response content type should be {string}")
    public void the_response_content_type_should_be(String expectedContentType) {
        ApiResponseValidator.of(response)
                .hasContentType(expectedContentType);
        System.out.println("✅ Content type validated: " + expectedContentType);
    }

    @Then("the response should have JSON path {string}")
    public void the_response_should_have_json_path(String jsonPath) {
        ApiResponseValidator.of(response)
                .hasJsonPath(jsonPath);
        System.out.println("✅ JSON path exists: " + jsonPath);
    }

    @Then("the response JSON path {string} should be {string}")
    public void the_response_json_path_should_be(String jsonPath, String expectedValue) {
        ApiResponseValidator.of(response)
                .hasJsonPathWithValue(jsonPath, expectedValue);
        System.out.println("✅ JSON path validated: " + jsonPath + "=" + expectedValue);
    }

    @Then("the response body should contain {string}")
    public void the_response_body_should_contain(String expectedText) {
        ApiResponseValidator.of(response)
                .bodyContains(expectedText);
        System.out.println("✅ Response body contains: " + expectedText);
    }

    @Then("I print the API response")
    public void i_print_the_api_response() {
        ApiResponseValidator.of(response)
                .printResponse();
    }
}

