package driver.strategy;

import org.openqa.selenium.WebDriver;

/**
 * Strategy Pattern Interface for different test execution environments
 *
 * This pattern allows switching between different execution strategies
 * (local, cloud, grid, docker, CI/CD) without changing test code
 *
 * Implementations:
 * - LocalExecutionStrategy: Run tests on local machine
 * - CloudExecutionStrategy: Run tests on cloud providers (BrowserStack, Sauce Labs, etc.)
 * - GridExecutionStrategy: Run tests on Selenium Grid
 * - DockerExecutionStrategy: Run tests in Docker containers
 * - CIExecutionStrategy: Run tests in CI/CD pipelines
 */
public interface ExecutionStrategy {

    /**
     * Create WebDriver instance based on the execution environment strategy
     *
     * @param browserType Browser to launch (chrome, firefox, edge)
     * @param headless Whether to run in headless mode
     * @return Configured WebDriver instance
     */
    WebDriver createDriver(String browserType, boolean headless);

    /**
     * Get the name of the execution environment
     *
     * @return Environment name (e.g., "Local", "Cloud", "Grid")
     */
    String getEnvironmentName();

    /**
     * Get configuration requirements for this strategy
     *
     * @return String describing required configuration
     */
    String getRequiredConfiguration();

    /**
     * Check if the strategy is properly configured
     *
     * @return true if all required configurations are present
     */
    boolean isConfigured();

    /**
     * Cleanup any resources specific to this execution strategy
     */
    default void cleanup() {
        // Default: no special cleanup needed
    }
}
