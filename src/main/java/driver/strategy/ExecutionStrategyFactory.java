package driver.strategy;

import driver.ConfigReader;

/**
 * Execution Strategy Factory
 *
 * Factory class to create the appropriate execution strategy
 * based on the configured test environment
 *
 * Supported Environments:
 * - local: Run tests on local machine (default)
 * - cloud: Run tests on cloud providers (BrowserStack, Sauce Labs, etc.)
 * - grid: Run tests on Selenium Grid
 * - docker: Run tests in Docker containers
 * - ci: Run tests in CI/CD pipelines
 *
 * Configuration:
 * Set 'execution.environment' in config.properties
 */
public class ExecutionStrategyFactory {

    private static final String EXECUTION_ENV_KEY = "execution.environment";
    private static final String DEFAULT_ENVIRONMENT = "local";

    /**
     * Get the appropriate execution strategy based on configuration
     *
     * @return ExecutionStrategy instance
     */
    public static ExecutionStrategy getStrategy() {
        String environment = ConfigReader.getProperty(EXECUTION_ENV_KEY, DEFAULT_ENVIRONMENT);
        return getStrategy(environment);
    }

    /**
     * Get execution strategy for a specific environment
     *
     * @param environment Environment name (local, cloud, grid, docker, ci)
     * @return ExecutionStrategy instance
     */
    public static ExecutionStrategy getStrategy(String environment) {
        if (environment == null || environment.trim().isEmpty()) {
            environment = DEFAULT_ENVIRONMENT;
        }

        switch (environment.toLowerCase().trim()) {
            case "local":
                return new LocalExecutionStrategy();

            case "cloud":
                return new CloudExecutionStrategy();

            case "grid":
                return new GridExecutionStrategy();

            case "docker":
                return new DockerExecutionStrategy();

            case "ci":
            case "cicd":
                return new CIExecutionStrategy();

            default:
                System.out.println("⚠️  Unknown environment: " + environment +
                        ". Using default: " + DEFAULT_ENVIRONMENT);
                return new LocalExecutionStrategy();
        }
    }

    /**
     * Get current environment name
     *
     * @return Current execution environment
     */
    public static String getCurrentEnvironment() {
        return ConfigReader.getProperty(EXECUTION_ENV_KEY, DEFAULT_ENVIRONMENT);
    }

    /**
     * Print all available strategies
     */
    public static void printAvailableStrategies() {
        System.out.println("========================================");
        System.out.println("📋 AVAILABLE EXECUTION STRATEGIES");
        System.out.println("========================================");
        System.out.println("1. local  - Run on local machine");
        System.out.println("2. cloud  - Run on cloud providers");
        System.out.println("3. grid   - Run on Selenium Grid");
        System.out.println("4. docker - Run in Docker containers");
        System.out.println("5. ci     - Run in CI/CD pipelines");
        System.out.println("========================================");
        System.out.println("Current: " + getCurrentEnvironment());
        System.out.println("========================================");
    }
}

