Feature: Web Testing with Playwright
  Testing basic web functionality using Playwright browser automation

  Scenario: Browser initialization test
    Given I have a browser available
    When I open a new browser page
    Then I should be on the correct page

  Scenario: Basic navigation test
    Given I have a browser available
    When I navigate to "https://example.com"
    Then I should be on the correct page
    And I should see the page title contains "example"