package driver;

import driver.strategy.ExecutionStrategy;
import driver.strategy.ExecutionStrategyFactory;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

/**
 * Singleton Pattern implementation for WebDriver management
 * Uses Strategy Pattern for different execution environments
 * Uses Factory Design Pattern for browser instantiation
 * Ensures only one instance of WebDriver exists throughout the test execution
 *
 * Design Patterns:
 * - Singleton: ThreadLocal storage for parallel execution
 * - Strategy: Different execution environments (local, cloud, grid, docker, ci)
 * - Factory: Browser instance creation
 */
public class DriverManager {

    // ThreadLocal to support parallel execution
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private DriverManager() {
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }

    /**
     * Get the WebDriver instance (Singleton)
     * Creates a new instance if it doesn't exist
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            initializeDriver();
        }
        return driver.get();
    }

    /**
     * Initialize WebDriver based on execution strategy and browser type
     * Uses Strategy Pattern to select execution environment
     * Uses Factory Design Pattern to create browser instances
     */
    private static void initializeDriver() {
        String browser = ConfigReader.getProperty("browser", "chrome");
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless", "false"));

        // STRATEGY PATTERN: Get execution strategy based on environment
        ExecutionStrategy executionStrategy = ExecutionStrategyFactory.getStrategy();

        // Create WebDriver using the selected strategy
        WebDriver webDriver = executionStrategy.createDriver(browser, headless);

        // Configure WebDriver
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("timeout", "10")))
        );
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        driver.set(webDriver);

        System.out.println("✅ WebDriver initialized using " +
                executionStrategy.getEnvironmentName() + " strategy");
    }

    /**
     * Quit the WebDriver instance and remove it from ThreadLocal
     * Calls strategy-specific cleanup
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();

            // Call strategy cleanup
            ExecutionStrategy strategy = ExecutionStrategyFactory.getStrategy();
            strategy.cleanup();
        }
    }

    /**
     * Check if driver instance exists
     *
     * @return true if driver exists, false otherwise
     */
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }
}

