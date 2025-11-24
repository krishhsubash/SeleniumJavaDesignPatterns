package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base Page Object Model class
 *
 * Implements common page functionality using Page Factory pattern
 * All page objects should extend this base class
 *
 * Design Patterns:
 * - Page Object Model: Encapsulates page structure and behavior
 * - Page Factory: Lazy initialization of web elements using @FindBy
 *
 * Features:
 * - Automatic PageFactory initialization
 * - Common wait utilities
 * - Element visibility checks
 * - Click, type, and navigation helpers
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor - initializes PageFactory for the page
     *
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // PAGE FACTORY: Initialize elements with @FindBy annotations
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for element to be visible
     *
     * @param element WebElement to wait for
     * @return WebElement once visible
     */
    protected WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be clickable
     *
     * @param element WebElement to wait for
     * @return WebElement once clickable
     */
    protected WebElement waitForElementClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Check if element is displayed
     *
     * @param element WebElement to check
     * @return true if displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Safe click on element with wait
     *
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        waitForElementClickable(element).click();
    }

    /**
     * Safe type into element with wait and clear
     *
     * @param element WebElement to type into
     * @param text Text to enter
     */
    protected void type(WebElement element, String text) {
        WebElement visibleElement = waitForElementVisible(element);
        visibleElement.clear();
        visibleElement.sendKeys(text);
    }

    /**
     * Get text from element with wait
     *
     * @param element WebElement to get text from
     * @return Element text
     */
    protected String getText(WebElement element) {
        return waitForElementVisible(element).getText();
    }

    /**
     * Get current page URL
     *
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get current page title
     *
     * @return Page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Navigate to URL
     *
     * @param url URL to navigate to
     */
    protected void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Wait for page title to contain text
     *
     * @param title Title text to wait for
     */
    protected void waitForTitleContains(String title) {
        wait.until(ExpectedConditions.titleContains(title));
    }

    /**
     * Wait for URL to contain text
     *
     * @param urlFragment URL fragment to wait for
     */
    protected void waitForUrlContains(String urlFragment) {
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    /**
     * Abstract method - Each page must provide its page name
     * Used for logging and identification
     *
     * @return Page name
     */
    public abstract String getPageName();

    /**
     * Log page action
     *
     * @param action Action description
     */
    protected void log(String action) {
        System.out.println("[" + getPageName() + "] " + action);
    }
}

