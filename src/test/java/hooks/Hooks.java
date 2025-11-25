package hooks;


import driver.DriverManager;
import adapter.AdapterFactory;
import adapter.WebAutomationAdapter;
import adapter.AppiumAdapter;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Cucumber Hooks for setup and teardown
 * Manages WebDriver lifecycle using Singleton pattern
 * Supports web (Selenium, Playwright) and mobile (Appium) testing
 */
public class Hooks {

    private static WebAutomationAdapter adapter;

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        System.out.println("========================================");
        System.out.println("Starting Scenario: " + scenario.getName());
        System.out.println("Tags: " + scenario.getSourceTagNames());
        System.out.println("========================================");

        // Driver will be initialized on first getDriver() call in step definitions
        // This is lazy initialization as part of Singleton pattern
    }

    @Before(value = "@mobile or @appium", order = 1)
    public void setUpMobile(Scenario scenario) {
        System.out.println("📱 Initializing mobile testing environment...");

        // Determine platform from tags or config
        String platform = "android"; // default
        if (scenario.getSourceTagNames().contains("@ios")) {
            platform = "ios";
        } else if (scenario.getSourceTagNames().contains("@android")) {
            platform = "android";
        }

        // Create Appium adapter
        adapter = AdapterFactory.createAdapter("appium", platform, false);
        System.out.println("✓ Mobile adapter created for platform: " + platform);
    }

    @Before(value = "@api or @rest or @grpc", order = 1)
    public void setUpAPI(Scenario scenario) {
        System.out.println("🌐 Initializing API testing environment...");
        // API-specific setup if needed
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        // Take screenshot if scenario fails
        if (scenario.isFailed()) {
            captureScreenshot(scenario);
        }

        System.out.println("========================================");
        System.out.println("Scenario Status: " + scenario.getStatus());
        System.out.println("========================================\n");
    }

    @After(value = "@mobile or @appium", order = 0)
    public void tearDownMobile(Scenario scenario) {
        // Close mobile app and driver
        if (adapter instanceof AppiumAdapter) {
            try {
                ((AppiumAdapter) adapter).quitBrowser();
                System.out.println("✓ Mobile driver closed");
            } catch (Exception e) {
                System.err.println("Error closing mobile driver: " + e.getMessage());
            }
        }
        adapter = null;
    }

    @After(value = "not @mobile and not @appium and not @api", order = 0)
    public void tearDownWeb(Scenario scenario) {
        // Quit web driver after each scenario
        if (DriverManager.isDriverInitialized()) {
            DriverManager.quitDriver();
            System.out.println("Browser closed");
        }
    }

    /**
     * Capture screenshot for failed scenarios
     */
    private void captureScreenshot(Scenario scenario) {
        try {
            // For mobile tests
            if (adapter instanceof AppiumAdapter) {
                ((AppiumAdapter) adapter).takeScreenshot(scenario.getName());
                System.out.println("📸 Mobile screenshot captured");

                // Also attach to Cucumber report
                byte[] screenshot = ((TakesScreenshot) ((AppiumAdapter) adapter).getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failed Screenshot: " + scenario.getName());
            }
            // For web tests
            else if (DriverManager.isDriverInitialized()) {
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failed Screenshot: " + scenario.getName());
                System.out.println("📸 Web screenshot captured");
            }
        } catch (Exception e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
        }
    }

    /**
     * Get the current adapter (for step definitions)
     */
    public static WebAutomationAdapter getAdapter() {
        return adapter;
    }
}
