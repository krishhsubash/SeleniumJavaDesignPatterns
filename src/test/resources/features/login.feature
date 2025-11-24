Feature: Login Functionality
  As a user
  I want to login to the application
  So that I can access my account

  Background:
    Given I navigate to the login page

  @smoke @login
  Scenario: Successful login with valid credentials
    When I enter username "testuser@example.com"
    And I enter password "Test@123"
    And I click on the login button
    Then I should be redirected to the dashboard
    And I should see the welcome message

  @login @negative
  Scenario: Login with invalid credentials
    When I enter username "invalid@example.com"
    And I enter password "wrongpass"
    And I click on the login button
    Then I should see an error message "Invalid credentials"
    And I should remain on the login page

  @login @validation
  Scenario Outline: Login validation scenarios
    When I enter username "<username>"
    And I enter password "<password>"
    And I click on the login button
    Then I should see an error message "<errorMessage>"

    Examples:
      | username              | password  | errorMessage                |
      |                       | Test@123  | Username is required        |
      | testuser@example.com  |           | Password is required        |
      | invalid-email         | Test@123  | Invalid email format        |
