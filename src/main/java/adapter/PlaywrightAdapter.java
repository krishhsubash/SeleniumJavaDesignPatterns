package adapter;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.Cookie;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Playwright Adapter
 *
 * Adapts Playwright API to conform to WebAutomationAdapter interface.
 * This allows Playwright-based tests to be executed through the unified adapter API.
 *
 * <h3>Design Pattern: ADAPTER</h3>
 *
 * <h4>Role:</h4>
 * Concrete Adapter - wraps Playwright (Adaptee) to implement
 * the WebAutomationAdapter interface (Target).
 *
 * <h4>How it works:</h4>
 * <pre>
 * Client calls: adapter.click("css=#submitBtn")
 * PlaywrightAdapter translates to: page.locator("#submitBtn").click()
 * </pre>
 *
 * <h4>Key Differences from Selenium:</h4>
 * - Playwright uses auto-waiting (waits for element to be actionable)
 * - Locators are lazy (evaluated at action time)
 * - More modern API with promises/async patterns
 * - Better network interception capabilities
 *
 * <h4>Locator Strategy:</h4>
 * Supports multiple locator strategies with prefixes:
 * - css=      → CSS selector
 * - xpath=    → XPath selector
 * - id=       → #id CSS selector
 * - text=     → text selector
 * - default   → CSS selector (if no prefix)
 *
 * @author Selenium Test Automation Framework
 * @version 1.0
 */
public class PlaywrightAdapter implements WebAutomationAdapter {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private int defaultTimeout = 10000; // 10 seconds in milliseconds

    /**
     * Default constructor
     */
    public PlaywrightAdapter() {
        // Browser will be initialized later
    }

    /**
     * Constructor with existing Playwright page instance
     *
     * @param page Existing Playwright Page instance
     */
    public PlaywrightAdapter(Page page) {
        this.page = page;
    }

    /**
     * Parse locator string and create Playwright selector
     *
     * @param locator Locator string with strategy prefix
     * @return Playwright selector string
     */
    private String parseLocator(String locator) {
        if (locator.startsWith("css=")) {
            return locator.substring(4);
        } else if (locator.startsWith("xpath=")) {
            return locator.substring(6);
        } else if (locator.startsWith("id=")) {
            return "#" + locator.substring(3);
        } else if (locator.startsWith("text=")) {
            return "text=" + locator.substring(5);
        } else if (locator.startsWith("name=")) {
            return "[name='" + locator.substring(5) + "']";
        } else if (locator.startsWith("class=")) {
            return "." + locator.substring(6);
        } else {
            // Default to CSS selector
            return locator;
        }
    }

    /**
     * Get locator for element
     *
     * @param locator Locator string
     * @return Playwright Locator
     */
    private Locator getLocator(String locator) {
        return page.locator(parseLocator(locator));
    }

    // ============================================================
    // BROWSER LIFECYCLE
    // ============================================================

    @Override
    public void initializeBrowser(String browserType, boolean headless) {
        playwright = Playwright.create();

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless);

        switch (browserType.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
            case "safari":
                browser = playwright.webkit().launch(options);
                break;
            case "chrome":
            case "chromium":
            default:
                browser = playwright.chromium().launch(options);
                break;
        }

        context = browser.newContext();
        page = context.newPage();
        page.setDefaultTimeout(defaultTimeout);

