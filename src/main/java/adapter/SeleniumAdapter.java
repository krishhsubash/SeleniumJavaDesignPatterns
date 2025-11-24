package adapter;

import driver.browserFactory.BrowserFactory;
import driver.browserFactory.ChromeDriverFactory;
import driver.browserFactory.EdgeDriverFactory;
import driver.browserFactory.FirefoxDriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Selenium WebDriver Adapter
 *
 * Adapts Selenium WebDriver API to conform to WebAutomationAdapter interface.
 * This allows Selenium-based tests to be executed through the unified adapter API.
 *
 * <h3>Design Pattern: ADAPTER</h3>
 *
 * <h4>Role:</h4>
 * Concrete Adapter - wraps Selenium WebDriver (Adaptee) to implement
 * the WebAutomationAdapter interface (Target).
 *
 * <h4>How it works:</h4>
 * <pre>
 * Client calls: adapter.click("css=#submitBtn")
 * SeleniumAdapter translates to: driver.findElement(By.cssSelector("#submitBtn")).click()
 * </pre>
 *
 * <h4>Locator Strategy:</h4>
 * Supports multiple locator strategies with prefixes:
 * - css=      → By.cssSelector
 * - xpath=    → By.xpath
 * - id=       → By.id
 * - name=     → By.name
 * - class=    → By.className
 * - tag=      → By.tagName
 * - link=     → By.linkText
 * - partial=  → By.partialLinkText
 * - default   → By.cssSelector (if no prefix)
 *
 * @author Selenium Test Automation Framework
 * @version 1.0
 */
public class SeleniumAdapter implements WebAutomationAdapter {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private static final int DEFAULT_TIMEOUT = 10;

    /**
     * Default constructor - gets driver from DriverManager Singleton
     */
    public SeleniumAdapter() {
        // Driver will be initialized later
    }

    /**
     * Constructor with existing WebDriver instance
     *
     * @param driver Existing WebDriver instance
     */
    public SeleniumAdapter(WebDriver driver) {
        this.driver = driver;
        initializeHelpers();
    }

    /**
     * Initialize helper objects (wait, actions)
     */
    private void initializeHelpers() {
        if (driver != null) {
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
            this.actions = new Actions(driver);
        }
    }

    /**
     * Parse locator string and create appropriate By locator
     *
     * @param locator Locator string with strategy prefix
     * @return By locator object
     */
    private By parseLocator(String locator) {
        if (locator.startsWith("css=")) {
            return By.cssSelector(locator.substring(4));
        } else if (locator.startsWith("xpath=")) {
            return By.xpath(locator.substring(6));
        } else if (locator.startsWith("id=")) {
            return By.id(locator.substring(3));
        } else if (locator.startsWith("name=")) {
            return By.name(locator.substring(5));
        } else if (locator.startsWith("class=")) {
            return By.className(locator.substring(6));
        } else if (locator.startsWith("tag=")) {
            return By.tagName(locator.substring(4));
        } else if (locator.startsWith("link=")) {
            return By.linkText(locator.substring(5));
        } else if (locator.startsWith("partial=")) {
            return By.partialLinkText(locator.substring(8));
        } else {
            // Default to CSS selector
            return By.cssSelector(locator);
        }
    }

    /**
     * Find element with parsed locator
     *
     * @param locator Element locator string
     * @return WebElement
     */
    private WebElement findElement(String locator) {
        return driver.findElement(parseLocator(locator));
    }

    // ============================================================
    // BROWSER LIFECYCLE
    // ============================================================

    @Override
    public void initializeBrowser(String browserType, boolean headless) {
        // Create appropriate browser factory
        BrowserFactory factory;
        switch (browserType.toLowerCase()) {
            case "firefox":
                factory = new FirefoxDriverFactory();
                break;
            case "edge":
                factory = new EdgeDriverFactory();
                break;
            case "chrome":
            default:
                factory = new ChromeDriverFactory();
                break;
        }

        this.driver = factory.createDriver(headless);
        initializeHelpers();
        System.out.println("✓ SeleniumAdapter: Initialized " + browserType + " browser (headless=" + headless + ")");
    }

    @Override
    public void closeBrowser() {
        if (driver != null) {
            driver.close();
            System.out.println("✓ SeleniumAdapter: Closed browser window");
        }
    }

