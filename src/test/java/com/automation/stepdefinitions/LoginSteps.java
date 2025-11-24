package com.automation.stepdefinitions;


import decorator.WebElementDecoratorFactory;
import driver.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Step definitions for Login feature
 * Demonstrates usage of DriverManager Singleton pattern and Decorator Pattern
 */
public class LoginSteps {

    private WebDriver driver;

    @Given("I navigate to the login page")
    public void i_navigate_to_the_login_page() {
        // Get driver instance from Singleton DriverManager
        driver = DriverManager.getDriver();

        // Replace with actual login page URL
        driver.get("https://example.com/login");
        System.out.println("Navigated to login page");
    }

    @When("I enter username {string}")
    public void i_enter_username(String username) {
        driver = DriverManager.getDriver();

        // Example implementation using Decorator Pattern
        try {
            WebElement usernameField = driver.findElement(By.id("username"));

            // Decorate with production mode: Error Handling + Logging + Screenshot on Error
            WebElement decoratedUsername = WebElementDecoratorFactory.productionMode(
                    usernameField, driver, "Username Field"
            );

            decoratedUsername.clear();
            decoratedUsername.sendKeys(username);
            System.out.println("Entered username: " + username);
        } catch (Exception e) {
            System.out.println("Username field not found - this is a demo implementation");
        }
    }

    @When("I enter password {string}")
    public void i_enter_password(String password) {
        driver = DriverManager.getDriver();

        // Example implementation using Decorator Pattern
        try {
            WebElement passwordField = driver.findElement(By.id("password"));

            // Decorate with production mode (sensitive data will be masked in logs)
            WebElement decoratedPassword = WebElementDecoratorFactory.productionMode(
                    passwordField, driver, "Password Field"
            );

            decoratedPassword.clear();
            decoratedPassword.sendKeys(password);
            System.out.println("Entered password");
        } catch (Exception e) {
            System.out.println("Password field not found - this is a demo implementation");
        }
    }

    @When("I click on the login button")
    public void i_click_on_the_login_button() {
        driver = DriverManager.getDriver();

        // Example implementation using Decorator Pattern
        try {
            WebElement loginButton = driver.findElement(By.id("loginButton"));

            // Decorate with highlighting and logging for visual feedback
            WebElement decoratedButton = WebElementDecoratorFactory.withHighlightingAndLogging(
                    loginButton, driver, "Login Button"
            );

            decoratedButton.click();
            System.out.println("Clicked login button");
        } catch (Exception e) {
            System.out.println("Login button not found - this is a demo implementation");
        }
    }

    @Then("I should be redirected to the dashboard")
    public void i_should_be_redirected_to_the_dashboard() {
        driver = DriverManager.getDriver();

        // Example implementation - replace with actual validation
        System.out.println("Current URL: " + driver.getCurrentUrl());
        // assertTrue(driver.getCurrentUrl().contains("dashboard"),
        //     "User should be redirected to dashboard");
    }

    @Then("I should see the welcome message")
    public void i_should_see_the_welcome_message() {
        driver = DriverManager.getDriver();

        // Example implementation - replace with actual locators
        System.out.println("Verifying welcome message");
        // WebElement welcomeMsg = driver.findElement(By.id("welcomeMessage"));
        // assertTrue(welcomeMsg.isDisplayed(), "Welcome message should be displayed");
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedError) {
        driver = DriverManager.getDriver();

        // Example implementation - replace with actual locators
        System.out.println("Expected error message: " + expectedError);
        // WebElement errorMsg = driver.findElement(By.className("error-message"));
        // assertEquals(expectedError, errorMsg.getText(), "Error message should match");
    }

    @Then("I should remain on the login page")
    public void i_should_remain_on_the_login_page() {
        driver = DriverManager.getDriver();

        // Example implementation - replace with actual validation
        System.out.println("Verifying still on login page: " + driver.getCurrentUrl());
        // assertTrue(driver.getCurrentUrl().contains("login"),
        //     "User should remain on login page");
    }
}
