package com.automation.stepdefinitions;

import adapter.AppiumAdapter;
import adapter.WebAutomationAdapter;
import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Appium Step Definitions for Mobile Testing
 *
 * Cucumber step definitions for mobile automation testing using Appium.
 * Supports Android and iOS mobile gestures, app management, and context switching.
 *
 * <h3>Design Patterns: BDD + STEP DEFINITIONS</h3>
 *
 * <h4>Mobile-Specific Steps:</h4>
 * - App Management: Install, launch, close, remove apps
 * - Mobile Gestures: Swipe, tap, hide keyboard
 * - Context Switching: Switch between NATIVE_APP and WEBVIEW
 * - Device Actions: Mobile-specific interactions
 *
 * <h4>Usage in Feature Files:</h4>
 * <pre>
 * {@literal @}mobile {@literal @}android
 * Scenario: Test Android App
 *   Given I am using "android" device
 *   When I launch the app
 *   And I tap on element "id=loginButton"
 *   Then element "id=dashboard" should be visible
 * </pre>
 *
 * @author Selenium Test Automation Framework
 * @version 1.0
 */
public class AppiumSteps {

    /**
     * Get the adapter instance from Hooks
     */
    private WebAutomationAdapter getAdapter() {
        return Hooks.getAdapter();
    }

    /**
     * Get AppiumAdapter instance with mobile-specific methods
     */
    private AppiumAdapter getAppiumAdapter() {
        WebAutomationAdapter adapter = getAdapter();
        if (adapter instanceof AppiumAdapter) {
            return (AppiumAdapter) adapter;
        }
        throw new IllegalStateException("AppiumAdapter not initialized. Make sure @mobile tag is used and mobile execution is configured.");
    }

    // ==================== APP MANAGEMENT ====================

    @When("I launch the app")
    public void launchApp() {
        System.out.println("📱 Launching the app...");
        // App is already launched during initialization
        // This step is for readability
    }

    @When("I close the app")
    public void closeApp() {
        System.out.println("📱 Closing the app...");
        getAppiumAdapter().closeApp();
    }

    @When("I install app {string}")
    public void installApp(String appPath) {
        System.out.println("📱 Installing app: " + appPath);
        getAppiumAdapter().installApp(appPath);
    }

    @When("I remove app {string}")
    public void removeApp(String bundleId) {
        System.out.println("📱 Removing app: " + bundleId);
        getAppiumAdapter().removeApp(bundleId);
    }

    // ==================== MOBILE GESTURES ====================

    @When("I swipe from {int},{int} to {int},{int}")
    public void swipe(int startX, int startY, int endX, int endY) {
        System.out.println(String.format("👆 Swiping from (%d,%d) to (%d,%d)", startX, startY, endX, endY));
        getAppiumAdapter().swipe(startX, startY, endX, endY);
    }

