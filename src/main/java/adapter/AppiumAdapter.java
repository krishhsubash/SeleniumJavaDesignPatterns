package adapter;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.InteractsWithApps;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.*;

/**
 * Appium Adapter Implementation
 *
 * Adapts Appium's AppiumDriver to work with the WebAutomationAdapter interface.
 * Supports both Android and iOS mobile automation.
 *
 * <h3>Design Pattern: ADAPTER</h3>
 *
 * <h4>Purpose:</h4>
 * Provides a unified interface for mobile automation using Appium,
 * allowing tests to run on Android and iOS without changing test code.
 *
 * <h4>Features:</h4>
 * - Android automation via AndroidDriver
 * - iOS automation via IOSDriver
 * - Mobile-specific gestures (swipe, tap, scroll)
 * - App installation and management
 * - Context switching (Native/WebView)
 * - Touch actions and gestures
 *
 * <h4>Usage:</h4>
 * <pre>
 * // Create Android adapter
 * WebAutomationAdapter adapter = AdapterFactory.createAdapter("appium", "android", false);
 * adapter.navigateTo("com.example.app"); // Launch app
 * adapter.click("id=loginButton");
 * adapter.type("id=username", "testuser");
 *
 * // Create iOS adapter
 * WebAutomationAdapter adapter = AdapterFactory.createAdapter("appium", "ios", false);
 * adapter.navigateTo("com.example.app");
 * </pre>
 *
 * @author Selenium Test Automation Framework
 * @version 1.0
 */
public class AppiumAdapter implements WebAutomationAdapter {

    private AppiumDriver driver;
    private WebDriverWait wait;
    private String platform; // "android" or "ios"
    private String appiumServerUrl = "http://localhost:4723";

