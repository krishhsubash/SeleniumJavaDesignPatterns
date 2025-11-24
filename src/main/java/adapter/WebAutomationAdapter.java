package adapter;

import java.util.List;

/**
 * Adapter Interface for Web Automation
 *
 * This interface defines a unified API for web automation operations.
 * It allows different automation frameworks (Selenium, Playwright) to be
 * used interchangeably through the Adapter pattern.
 *
 * <h3>Design Pattern: ADAPTER</h3>
 *
 * <h4>Purpose:</h4>
 * Converts the interface of a class into another interface that clients expect.
 * Allows incompatible interfaces to work together.
 *
 * <h4>Problem Solved:</h4>
 * - Selenium WebDriver API differs from Playwright API
 * - Tests written for Selenium cannot directly use Playwright
 * - Need to switch between frameworks without rewriting tests
 *
 * <h4>Solution:</h4>
 * Define a common interface (WebAutomationAdapter) that both frameworks
 * can implement through adapter classes (SeleniumAdapter, PlaywrightAdapter).
 *
 * <pre>
 * Client Code → WebAutomationAdapter (Interface)
 *                       ↑
 *                       |
 *              ┌────────┴─────────┐
 *              |                  |
 *      SeleniumAdapter    PlaywrightAdapter
 *              |                  |
 *         WebDriver           Playwright
 * </pre>
 *
 * @author Selenium Test Automation Framework
 * @version 1.0
 */
public interface WebAutomationAdapter {

    // ============================================================
    // BROWSER LIFECYCLE
    // ============================================================

    /**
     * Initialize the browser instance
     *
     * @param browserType Browser type (chrome, firefox, edge, etc.)
     * @param headless Whether to run in headless mode
     */
    void initializeBrowser(String browserType, boolean headless);

    /**
     * Close the current browser window/page
     */
    void closeBrowser();

    /**
     * Quit/terminate the entire browser instance
     */
    void quitBrowser();

    // ============================================================
    // NAVIGATION
    // ============================================================

    /**
     * Navigate to a URL
     *
     * @param url The URL to navigate to
     */
    void navigateTo(String url);

    /**
     * Navigate back in browser history
     */
    void navigateBack();

    /**
     * Navigate forward in browser history
     */
    void navigateForward();

    /**
     * Refresh the current page
     */
    void refreshPage();

    /**
     * Get the current page URL
     *
     * @return Current URL
     */
    String getCurrentUrl();

    /**
     * Get the page title
     *
     * @return Page title
     */
    String getPageTitle();

    // ============================================================
    // ELEMENT INTERACTION
    // ============================================================

    /**
     * Click on an element
     *
     * @param locator Element locator (CSS, XPath, ID, etc.)
     */
    void click(String locator);

    /**
     * Type text into an element
     *
     * @param locator Element locator
     * @param text Text to type
     */
    void type(String locator, String text);

    /**
     * Clear text from an element
     *
     * @param locator Element locator
     */
    void clear(String locator);

    /**
     * Submit a form
     *
     * @param locator Form or submit button locator
     */
    void submit(String locator);

    // ============================================================
    // ELEMENT INFORMATION
    // ============================================================

    /**
     * Get text from an element
     *
     * @param locator Element locator
     * @return Element text
     */
    String getText(String locator);

    /**
     * Get attribute value from an element
     *
     * @param locator Element locator
     * @param attribute Attribute name
     * @return Attribute value
     */
    String getAttribute(String locator, String attribute);

    /**
     * Check if element is displayed
     *
     * @param locator Element locator
     * @return true if displayed, false otherwise
     */
    boolean isDisplayed(String locator);

    /**
     * Check if element is enabled
     *
     * @param locator Element locator
     * @return true if enabled, false otherwise
     */
    boolean isEnabled(String locator);

    /**
     * Check if element is selected (for checkboxes/radio buttons)
     *
     * @param locator Element locator
     * @return true if selected, false otherwise
     */
    boolean isSelected(String locator);

    // ============================================================
    // DROPDOWN/SELECT
    // ============================================================

    /**
     * Select option by visible text
     *
     * @param locator Select element locator
     * @param text Visible text to select
     */
    void selectByText(String locator, String text);

    /**
     * Select option by value
     *
     * @param locator Select element locator
     * @param value Value attribute to select
     */
    void selectByValue(String locator, String value);

    /**
     * Select option by index
     *
     * @param locator Select element locator
     * @param index Zero-based index to select
     */
    void selectByIndex(String locator, int index);

