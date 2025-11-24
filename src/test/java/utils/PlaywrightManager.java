package utils;

import com.microsoft.playwright.*;

/**
 * PlaywrightManager handles the lifecycle of Playwright instances for testing.
 * This provides a singleton pattern to ensure Playwright is properly initialized
 * and cleaned up across test runs.
 */
public class PlaywrightManager {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;
    
    /**
     * Initialize Playwright browser instance
     */
    public static void initializePlaywright() {
        if (playwright == null) {
            System.out.println("🎭 Initializing Playwright...");
            
            // Create Playwright instance
            playwright = Playwright.create();
            
            // Launch browser (headless by default, can be configured)
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false) // Set to false for debugging
                .setSlowMo(100)    // Slow down by 100ms for better visibility
            );
            
            // Create browser context
            context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1280, 720)
                .setUserAgent("PlaywrightTestRunner/1.0")
            );
            
            // Create a new page
            page = context.newPage();
            
            System.out.println("✅ Playwright initialized successfully!");
            System.out.println("🌐 Browser: " + browser.browserType().name());
            System.out.println("📱 Viewport: 1280x720");
        }
    }
    
    /**
     * Get the current Playwright instance
     */
    public static Playwright getPlaywright() {
        if (playwright == null) {
            initializePlaywright();
        }
        return playwright;
    }
    
    /**
     * Get the current Browser instance
     */
    public static Browser getBrowser() {
        if (browser == null) {
            initializePlaywright();
        }
        return browser;
    }
    
    /**
     * Get the current BrowserContext instance
     */
    public static BrowserContext getBrowserContext() {
        if (context == null) {
            initializePlaywright();
        }
        return context;
    }
    
    /**
     * Get the current Page instance
     */
    public static Page getPage() {
        if (page == null) {
            initializePlaywright();
        }
        return page;
    }
    
    /**
     * Create a new page instance
     */
    public static Page createNewPage() {
        if (context == null) {
            initializePlaywright();
        }
        return context.newPage();
    }
    
    /**
     * Navigate to a URL using the default page
     */
    public static void navigateTo(String url) {
        if (page == null) {
            initializePlaywright();
        }
        System.out.println("🔗 Navigating to: " + url);
        page.navigate(url);
    }
    
    /**
     * Take a screenshot of the current page
     */
    public static void takeScreenshot(String filename) {
        if (page == null) {
            System.err.println("❌ Cannot take screenshot - no page instance");
            return;
        }
        
        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get(filename)));
            System.out.println("📸 Screenshot saved: " + filename);
        } catch (Exception e) {
            System.err.println("❌ Failed to take screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Clean up Playwright resources
     */
    public static void cleanup() {
        System.out.println("🧹 Cleaning up Playwright resources...");
        
        if (page != null) {
            page.close();
            page = null;
        }
        
        if (context != null) {
            context.close();
            context = null;
        }
        
        if (browser != null) {
            browser.close();
            browser = null;
        }
        
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
        
        System.out.println("✅ Playwright cleanup completed!");
    }
    
    /**
     * Check if Playwright is initialized
     */
    public static boolean isInitialized() {
        return playwright != null && browser != null && context != null && page != null;
    }
    
    /**
     * Get current page title
     */
    public static String getPageTitle() {
        if (page == null) {
            return null;
        }
        return page.title();
    }
    
    /**
     * Get current page URL
     */
    public static String getCurrentUrl() {
        if (page == null) {
            return null;
        }
        return page.url();
    }
}