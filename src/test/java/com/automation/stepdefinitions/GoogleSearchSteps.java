package com.automation.stepdefinitions;

import decorator.WebElementDecoratorFactory;
import driver.DriverManager;
import utils.CaptchaHandler;
import utils.ElementFinder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Step definitions for Google Search feature
 * Demonstrates:
 * - Singleton Pattern (DriverManager)
 * - Factory Pattern (BrowserFactory - used by DriverManager)
 * - Builder Pattern (ChromeOptionsBuilder - used by Factory)
 * - Decorator Pattern (WebElementDecorator - for enhanced elements)
 */
public class GoogleSearchSteps {

    private WebDriver driver;
    private ElementFinder finder;

    @Given("I am on the Google homepage")
    public void i_am_on_the_google_homepage() {
        // SINGLETON PATTERN: Get driver instance from DriverManager
        // (DriverManager uses FACTORY & BUILDER patterns internally to create browser)
        driver = DriverManager.getDriver();
        finder = new ElementFinder(driver);

        CaptchaHandler captchaHandler = new CaptchaHandler(driver);

        // Strategy 1: Try direct URL to search (bypasses homepage CAPTCHA)
        driver.get("https://www.google.com/");

        // Hide automation indicators immediately
        captchaHandler.hideAutomationIndicators();

        // Wait a bit for page to stabilize
        CaptchaHandler.addDelay(1000);

        // Check for CAPTCHA and handle automatically
        if (captchaHandler.isCaptchaPresent()) {
            System.out.println("⚠️ CAPTCHA detected on Google homepage - attempting automated bypass");
            captchaHandler.handleCaptchaAutomatically();
        }

        // Handle cookie consent if present
        // DECORATOR PATTERN: Using error handling decorator for flaky consent button
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement consentButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Accept') or contains(., 'I agree')]")
            ));

            // Decorate with error handling + logging
            WebElement decoratedButton = WebElementDecoratorFactory.withLoggingAndErrorHandling(
                    consentButton, driver, "Cookie Consent Button"
            );
            decoratedButton.click();
        } catch (Exception e) {
            // Cookie consent not present or already accepted
            System.out.println("Cookie consent dialog not found - proceeding");
        }
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        driver = DriverManager.getDriver();
        CaptchaHandler captchaHandler = new CaptchaHandler(driver);

        // Strategy 1: Try direct URL navigation (avoids search box interaction)
        if (captchaHandler.navigateDirectToSearchResults(searchTerm)) {
            System.out.println("✅ Searched using direct URL: " + searchTerm);
            return;
        }

        // Strategy 2: Use search suggestions (more human-like)
        try {
            if (captchaHandler.useSearchSuggestions(searchTerm)) {
                System.out.println("✅ Searched using suggestions: " + searchTerm);

                // Check for CAPTCHA after search
                CaptchaHandler.addDelay(1000);
                if (captchaHandler.isCaptchaPresent()) {
                    captchaHandler.handleCaptchaAutomatically();
                }
                return;
            }
        } catch (Exception e) {
            System.out.println("Search suggestions failed, trying standard approach");
        }

        // Strategy 3: Standard search (fallback)
        // Add small delay to appear more human-like
        CaptchaHandler.addDelay(500);

        // DECORATOR PATTERN: Use production mode for search box
        // Includes: Error Handling + Logging + Screenshot on Error
        WebElement searchBox = finder.findProductionElement(
                By.name("q"),
                "Google Search Box"
        );

        searchBox.clear();

        // Type with small delays to appear more human
        CaptchaHandler.addDelay(300);
        searchBox.sendKeys(searchTerm);

        CaptchaHandler.addDelay(500);
        searchBox.sendKeys(Keys.RETURN);

        // Check for CAPTCHA after search
        CaptchaHandler.addDelay(1000);

        if (captchaHandler.isCaptchaPresent()) {
            System.out.println("⚠️ CAPTCHA detected after search - attempting automated bypass");
            captchaHandler.handleCaptchaAutomatically();
        }

        System.out.println("✅ Searched for: " + searchTerm);
    }

    @Then("I should see search results")
    public void i_should_see_search_results() {
        driver = DriverManager.getDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));

        // DECORATOR PATTERN: Use error handling decorator for assertion
        // This ensures retries if element is stale or not immediately available
        WebElement searchResults = finder.findSafe(
                By.id("search"),
                "Search Results Container"
        );

        assertTrue(searchResults.isDisplayed(), "Search results should be displayed");
        System.out.println("✅ Search results displayed successfully");
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedTitle) {
        driver = DriverManager.getDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains(expectedTitle));

        String actualTitle = driver.getTitle();
        assertTrue(actualTitle.contains(expectedTitle),
                "Page title should contain: " + expectedTitle + " but was: " + actualTitle);
    }
}
