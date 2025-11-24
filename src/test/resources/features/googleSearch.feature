Feature: Google Search Functionality
  As a user
  I want to search on Google
  So that I can find relevant information

  @smoke @search
  Scenario: Successful search for Selenium WebDriver
    Given I am on the Google homepage
    When I search for "Selenium WebDriver"
    Then I should see search results
    And the page title should contain "Selenium WebDriver"

  @smoke @search
  Scenario: Search with multiple keywords
    Given I am on the Google homepage
    When I search for "Cucumber BDD Testing"
    Then I should see search results
    And the page title should contain "Cucumber BDD Testing"

  @search
  Scenario Outline: Search for different technologies
    Given I am on the Google homepage
    When I search for "<searchTerm>"
    Then I should see search results
    And the page title should contain "<searchTerm>"

    Examples:
      | searchTerm           |
      | Java Programming     |
      | Selenium Automation  |
      | BDD Framework        |
