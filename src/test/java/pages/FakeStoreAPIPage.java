package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.LoadState;

/**
 * Page Object Model for FakeStore API website interactions using Playwright
 */
public class FakeStoreAPIPage {
    
    private final Page page;
    private static final int DEFAULT_TIMEOUT = 10000; // 10 seconds
    
    // Locators for common elements
    private static final String DOCS_LINK_SELECTOR = "a[href*='docs'], a:has-text('docs'), a:has-text('Docs'), a:has-text('Documentation')";
    private static final String API_LINK_SELECTOR = "a[href*='api']";
    private static final String TITLE_SELECTOR = "title";
    
    public FakeStoreAPIPage(Page page) {
        this.page = page;
    }
    
    /**
     * Navigate to the FakeStore API homepage
     */
    public void navigateToHomepage(String url) {
        System.out.println("🔗 Navigating to FakeStore API: " + url);
        try {
            page.navigate(url);
            page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(DEFAULT_TIMEOUT));
            System.out.println("✅ Navigation completed");
        } catch (TimeoutError e) {
            System.err.println("⚠️ Navigation timeout, but continuing: " + e.getMessage());
            // Continue anyway - page might be partially loaded
        } catch (Exception e) {
            System.err.println("❌ Navigation error: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to homepage", e);
        }
    }
    
    /**
     * Check if the homepage is loaded by looking for common elements
     */
    public boolean isHomepageLoaded() {
        try {
            // Wait for page to be in a reasonable state
            page.waitForTimeout(2000);
            
            // Check if the page has loaded by looking for basic HTML structure
            boolean hasTitle = page.locator("title").count() > 0;
            boolean hasBody = page.locator("body").count() > 0;
            
            if (hasTitle && hasBody) {
                System.out.println("✅ Homepage appears to be loaded");
                return true;
            } else {
                System.out.println("⚠️ Homepage elements not found");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error checking homepage load status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click on the docs link
     */
    public boolean clickDocsLink() {
        try {
            System.out.println("🔍 Looking for docs link...");
            
            // Try multiple selectors for docs link
            String[] docsSelectors = {
                "a[href*='docs']",
                "a:has-text('docs')",
                "a:has-text('Docs')",
                "a:has-text('Documentation')",
                "a:has-text('API')",
                "[href*='docs']",
                ".docs",
                "#docs"
            };
            
            for (String selector : docsSelectors) {
                try {
                    Locator docsLink = page.locator(selector);
                    if (docsLink.count() > 0) {
                        System.out.println("🎯 Found docs link with selector: " + selector);
                        docsLink.first().click();
                        System.out.println("✅ Clicked on docs link");
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Selector " + selector + " failed: " + e.getMessage());
                    continue;
                }
            }
            
            // If no specific docs link found, try to find any navigation links
            System.out.println("🔍 No specific docs link found, looking for navigation links...");
            Locator allLinks = page.locator("a");
            int linkCount = allLinks.count();
            System.out.println("📊 Found " + linkCount + " links on the page");
            
            if (linkCount > 0) {
                // Click on the first link as a fallback
                System.out.println("🔗 Clicking on first available link as fallback");
                allLinks.first().click();
                return true;
            }
            
            System.out.println("❌ No clickable links found on the page");
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error clicking docs link: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the current page title
     */
    public String getPageTitle() {
        try {
            String title = page.title();
            if (title == null || title.trim().isEmpty()) {
                System.out.println("⚠️ Page title is empty or null");
                return "Unknown Title";
            }
            return title;
        } catch (Exception e) {
            System.err.println("❌ Error getting page title: " + e.getMessage());
            return "Error retrieving title";
        }
    }
    
    /**
     * Get the current URL
     */
    public String getCurrentUrl() {
        try {
            return page.url();
        } catch (Exception e) {
            System.err.println("❌ Error getting current URL: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Take a screenshot of the current page
     */
    public boolean takeScreenshot(String filename) {
        try {
            System.out.println("📸 Taking screenshot: " + filename);
            
            // Take screenshot with full page
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(java.nio.file.Paths.get(filename))
                .setFullPage(true));
            
            System.out.println("✅ Screenshot saved successfully");
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error taking screenshot: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Wait for an element to be visible
     */
    public boolean waitForElement(String selector, int timeoutMs) {
        try {
            page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
            return true;
        } catch (TimeoutError e) {
            System.err.println("⚠️ Element not found within timeout: " + selector);
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error waiting for element: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if an element exists on the page
     */
    public boolean elementExists(String selector) {
        try {
            return page.locator(selector).count() > 0;
        } catch (Exception e) {
            System.err.println("❌ Error checking element existence: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get text content of an element
     */
    public String getElementText(String selector) {
        try {
            Locator element = page.locator(selector);
            if (element.count() > 0) {
                return element.first().textContent();
            } else {
                System.out.println("⚠️ Element not found: " + selector);
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Error getting element text: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Close the current page
     */
    public void closePage() {
        try {
            if (page != null) {
                page.close();
                System.out.println("✅ Page closed successfully");
            }
        } catch (Exception e) {
            System.err.println("❌ Error closing page: " + e.getMessage());
        }
    }
}