        System.out.println("✓ PlaywrightAdapter: Initialized " + browserType + " browser (headless=" + headless + ")");
    }

    @Override
    public void closeBrowser() {
        if (page != null) {
            page.close();
            System.out.println("✓ PlaywrightAdapter: Closed page");
        }
    }

    @Override
    public void quitBrowser() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        System.out.println("✓ PlaywrightAdapter: Quit browser");
    }

    // ============================================================
    // NAVIGATION
    // ============================================================

    @Override
    public void navigateTo(String url) {
        page.navigate(url);
        System.out.println("✓ PlaywrightAdapter: Navigated to " + url);
    }

    @Override
    public void navigateBack() {
        page.goBack();
    }

    @Override
    public void navigateForward() {
        page.goForward();
    }

    @Override
    public void refreshPage() {
        page.reload();
    }

    @Override
    public String getCurrentUrl() {
        return page.url();
    }

    @Override
    public String getPageTitle() {
        return page.title();
    }

    // ============================================================
    // ELEMENT INTERACTION
    // ============================================================

    @Override
    public void click(String locator) {
        getLocator(locator).click();
        System.out.println("✓ PlaywrightAdapter: Clicked " + locator);
    }

    @Override
    public void type(String locator, String text) {
        getLocator(locator).fill(text);
        System.out.println("✓ PlaywrightAdapter: Typed '" + text + "' into " + locator);
    }

    @Override
    public void clear(String locator) {
        getLocator(locator).fill("");
    }

    @Override
    public void submit(String locator) {
        // Playwright doesn't have submit, use click instead
        getLocator(locator).click();
    }

    // ============================================================
    // ELEMENT INFORMATION
    // ============================================================

    @Override
    public String getText(String locator) {
        return getLocator(locator).textContent();
    }

    @Override
    public String getAttribute(String locator, String attribute) {
        return getLocator(locator).getAttribute(attribute);
    }

    @Override
    public boolean isDisplayed(String locator) {
        try {
            return getLocator(locator).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isEnabled(String locator) {
        return getLocator(locator).isEnabled();
    }

    @Override
    public boolean isSelected(String locator) {
        return getLocator(locator).isChecked();
    }

    // ============================================================
    // DROPDOWN/SELECT
    // ============================================================

    @Override
    public void selectByText(String locator, String text) {
        getLocator(locator).selectOption(new SelectOption().setLabel(text));
    }

    @Override
    public void selectByValue(String locator, String value) {
        getLocator(locator).selectOption(new SelectOption().setValue(value));
    }

    @Override
    public void selectByIndex(String locator, int index) {
        getLocator(locator).selectOption(new SelectOption().setIndex(index));
    }

    // ============================================================
    // WAITS
    // ============================================================

    @Override
    public void waitForVisible(String locator, int timeoutSeconds) {
        getLocator(locator).waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                .setTimeout(timeoutSeconds * 1000.0));
    }

    @Override
    public void waitForClickable(String locator, int timeoutSeconds) {
        // Playwright auto-waits for actionability
        getLocator(locator).waitFor(new Locator.WaitForOptions()
                .setTimeout(timeoutSeconds * 1000.0));
    }

    @Override
    public void waitForPresent(String locator, int timeoutSeconds) {
        getLocator(locator).waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.ATTACHED)
                .setTimeout(timeoutSeconds * 1000.0));
    }

    @Override
    public void setImplicitWait(int timeoutSeconds) {
        this.defaultTimeout = timeoutSeconds * 1000;
        page.setDefaultTimeout(defaultTimeout);
    }

    // ============================================================
    // FRAMES & WINDOWS
    // ============================================================

    @Override
    public void switchToFrame(int frameIndex) {
        // Playwright handles frames differently - use frame selector
        page.frameLocator("iframe >> nth=" + frameIndex);
    }

    @Override
    public void switchToFrame(String frameNameOrId) {
        // Playwright handles frames differently
        page.frameLocator("iframe[name='" + frameNameOrId + "'], iframe#" + frameNameOrId);
    }

    @Override
    public void switchToDefaultContent() {
        // Playwright doesn't require explicit switch back
        // Context is maintained automatically
    }

    @Override
    public void switchToWindow(String windowHandle) {
        // Playwright uses pages instead of window handles
        // This is a simplified implementation
        System.out.println("⚠ PlaywrightAdapter: Window switching works differently - use page references");
    }

    @Override
    public List<String> getWindowHandles() {
        // Playwright uses pages
        List<String> handles = new ArrayList<>();
        for (Page p : context.pages()) {
            handles.add(p.url());
        }
        return handles;
    }

    // ============================================================
    // ALERTS
    // ============================================================

    @Override
    public void acceptAlert() {
        page.onDialog(dialog -> dialog.accept());
    }

    @Override
    public void dismissAlert() {
        page.onDialog(dialog -> dialog.dismiss());
    }

    @Override
    public String getAlertText() {
        // Playwright handles dialogs through listeners
        final String[] alertText = {""};
        page.onDialog(dialog -> {
            alertText[0] = dialog.message();
            dialog.accept();
        });
        return alertText[0];
    }

    @Override
    public void typeIntoAlert(String text) {
        page.onDialog(dialog -> dialog.accept(text));
    }

    // ============================================================
    // JAVASCRIPT EXECUTION
    // ============================================================

    @Override
    public Object executeScript(String script) {
        return page.evaluate(script);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        // Playwright evaluate can accept arguments
        return page.evaluate(script, args.length > 0 ? args[0] : null);
    }

    // ============================================================
    // SCREENSHOTS
    // ============================================================

    @Override
    public boolean takeScreenshot(String filePath) {
        try {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(filePath))
                    .setFullPage(true));
            System.out.println("✓ PlaywrightAdapter: Screenshot saved to " + filePath);
            return true;
        } catch (Exception e) {
            System.err.println("✗ PlaywrightAdapter: Failed to take screenshot - " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean takeElementScreenshot(String locator, String filePath) {
        try {
            getLocator(locator).screenshot(new Locator.ScreenshotOptions()
                    .setPath(Paths.get(filePath)));
            System.out.println("✓ PlaywrightAdapter: Element screenshot saved to " + filePath);
            return true;
        } catch (Exception e) {
            System.err.println("✗ PlaywrightAdapter: Failed to take element screenshot - " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // COOKIES
    // ============================================================

    @Override
    public void addCookie(String name, String value) {
        context.addCookies(java.util.Collections.singletonList(
                new Cookie(name, value)
                        .setUrl(page.url())
        ));
    }

    @Override
    public String getCookie(String name) {
        for (Cookie cookie : context.cookies()) {
            if (cookie.name.equals(name)) {
                return cookie.value;
            }
        }
        return null;
    }

    @Override
    public void deleteCookie(String name) {
        List<Cookie> cookies = context.cookies();
        cookies.removeIf(cookie -> cookie.name.equals(name));
        context.clearCookies();
        context.addCookies(cookies);
    }

    @Override
    public void deleteAllCookies() {
        context.clearCookies();
    }

    // ============================================================
    // ADVANCED ACTIONS
    // ============================================================

    @Override
    public void hoverOver(String locator) {
        getLocator(locator).hover();
    }

    @Override
    public void doubleClick(String locator) {
        getLocator(locator).dblclick();
    }

    @Override
    public void rightClick(String locator) {
        getLocator(locator).click(new Locator.ClickOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT));
    }

    @Override
    public void dragAndDrop(String sourceLocator, String targetLocator) {
        getLocator(sourceLocator).dragTo(getLocator(targetLocator));
    }

    // ============================================================
    // UTILITY METHODS
    // ============================================================

    @Override
    public String getAdapterType() {
        return "Microsoft Playwright";
    }

    @Override
    public boolean isActive() {
        return page != null && !page.isClosed();
    }

    @Override
    public Object getNativeDriver() {
        return page;
    }
}