    /**
     * Initialize browser/app with specified device type and mode
     *
     * @param deviceType Device type: "android", "ios", "android-emulator", "ios-simulator"
     * @param headless Not applicable for mobile (ignored)
     */
    @Override
    public void initializeBrowser(String deviceType, boolean headless) {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();

            if (deviceType.toLowerCase().contains("android")) {
                initializeAndroid(caps, deviceType);
            } else if (deviceType.toLowerCase().contains("ios")) {
                initializeIOS(caps, deviceType);
            } else {
                throw new IllegalArgumentException("Unsupported device type: " + deviceType +
                        ". Use 'android' or 'ios'");
            }

            System.out.println("✓ AppiumAdapter initialized: " + platform + " on " + deviceType);

        } catch (Exception e) {
            System.err.println("❌ Failed to initialize Appium: " + e.getMessage());
            throw new RuntimeException("Appium initialization failed", e);
        }
    }

    /**
     * Initialize Android driver
     */
    private void initializeAndroid(DesiredCapabilities caps, String deviceType) throws Exception {
        platform = "android";

        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "UiAutomator2");

        if (deviceType.contains("emulator")) {
            caps.setCapability("deviceName", "Android Emulator");
            caps.setCapability("avd", "Pixel_4_API_30"); // Default AVD
        } else {
            caps.setCapability("deviceName", "Android Device");
        }

        // Add more capabilities as needed
        caps.setCapability("platformVersion", "11.0");
        caps.setCapability("autoGrantPermissions", true);
        caps.setCapability("noReset", false);

        driver = new AndroidDriver(new URL(appiumServerUrl), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("✓ AndroidDriver created successfully");
    }

    /**
     * Initialize iOS driver
     */
    private void initializeIOS(DesiredCapabilities caps, String deviceType) throws Exception {
        platform = "ios";

        caps.setCapability("platformName", "iOS");
        caps.setCapability("automationName", "XCUITest");

        if (deviceType.contains("simulator")) {
            caps.setCapability("deviceName", "iPhone 14");
        } else {
            caps.setCapability("deviceName", "iPhone");
            caps.setCapability("udid", "auto"); // Auto-detect connected device
        }

        caps.setCapability("platformVersion", "16.0");
        caps.setCapability("noReset", false);

        driver = new IOSDriver(new URL(appiumServerUrl), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("✓ IOSDriver created successfully");
    }

    /**
     * Launch app or navigate to URL (for hybrid apps)
     *
     * @param urlOrPackage App package name (Android) or Bundle ID (iOS) or URL
     */
    @Override
    public void navigateTo(String urlOrPackage) {
        try {
            if (urlOrPackage.startsWith("http")) {
                // Navigate to URL in WebView
                driver.get(urlOrPackage);
            } else {
                // Launch app by package/bundle ID
                if (driver instanceof InteractsWithApps) {
                    ((InteractsWithApps) driver).activateApp(urlOrPackage);
                }
            }
            System.out.println("✓ Launched: " + urlOrPackage);
        } catch (Exception e) {
            System.err.println("❌ Navigation failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Click element with mobile-optimized handling
     */
    @Override
    public void click(String locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(findElement(locator)));
            element.click();
            System.out.println("✓ Clicked: " + locator);
        } catch (Exception e) {
            System.err.println("❌ Click failed: " + locator);
            takeScreenshot("click-error-" + System.currentTimeMillis());
            throw e;
        }
    }

    /**
     * Type text with mobile keyboard
     */
    @Override
    public void type(String locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(parseLocator(locator)));
            element.clear();
            element.sendKeys(text);
            hideKeyboard(); // Mobile-specific
            System.out.println("✓ Typed into: " + locator);
        } catch (Exception e) {
            System.err.println("❌ Type failed: " + locator);
            throw e;
        }
    }

    /**
     * Get text from element
     */
    @Override
    public String getText(String locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(parseLocator(locator)));
            String text = element.getText();
            System.out.println("✓ Got text: " + text);
            return text;
        } catch (Exception e) {
            System.err.println("❌ Get text failed: " + locator);
            return "";
        }
    }

    /**
     * Check if element is displayed
     */
    @Override
    public boolean isDisplayed(String locator) {
        try {
            WebElement element = findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    @Override
    public boolean isEnabled(String locator) {
        try {
            WebElement element = findElement(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get attribute value
     */
    @Override
    public String getAttribute(String locator, String attribute) {
        try {
            WebElement element = findElement(locator);
            return element.getAttribute(attribute);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Execute JavaScript (for hybrid apps in WebView context)
     */
    @Override
    public Object executeScript(String script) {
        return executeScript(script, new Object[]{});
    }

    /**
     * Execute JavaScript with arguments (for hybrid apps in WebView context)
     */
    @Override
    public Object executeScript(String script, Object... args) {
        try {
            return ((JavascriptExecutor) driver).executeScript(script, args);
        } catch (Exception e) {
            System.err.println("❌ Script execution failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Take screenshot
     */
    @Override
    public boolean takeScreenshot(String filePath) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            destination.getParentFile().mkdirs();
            org.apache.commons.io.FileUtils.copyFile(screenshot, destination);
            System.out.println("✓ Screenshot saved: " + filePath);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Screenshot failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get current page title (for hybrid apps)
     */
    @Override
    public String getPageTitle() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Get current URL or app package
     */
    @Override
    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            return getCurrentContext(); // Return current context for native apps
        }
    }

    /**
     * Switch to frame (for hybrid apps)
     */
    @Override
    public void switchToFrame(String frameLocator) {
        try {
            driver.switchTo().frame(findElement(frameLocator));
        } catch (Exception e) {
            System.err.println("❌ Frame switch failed: " + e.getMessage());
        }
    }

    /**
     * Switch to default content
     */
    @Override
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    /**
     * Get window handles
     */
    @Override
    public List<String> getWindowHandles() {
        return new ArrayList<>(driver.getWindowHandles());
    }

    /**
     * Switch to window
     */
    @Override
    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    /**
     * Accept alert (if present)
     */
    @Override
    public void acceptAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent()).accept();
        } catch (Exception e) {
            System.out.println("ℹ️ No alert present");
        }
    }

    /**
     * Dismiss alert
     */
    @Override
    public void dismissAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent()).dismiss();
        } catch (Exception e) {
            System.out.println("ℹ️ No alert present");
        }
    }

    /**
     * Get alert text
     */
    @Override
    public String getAlertText() {
        try {
            return wait.until(ExpectedConditions.alertIsPresent()).getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Find element with locator parsing
     */
    public WebElement findElement(String locator) {
        return driver.findElement(parseLocator(locator));
    }

    /**
     * Find multiple elements
     */
    public List<WebElement> findElements(String locator) {
        return driver.findElements(parseLocator(locator));
    }

    /**
     * Wait for element to be visible
     */
    public void waitForElementVisible(String locator, int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(parseLocator(locator)));
    }

    /**
     * Wait for element to be clickable
     */
    public void waitForElementClickable(String locator, int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.elementToBeClickable(parseLocator(locator)));
    }

    /**
     * Check if adapter is active
     */
    @Override
    public boolean isActive() {
        return driver != null && driver.getSessionId() != null;
    }

    /**
     * Quit browser/app
     */
    @Override
    public void quitBrowser() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✓ Appium driver quit successfully");
            } catch (Exception e) {
                System.err.println("⚠️ Error quitting driver: " + e.getMessage());
            }
        }
    }

    // ============================================================
    // MOBILE-SPECIFIC METHODS
    // ============================================================

    /**
     * Install app on device
     *
     * @param appPath Path to .apk (Android) or .app/.ipa (iOS) file
     */
    public void installApp(String appPath) {
        try {
            if (driver instanceof InteractsWithApps) {
                ((InteractsWithApps) driver).installApp(appPath);
                System.out.println("✓ App installed: " + appPath);
            }
        } catch (Exception e) {
            System.err.println("❌ App installation failed: " + e.getMessage());
        }
    }

    /**
     * Remove app from device
     *
     * @param bundleId App bundle identifier
     */
    public void removeApp(String bundleId) {
        try {
            if (driver instanceof InteractsWithApps) {
                ((InteractsWithApps) driver).removeApp(bundleId);
                System.out.println("✓ App removed: " + bundleId);
            }
        } catch (Exception e) {
            System.err.println("❌ App removal failed: " + e.getMessage());
        }
    }

    /**
     * Launch app
     *
     * @param bundleId App bundle identifier
     */
    public void launchApp(String bundleId) {
        try {
            if (driver instanceof InteractsWithApps) {
                ((InteractsWithApps) driver).activateApp(bundleId);
                System.out.println("✓ App launched: " + bundleId);
            }
        } catch (Exception e) {
            System.err.println("❌ App launch failed: " + e.getMessage());
        }
    }

    /**
     * Close app (background it)
     */
    public void closeApp() {
        try {
            if (driver instanceof InteractsWithApps) {
                ((InteractsWithApps) driver).terminateApp(
                        platform.equals("android") ?
                                driver.getCapabilities().getCapability("appPackage").toString() :
                                driver.getCapabilities().getCapability("bundleId").toString()
                );
                System.out.println("✓ App closed");
            }
        } catch (Exception e) {
            System.err.println("❌ App close failed: " + e.getMessage());
        }
    }

    /**
     * Hide keyboard
     */
    public void hideKeyboard() {
        try {
            if (platform.equals("android") && driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).hideKeyboard();
            } else if (platform.equals("ios") && driver instanceof IOSDriver) {
                ((IOSDriver) driver).hideKeyboard();
            }
        } catch (Exception e) {
            // Keyboard not visible, ignore
        }
    }

    /**
     * Swipe gesture
     *
     * @param startX Start X coordinate
     * @param startY Start Y coordinate
     * @param endX End X coordinate
     * @param endY End Y coordinate
     */
    public void swipe(int startX, int startY, int endX, int endY) {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);

            swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                    PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(500),
                    PointerInput.Origin.viewport(), endX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Arrays.asList(swipe));
            System.out.println("✓ Swiped from (" + startX + "," + startY + ") to (" + endX + "," + endY + ")");
        } catch (Exception e) {
            System.err.println("❌ Swipe failed: " + e.getMessage());
        }
    }

    /**
     * Tap at coordinates
     */
    public void tap(int x, int y) {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);

            tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
                    PointerInput.Origin.viewport(), x, y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Arrays.asList(tap));
            System.out.println("✓ Tapped at (" + x + "," + y + ")");
        } catch (Exception e) {
            System.err.println("❌ Tap failed: " + e.getMessage());
        }
    }

    /**
     * Switch context (NATIVE_APP / WEBVIEW)
     */
    public void switchContext(String contextName) {
        try {
            // Use executeScript for context switching in Appium 9.0
            driver.executeScript("mobile: context", Map.of("name", contextName));
            System.out.println("✓ Switched to context: " + contextName);
        } catch (Exception e) {
            System.err.println("❌ Context switch failed: " + e.getMessage());
        }
    }

    /**
     * Get available contexts
     */
    @SuppressWarnings("unchecked")
    public Set<String> getContexts() {
        try {
            Object result = driver.executeScript("mobile: getContexts");
            if (result instanceof List) {
                return new HashSet<>((List<String>) result);
            }
            return Collections.emptySet();
        } catch (Exception e) {
            System.err.println("❌ Get contexts failed: " + e.getMessage());
            return Collections.emptySet();
        }
    }

    /**
     * Get current context
     */
    public String getCurrentContext() {
        try {
            Object result = driver.executeScript("mobile: getContext");
            return result != null ? result.toString() : "NATIVE_APP";
        } catch (Exception e) {
            System.err.println("❌ Get current context failed: " + e.getMessage());
            return "NATIVE_APP";
        }
    }

    /**
     * Get Appium driver instance
     */
    public AppiumDriver getDriver() {
        return driver;
    }

    /**
     * Set Appium server URL
     */
    public void setAppiumServerUrl(String url) {
        this.appiumServerUrl = url;
    }

    // ============================================================
    // MISSING INTERFACE METHODS (Mobile-optimized implementations)
    // ============================================================

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
        try {
            String context = getCurrentContext();
            if (context != null && context.contains("WEBVIEW")) {
                driver.navigate().refresh();
            }
        } catch (Exception e) {
            // If not in WEBVIEW context, skip refresh
        }
    }

    @Override
    public void closeBrowser() {
        if (driver != null) {
            driver.close();
        }
    }

    @Override
    public void clear(String locator) {
        findElement(locator).clear();
    }

    @Override
    public void submit(String locator) {
        findElement(locator).submit();
    }

    @Override
    public boolean isSelected(String locator) {
        try {
            return findElement(locator).isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void selectByText(String locator, String text) {
        // Not commonly used in mobile, but implementing for completeness
        System.out.println("⚠️ selectByText not commonly used in mobile apps");
    }

    @Override
    public void selectByValue(String locator, String value) {
        // Not commonly used in mobile, but implementing for completeness
        System.out.println("⚠️ selectByValue not commonly used in mobile apps");
    }

    @Override
    public void selectByIndex(String locator, int index) {
        // Not commonly used in mobile, but implementing for completeness
        System.out.println("⚠️ selectByIndex not commonly used in mobile apps");
    }

    @Override
    public void waitForVisible(String locator, int timeoutSeconds) {
        waitForElementVisible(locator, timeoutSeconds);
    }

    @Override
    public void waitForClickable(String locator, int timeoutSeconds) {
        waitForElementClickable(locator, timeoutSeconds);
    }

    @Override
    public void waitForPresent(String locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(parseLocator(locator)));
    }

    @Override
    public void setImplicitWait(int timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSeconds));
    }

    @Override
    public void switchToFrame(int frameIndex) {
        driver.switchTo().frame(frameIndex);
    }

    @Override
    public void typeIntoAlert(String text) {
        try {
            driver.switchTo().alert().sendKeys(text);
        } catch (Exception e) {
            System.err.println("❌ Alert not present: " + e.getMessage());
        }
    }

    @Override
    public boolean takeElementScreenshot(String locator, String filePath) {
        try {
            WebElement element = findElement(locator);
            File screenshot = ((TakesScreenshot) element).getScreenshotAs(OutputType.FILE);
            org.apache.commons.io.FileUtils.copyFile(screenshot, new File(filePath));
            return true;
        } catch (Exception e) {
            System.err.println("❌ Element screenshot failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void addCookie(String name, String value) {
        driver.manage().addCookie(new Cookie(name, value));
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

    @Override
    public void hoverOver(String locator) {
        // Mobile doesn't support hover, implement as tap
        click(locator);
    }

    @Override
    public void doubleClick(String locator) {
        // Simulate double click with two taps
        WebElement element = findElement(locator);
        Point location = element.getLocation();
        tap(location.getX() + element.getSize().getWidth() / 2,
                location.getY() + element.getSize().getHeight() / 2);
        tap(location.getX() + element.getSize().getWidth() / 2,
                location.getY() + element.getSize().getHeight() / 2);
    }

    @Override
    public void rightClick(String locator) {
        // Mobile doesn't support right click, implement as long press
        System.out.println("⚠️ Right click not supported on mobile - using regular click");
        click(locator);
    }

    @Override
    public void dragAndDrop(String sourceLocator, String targetLocator) {
        try {
            WebElement source = findElement(sourceLocator);
            WebElement target = findElement(targetLocator);

            Point sourceLocation = source.getLocation();
            Dimension sourceSize = source.getSize();
            Point targetLocation = target.getLocation();
            Dimension targetSize = target.getSize();

            int startX = sourceLocation.getX() + sourceSize.getWidth() / 2;
            int startY = sourceLocation.getY() + sourceSize.getHeight() / 2;
            int endX = targetLocation.getX() + targetSize.getWidth() / 2;
            int endY = targetLocation.getY() + targetSize.getHeight() / 2;

            swipe(startX, startY, endX, endY);
        } catch (Exception e) {
            System.err.println("❌ Drag and drop failed: " + e.getMessage());
        }
    }

    @Override
    public String getAdapterType() {
        return "appium";
    }

    @Override
    public Object getNativeDriver() {
        return driver;
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    /**
     * Parse locator string to By object
     * Supports: id=, name=, xpath=, class=, css=, accessibility=
     */
    private By parseLocator(String locator) {
        if (locator.startsWith("id=")) {
            return By.id(locator.substring(3));
        } else if (locator.startsWith("name=")) {
            return By.name(locator.substring(5));
        } else if (locator.startsWith("xpath=")) {
            return By.xpath(locator.substring(6));
        } else if (locator.startsWith("class=")) {
            return By.className(locator.substring(6));
        } else if (locator.startsWith("css=")) {
            return By.cssSelector(locator.substring(4));
        } else if (locator.startsWith("accessibility=") || locator.startsWith("acc=")) {
            String id = locator.startsWith("accessibility=")
                    ? locator.substring(14)
                    : locator.substring(4);
            return org.openqa.selenium.By.xpath("//*[@content-desc='" + id + "']");
        } else {
            // Default to XPath
            return By.xpath(locator);
        }
    }
}