    @When("I swipe {string} on element {string}")
    public void swipeOnElement(String direction, String locator) {
        System.out.println("👆 Swiping " + direction + " on element: " + locator);
        // This is a simplified version - you can enhance with actual element coordinates
        switch (direction.toLowerCase()) {
            case "up":
                getAppiumAdapter().swipe(500, 1000, 500, 200);
                break;
            case "down":
                getAppiumAdapter().swipe(500, 200, 500, 1000);
                break;
            case "left":
                getAppiumAdapter().swipe(800, 500, 200, 500);
                break;
            case "right":
                getAppiumAdapter().swipe(200, 500, 800, 500);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    @When("I tap at coordinates {int},{int}")
    public void tap(int x, int y) {
        System.out.println(String.format("👆 Tapping at (%d,%d)", x, y));
        getAppiumAdapter().tap(x, y);
    }

    @When("I tap on element {string}")
    public void tapOnElement(String locator) {
        System.out.println("👆 Tapping on element: " + locator);
        getAdapter().click(locator);
    }

    @When("I click on element {string}")
    public void clickOnElement(String locator) {
        tapOnElement(locator);
    }

    @When("I enter {string} in element {string}")
    public void enterTextInElement(String text, String locator) {
        System.out.println("⌨️ Entering '" + text + "' in element: " + locator);
        getAdapter().type(locator, text);
    }

    @When("I hide keyboard")
    public void hideKeyboard() {
        System.out.println("⌨️ Hiding keyboard...");
        getAppiumAdapter().hideKeyboard();
    }

    @When("I hide the keyboard")
    public void hideTheKeyboard() {
        hideKeyboard();
    }

    // ==================== CONTEXT SWITCHING ====================

    @When("I switch to {string} context")
    public void switchContext(String contextName) {
        System.out.println("🔄 Switching to context: " + contextName);
        getAppiumAdapter().switchContext(contextName);
    }

    @When("I switch to webview")
    public void switchToWebView() {
        System.out.println("🔄 Switching to WEBVIEW context...");
        // Find first WEBVIEW context
        java.util.Set<String> contexts = getAppiumAdapter().getContexts();
        for (String context : contexts) {
            if (context.contains("WEBVIEW")) {
                getAppiumAdapter().switchContext(context);
                return;
            }
        }
        throw new RuntimeException("No WEBVIEW context found");
    }

    @When("I switch to WEBVIEW")
    public void switchToWEBVIEW() {
        switchToWebView();
    }

    @When("I switch to native")
    public void switchToNative() {
        System.out.println("🔄 Switching to NATIVE_APP context...");
        getAppiumAdapter().switchContext("NATIVE_APP");
    }

    @When("I switch to NATIVE_APP")
    public void switchToNATIVE_APP() {
        switchToNative();
    }

    @Then("current context should be {string}")
    public void verifyCurrentContext(String expectedContext) {
        String currentContext = getAppiumAdapter().getCurrentContext();
        System.out.println("✓ Verifying current context: " + currentContext);
        assertTrue(
                currentContext.contains(expectedContext),"Expected context: " + expectedContext + ", but was: " + currentContext);
    }

    // ==================== DEVICE-SPECIFIC ACTIONS ====================

    @Given("I am using mobile testing framework")
    public void iAmUsingMobileTestingFramework() {
        System.out.println("📱 Using mobile testing framework");
        // Framework is already initialized by hooks based on @mobile tag
        // This step is mainly for documentation/readability
    }

    @Given("I am using {string} device")
    public void setDevice(String platform) {
        System.out.println("📱 Using device platform: " + platform);
        // Device is already initialized by hooks based on configuration
        // This step is mainly for documentation/readability
    }

    @Given("I am on {string} device")
    public void setDeviceAlternative(String platform) {
        setDevice(platform);
    }

    @When("I rotate device to {string}")
    public void rotateDevice(String orientation) {
        System.out.println("🔄 Rotating device to: " + orientation);
        // Note: This requires adding rotation method to AppiumAdapter
        System.out.println("⚠️ Device rotation not yet implemented");
    }

    @When("I put app in background for {int} seconds")
    public void backgroundApp(int seconds) {
        System.out.println("📱 Putting app in background for " + seconds + " seconds...");
        // Note: This requires adding background method to AppiumAdapter
        System.out.println("⚠️ Background app not yet implemented");
    }

    @When("I wait for {int} seconds")
    public void waitForSeconds(int seconds) throws InterruptedException {
        System.out.println("⏳ Waiting for " + seconds + " seconds...");
        Thread.sleep(seconds * 1000L);
    }

    // ==================== MOBILE ELEMENT INTERACTIONS ====================

    @When("I scroll to element {string}")
    public void scrollToElement(String locator) {
        System.out.println("📜 Scrolling to element: " + locator);
        // Simple scroll implementation - swipe until element is visible
        int maxScrolls = 5;
        int scrollCount = 0;

        while (scrollCount < maxScrolls) {
            if (getAdapter().isDisplayed(locator)) {
                System.out.println("✓ Element found after " + scrollCount + " scrolls");
                return;
            }
            getAppiumAdapter().swipe(500, 1000, 500, 200);
            scrollCount++;
        }

        throw new RuntimeException("Element not found after " + maxScrolls + " scrolls: " + locator);
    }

    @When("I long press on element {string}")
    public void longPress(String locator) {
        System.out.println("👆 Long pressing on element: " + locator);
        // Note: This requires adding long press method to AppiumAdapter
        System.out.println("⚠️ Long press not yet implemented - using regular click");
        getAdapter().click(locator);
    }

    @Then("element {string} should be visible on mobile")
    public void verifyElementVisibleOnMobile(String locator) {
        System.out.println("✓ Verifying element is visible: " + locator);
        assertTrue(
                getAdapter().isDisplayed(locator),"Element should be visible: " + locator);
    }

    @Then("element {string} should not be visible on mobile")
    public void verifyElementNotVisibleOnMobile(String locator) {
        System.out.println("✓ Verifying element is not visible: " + locator);
        assertFalse(
                getAdapter().isDisplayed(locator),"Element should not be visible: " + locator);
    }

    @Then("element {string} text should be {string} on mobile")
    public void verifyElementTextOnMobile(String locator, String expectedText) {
        String actualText = getAdapter().getText(locator);
        System.out.println("✓ Verifying text. Expected: " + expectedText + ", Actual: " + actualText);
        assertEquals(expectedText, actualText,"Element text mismatch");
    }

    // ==================== MOBILE-SPECIFIC VALIDATIONS ====================

    @Then("I should see {int} contexts available")
    public void verifyContextCount(int expectedCount) {
        java.util.Set<String> contexts = getAppiumAdapter().getContexts();
        System.out.println("✓ Available contexts: " + contexts);
        assertEquals(expectedCount, contexts.size(),"Context count mismatch");
    }

    @Then("WEBVIEW context should be available")
    public void verifyWebViewAvailable() {
        java.util.Set<String> contexts = getAppiumAdapter().getContexts();
        boolean hasWebView = contexts.stream().anyMatch(c -> c.contains("WEBVIEW"));
        System.out.println("✓ Checking for WEBVIEW context. Available: " + hasWebView);
        assertTrue(hasWebView,"WEBVIEW context should be available");
    }

    @Then("app should be installed")
    public void verifyAppInstalled() {
        System.out.println("✓ Verifying app is installed...");
        // This would require app package verification
        assertTrue( getAppiumAdapter().isActive(),"App should be installed");
    }
}
