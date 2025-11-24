package driver.strategy;
import driver.browserFactory.BrowserFactory;
import driver.browserFactory.BrowserFactoryProvider;
import org.openqa.selenium.WebDriver;

/**
 * Local Execution Strategy
 *
 * Runs tests on the local machine using locally installed browsers
 * This is the default strategy for development and debugging
 *
 * Features:
 * - Uses local browser drivers (via WebDriverManager)
 * - Fast execution (no network overhead)
 * - Easy debugging with browser visibility
 * - Supports headless mode for CI
 */
public class LocalExecutionStrategy implements ExecutionStrategy {

    @Override
    public WebDriver createDriver(String browserType, boolean headless) {
        // Use existing BrowserFactory pattern for local execution
        BrowserFactory factory = BrowserFactoryProvider.getBrowserFactory(browserType);
        WebDriver driver = factory.createDriver(headless);

        System.out.println("========================================");
        System.out.println("🖥️  LOCAL EXECUTION STRATEGY");
        System.out.println("========================================");
        System.out.println("Browser: " + browserType);
        System.out.println("Headless: " + headless);
        System.out.println("Environment: Local Machine");
        System.out.println("========================================");

        return driver;
    }

    @Override
    public String getEnvironmentName() {
        return "Local";
    }

    @Override
    public String getRequiredConfiguration() {
        return "No special configuration required. Uses local browser installations.";
    }

    @Override
    public boolean isConfigured() {
        // Local strategy is always configured (relies on WebDriverManager)
        return true;
    }

    @Override
    public void cleanup() {
        // No special cleanup needed for local execution
        System.out.println("✅ Local execution cleanup complete");
    }
}