    @Override
    public void quitBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("✓ SeleniumAdapter: Quit browser");
        }
    }

    // ============================================================
    // NAVIGATION
    // ============================================================

    @Override
    public void navigateTo(String url) {
        driver.get(url);
        System.out.println("✓ SeleniumAdapter: Navigated to " + url);
    }

    @Override
    public void navigateBack() {
        driver.navigate().back();
    }

    @Override
    public void navigateForward() {
        driver.navigate().forward();
    }

    @Override
    public void refreshPage() {
        driver.navigate().refresh();
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getPageTitle() {
        return driver.getTitle();
    }

    // ============================================================
    // ELEMENT INTERACTION
    // ============================================================

    @Override
    public void click(String locator) {
        findElement(locator).click();
        System.out.println("✓ SeleniumAdapter: Clicked " + locator);
    }

    @Override
    public void type(String locator, String text) {
        findElement(locator).sendKeys(text);
        System.out.println("✓ SeleniumAdapter: Typed '" + text + "' into " + locator);
    }

    @Override
    public void clear(String locator) {
        findElement(locator).clear();
    }

    @Override
    public void submit(String locator) {
        findElement(locator).submit();
    }

    // ============================================================
    // ELEMENT INFORMATION
    // ============================================================

    @Override
    public String getText(String locator) {
        return findElement(locator).getText();
    }

    @Override
    public String getAttribute(String locator, String attribute) {
        return findElement(locator).getAttribute(attribute);
    }

    @Override
    public boolean isDisplayed(String locator) {
        try {
            return findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public boolean isEnabled(String locator) {
        return findElement(locator).isEnabled();
    }

    @Override
    public boolean isSelected(String locator) {
        return findElement(locator).isSelected();
    }

    // ============================================================
    // DROPDOWN/SELECT
    // ============================================================

    @Override
    public void selectByText(String locator, String text) {
        Select select = new Select(findElement(locator));
        select.selectByVisibleText(text);
    }

    @Override
    public void selectByValue(String locator, String value) {
        Select select = new Select(findElement(locator));
        select.selectByValue(value);
    }

    @Override
    public void selectByIndex(String locator, int index) {
        Select select = new Select(findElement(locator));
        select.selectByIndex(index);
    }

    // ============================================================
    // WAITS
    // ============================================================

    @Override
    public void waitForVisible(String locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        customWait.until(ExpectedConditions.visibilityOfElementLocated(parseLocator(locator)));
    }

    @Override
    public void waitForClickable(String locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        customWait.until(ExpectedConditions.elementToBeClickable(parseLocator(locator)));
    }

    @Override
    public void waitForPresent(String locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        customWait.until(ExpectedConditions.presenceOfElementLocated(parseLocator(locator)));
    }

    @Override
    public void setImplicitWait(int timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSeconds));
    }

    // ============================================================
    // FRAMES & WINDOWS
    // ============================================================

    @Override
    public void switchToFrame(int frameIndex) {
        driver.switchTo().frame(frameIndex);
    }

    @Override
    public void switchToFrame(String frameNameOrId) {
        driver.switchTo().frame(frameNameOrId);
    }

    @Override
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    @Override
    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    @Override
    public List<String> getWindowHandles() {
        Set<String> handles = driver.getWindowHandles();
        return new ArrayList<>(handles);
    }

    // ============================================================
    // ALERTS
    // ============================================================

    @Override
    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    @Override
    public void dismissAlert() {
        driver.switchTo().alert().dismiss();
    }

    @Override
    public String getAlertText() {
        return driver.switchTo().alert().getText();
    }

    @Override
    public void typeIntoAlert(String text) {
        driver.switchTo().alert().sendKeys(text);
    }

    // ============================================================
    // JAVASCRIPT EXECUTION
    // ============================================================

    @Override
    public Object executeScript(String script) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    // ============================================================
    // SCREENSHOTS
    // ============================================================

    @Override
    public boolean takeScreenshot(String filePath) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileHandler.copy(srcFile, destFile);
            System.out.println("✓ SeleniumAdapter: Screenshot saved to " + filePath);
            return true;
        } catch (Exception e) {
            System.err.println("✗ SeleniumAdapter: Failed to take screenshot - " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean takeElementScreenshot(String locator, String filePath) {
        try {
            WebElement element = findElement(locator);
            File srcFile = element.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileHandler.copy(srcFile, destFile);
            System.out.println("✓ SeleniumAdapter: Element screenshot saved to " + filePath);
            return true;
        } catch (Exception e) {
            System.err.println("✗ SeleniumAdapter: Failed to take element screenshot - " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // COOKIES
    // ============================================================

    @Override
    public void addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        driver.manage().addCookie(cookie);
    }

    @Override
    public String getCookie(String name) {
        Cookie cookie = driver.manage().getCookieNamed(name);
        return cookie != null ? cookie.getValue() : null;
    }

    @Override
    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }

    @Override
    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    // ============================================================
    // ADVANCED ACTIONS
    // ============================================================

    @Override
    public void hoverOver(String locator) {
        WebElement element = findElement(locator);
        actions.moveToElement(element).perform();
    }

    @Override
    public void doubleClick(String locator) {
        WebElement element = findElement(locator);
        actions.doubleClick(element).perform();
    }

    @Override
    public void rightClick(String locator) {
        WebElement element = findElement(locator);
        actions.contextClick(element).perform();
    }

    @Override
    public void dragAndDrop(String sourceLocator, String targetLocator) {
        WebElement source = findElement(sourceLocator);
        WebElement target = findElement(targetLocator);
        actions.dragAndDrop(source, target).perform();
    }

    // ============================================================
    // UTILITY METHODS
    // ============================================================

    @Override
    public String getAdapterType() {
        return "Selenium WebDriver";
    }

    @Override
    public boolean isActive() {
        return driver != null;
    }

    @Override
    public Object getNativeDriver() {
        return driver;
    }
}

