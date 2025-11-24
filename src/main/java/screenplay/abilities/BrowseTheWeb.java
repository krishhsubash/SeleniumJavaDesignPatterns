package screenplay.abilities;

import adapter.WebAutomationAdapter;

/**
 * Ability to browse the web using a WebAutomationAdapter.
 * 
 * This ability wraps the Adapter pattern, allowing actors to interact with
 * web browsers through either Selenium or Playwright.
 * 
 * Example usage:
 * <pre>
 * WebAutomationAdapter adapter = AdapterFactory.createAdapter("selenium", "chrome", true);
 * Actor john = Actor.named("John");
 * john.can(BrowseTheWeb.with(adapter));
 * 
 * john.attemptsTo(Open.url("https://www.google.com"));
 * john.attemptsTo(Click.on("css=#searchButton"));
 * String title = john.asksFor(TheTitle.ofThePage());
 * </pre>
 * 
 * Integration with other patterns:
 * - Uses ADAPTER pattern: Works with both Selenium and Playwright
 * - Enables SCREENPLAY pattern: Actors use this ability to browse
 * - Works with SINGLETON pattern: Can use shared driver instances
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class BrowseTheWeb {
    
    private final WebAutomationAdapter adapter;
    
    /**
     * Creates a new BrowseTheWeb ability with the given adapter.
     * 
     * @param adapter The WebAutomationAdapter to use
     */
    private BrowseTheWeb(WebAutomationAdapter adapter) {
        this.adapter = adapter;
    }
    
    /**
     * Creates a new BrowseTheWeb ability with the given adapter.
     * 
     * @param adapter The WebAutomationAdapter to use
     * @return A new BrowseTheWeb ability
     */
    public static BrowseTheWeb with(WebAutomationAdapter adapter) {
        return new BrowseTheWeb(adapter);
    }
    
    /**
     * Gets the underlying WebAutomationAdapter.
     * 
     * @return The WebAutomationAdapter
     */
    public WebAutomationAdapter getAdapter() {
        return adapter;
    }
    
    /**
     * Navigates to a URL.
     * 
     * @param url The URL to navigate to
     */
    public void navigateTo(String url) {
        adapter.navigateTo(url);
    }
    
    /**
     * Clicks an element.
     * 
     * @param locator The element locator
     */
    public void click(String locator) {
        adapter.click(locator);
    }
    
    /**
     * Types text into an element.
     * 
     * @param locator The element locator
     * @param text The text to type
     */
    public void type(String locator, String text) {
        adapter.type(locator, text);
    }
    
    /**
     * Gets text from an element.
     * 
     * @param locator The element locator
     * @return The element text
     */
    public String getText(String locator) {
        return adapter.getText(locator);
    }
    
    /**
     * Gets the current URL.
     * 
     * @return The current URL
     */
    public String getCurrentUrl() {
        return adapter.getCurrentUrl();
    }
    
    /**
     * Gets the page title.
     * 
     * @return The page title
     */
    public String getPageTitle() {
        return adapter.getPageTitle();
    }
    
    /**
     * Checks if an element is displayed.
     * 
     * @param locator The element locator
     * @return true if the element is displayed, false otherwise
     */
    public boolean isDisplayed(String locator) {
        return adapter.isDisplayed(locator);
    }
    
    /**
     * Waits for an element to be visible.
     * 
     * @param locator The element locator
     * @param timeoutSeconds The timeout in seconds
     */
    public void waitForVisible(String locator, int timeoutSeconds) {
        adapter.waitForVisible(locator, timeoutSeconds);
    }
    
    /**
     * Takes a screenshot.
     * 
     * @param filePath The file path to save the screenshot
     * @return true if screenshot was taken successfully
     */
    public boolean takeScreenshot(String filePath) {
        return adapter.takeScreenshot(filePath);
    }
    
    /**
     * Closes the browser.
     */
    public void closeBrowser() {
        adapter.closeBrowser();
    }
    
    /**
     * Quits the browser.
     */
    public void quitBrowser() {
        adapter.quitBrowser();
    }
}
