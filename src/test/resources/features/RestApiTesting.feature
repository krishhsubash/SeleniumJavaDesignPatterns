@api @rest-assured
Feature: REST API Testing with REST Assured
  As a QA Engineer
  I want to test REST APIs
  So that I can validate API functionality and performance

  Background:
    Given I have API base URL "https://jsonplaceholder.typicode.com"

  @smoke @get
  Scenario: Get user by ID
    When I send GET request to "/users/1"
    Then the response status code should be 200
    And the response should be successful
    And the response time should be less than 2000 ms
    And the response content type should be "application/json"
    And the response should have JSON path "id"
    And the response JSON path "id" should be "1"
    And the response should have JSON path "name"

  @smoke @get
  Scenario: Get all users
    When I send GET request to "/users"
    Then the response status code should be 200
    And the response should be successful
    And the response time should be less than 3000 ms
    And the response content type should be "application/json"

  @smoke @get
  Scenario: Get posts with query parameters
    Given I add query parameter "userId" with value "1"
    When I send GET request to "/posts"
    Then the response status code should be 200
    And the response should be successful
    And the response body should contain "userId"

  @post
  Scenario: Create new post
    When I send POST request to "/posts" with body:
      """
      {
        "title": "Test Post",
        "body": "This is a test post",
        "userId": 1
      }
      """
    Then the response status code should be 201
    And the response should have JSON path "id"
    And the response JSON path "title" should be "Test Post"

  @put
  Scenario: Update existing post
    When I send PUT request to "/posts/1" with body:
      """
      {
        "id": 1,
        "title": "Updated Title",
        "body": "Updated body",
        "userId": 1
      }
      """
    Then the response status code should be 200
    And the response JSON path "title" should be "Updated Title"

  @delete
  Scenario: Delete post
    When I send DELETE request to "/posts/1"
    Then the response status code should be 200
    And the response should be successful

  @validation @headers
  Scenario: Validate response headers
    When I send GET request to "/users/1"
    Then the response status code should be 200
    And the response should have header "Content-Type"
    And the response should have header "Cache-Control"

  @performance
  Scenario: Validate API performance
    When I send GET request to "/users"
    Then the response time should be less than 1000 ms
    And the response should be successful

  @schema-validation
  Scenario: Validate JSON response structure
    When I send GET request to "/users/1"
    Then the response status code should be 200
    And the response should have JSON path "id"
    And the response should have JSON path "name"
    And the response should have JSON path "email"
    And the response should have JSON path "address"
    And the response should have JSON path "phone"

  @negative
  Scenario: Handle non-existent resource
    When I send GET request to "/users/9999999"
    Then the response status code should be 404

  @debug
  Scenario: Print API response for debugging
    When I send GET request to "/users/1"
    Then I print the API response
    And the response status code should be 200
