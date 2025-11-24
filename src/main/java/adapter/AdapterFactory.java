package adapter;

/**
 * Factory for creating WebAutomationAdapter instances
 *
 * This factory creates appropriate adapter based on automation framework type.
 * Uses Factory Design Pattern to instantiate adapters.
 *
 * <h3>Design Patterns Combined:</h3>
 * - Factory Pattern: Creates adapter instances
 * - Adapter Pattern: Provides unified interface for different frameworks
 *
 * <h4>Usage:</h4>
 * <pre>
 * // Create Selenium adapter
 * WebAutomationAdapter adapter = AdapterFactory.createAdapter("selenium", "chrome", false);
 * adapter.navigateTo("https://google.com");
 *
 * // Create Playwright adapter
 * WebAutomationAdapter adapter = AdapterFactory.createAdapter("playwright", "chromium", true);
 * adapter.navigateTo("https://google.com");
 *
 * // Same API for both!
 * adapter.click("css=#searchButton");
 * adapter.type("css=#searchInput", "test automation");
 * </pre>
 *
 * @author Selenium Test Automation Framework
 * @version 1.0
 */
public class AdapterFactory {

    /**
     * Create adapter for specified framework type
     *
     * @param frameworkType Framework type (selenium, playwright)
     * @param browserType Browser type (chrome, firefox, edge, webkit)
     * @param headless Whether to run in headless mode
     * @return WebAutomationAdapter instance
     * @throws IllegalArgumentException if framework type is not supported
     */
    public static WebAutomationAdapter createAdapter(String frameworkType, String browserType, boolean headless) {
        WebAutomationAdapter adapter;

        switch (frameworkType.toLowerCase()) {
            case "playwright":
                System.out.println("✓ AdapterFactory: Creating Playwright adapter");
                adapter = new PlaywrightAdapter();
                break;

            case "selenium":
            case "webdriver":
            default:
                System.out.println("✓ AdapterFactory: Creating Selenium adapter");
                adapter = new SeleniumAdapter();
                break;
        }

        // Initialize browser
        adapter.initializeBrowser(browserType, headless);

        return adapter;
    }

    /**
     * Create adapter with default settings
     * Framework: Selenium, Browser: Chrome, Headless: false
     *
     * @return SeleniumAdapter with Chrome browser
     */
    public static WebAutomationAdapter createAdapter() {
        return createAdapter("selenium", "chrome", false);
    }

    /**
     * Create adapter with specified framework, default browser and mode
     * Browser: Chrome, Headless: false
     *
     * @param frameworkType Framework type (selenium, playwright)
     * @return WebAutomationAdapter instance
     */
    public static WebAutomationAdapter createAdapter(String frameworkType) {
        return createAdapter(frameworkType, "chrome", false);
    }

    /**
     * Create adapter with specified framework and browser, headless mode off
     *
     * @param frameworkType Framework type (selenium, playwright)
     * @param browserType Browser type
     * @return WebAutomationAdapter instance
     */
    public static WebAutomationAdapter createAdapter(String frameworkType, String browserType) {
        return createAdapter(frameworkType, browserType, false);
    }

    /**
     * Get supported framework types
     *
     * @return Array of supported framework names
     */
    public static String[] getSupportedFrameworks() {
        return new String[]{"selenium", "playwright"};
    }

    /**
     * Check if framework type is supported
     *
     * @param frameworkType Framework type to check
     * @return true if supported, false otherwise
     */
    public static boolean isSupported(String frameworkType) {
        for (String supported : getSupportedFrameworks()) {
            if (supported.equalsIgnoreCase(frameworkType)) {
                return true;
            }
        }
        return false;
    }
}