    // ============================================================
    // WAITS
    // ============================================================

    /**
     * Wait for element to be visible
     *
     * @param locator Element locator
     * @param timeoutSeconds Maximum wait time in seconds
     */
    void waitForVisible(String locator, int timeoutSeconds);

    /**
     * Wait for element to be clickable
     *
     * @param locator Element locator
     * @param timeoutSeconds Maximum wait time in seconds
     */
    void waitForClickable(String locator, int timeoutSeconds);

    /**
     * Wait for element to be present in DOM
     *
     * @param locator Element locator
     * @param timeoutSeconds Maximum wait time in seconds
     */
    void waitForPresent(String locator, int timeoutSeconds);

    /**
     * Implicit wait - applies to all element lookups
     *
     * @param timeoutSeconds Wait time in seconds
     */
    void setImplicitWait(int timeoutSeconds);

    // ============================================================
    // FRAMES & WINDOWS
    // ============================================================

    /**
     * Switch to frame by index
     *
     * @param frameIndex Zero-based frame index
     */
    void switchToFrame(int frameIndex);

    /**
     * Switch to frame by name or ID
     *
     * @param frameNameOrId Frame name or ID
     */
    void switchToFrame(String frameNameOrId);

    /**
     * Switch back to default content (main page)
     */
    void switchToDefaultContent();

    /**
     * Switch to window by handle
     *
     * @param windowHandle Window handle string
     */
    void switchToWindow(String windowHandle);

    /**
     * Get all window handles
     *
     * @return List of window handles
     */
    List<String> getWindowHandles();

    // ============================================================
    // ALERTS
    // ============================================================

    /**
     * Accept alert (click OK)
     */
    void acceptAlert();

    /**
     * Dismiss alert (click Cancel)
     */
    void dismissAlert();

    /**
     * Get alert text
     *
     * @return Alert message text
     */
    String getAlertText();

    /**
     * Type text into alert prompt
     *
     * @param text Text to type
     */
    void typeIntoAlert(String text);

    // ============================================================
    // JAVASCRIPT EXECUTION
    // ============================================================

    /**
     * Execute JavaScript
     *
     * @param script JavaScript code to execute
     * @return Script execution result
     */
    Object executeScript(String script);

    /**
     * Execute JavaScript with arguments
     *
     * @param script JavaScript code to execute
     * @param args Arguments to pass to script
     * @return Script execution result
     */
    Object executeScript(String script, Object... args);

    // ============================================================
    // SCREENSHOTS
    // ============================================================

    /**
     * Take screenshot and save to file
     *
     * @param filePath Path where screenshot should be saved
     * @return true if screenshot saved successfully
     */
    boolean takeScreenshot(String filePath);

    /**
     * Take screenshot of specific element
     *
     * @param locator Element locator
     * @param filePath Path where screenshot should be saved
     * @return true if screenshot saved successfully
     */
    boolean takeElementScreenshot(String locator, String filePath);

    // ============================================================
    // COOKIES
    // ============================================================

    /**
     * Add a cookie
     *
     * @param name Cookie name
     * @param value Cookie value
     */
    void addCookie(String name, String value);

    /**
     * Get cookie value by name
     *
     * @param name Cookie name
     * @return Cookie value
     */
    String getCookie(String name);

    /**
     * Delete cookie by name
     *
     * @param name Cookie name
     */
    void deleteCookie(String name);

    /**
     * Delete all cookies
     */
    void deleteAllCookies();

    // ============================================================
    // ADVANCED ACTIONS
    // ============================================================

    /**
     * Hover over an element
     *
     * @param locator Element locator
     */
    void hoverOver(String locator);

    /**
     * Double click an element
     *
     * @param locator Element locator
     */
    void doubleClick(String locator);

    /**
     * Right click an element
     *
     * @param locator Element locator
     */
    void rightClick(String locator);

    /**
     * Drag element from source to target
     *
     * @param sourceLocator Source element locator
     * @param targetLocator Target element locator
     */
    void dragAndDrop(String sourceLocator, String targetLocator);

    // ============================================================
    // UTILITY METHODS
    // ============================================================

    /**
     * Get the adapter type (selenium, playwright, etc.)
     *
     * @return Adapter type name
     */
    String getAdapterType();

    /**
     * Check if browser is currently active
     *
     * @return true if browser is active
     */
    boolean isActive();

    /**
     * Get underlying driver instance (for framework-specific operations)
     *
     * @return Native driver instance (WebDriver or Playwright)
     */
    Object getNativeDriver();
}

