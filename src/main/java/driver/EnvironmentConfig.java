package driver;

import driver.strategy.ExecutionStrategy;
import driver.strategy.ExecutionStrategyFactory;

/**
 * Environment Configuration Helper
 *
 * Provides utilities for managing test execution environment configuration
 * and validating environment-specific settings
 */
public class EnvironmentConfig {

    // Execution environment
    public static final String EXECUTION_ENVIRONMENT = "execution.environment";

    // Cloud provider settings
    public static final String CLOUD_PROVIDER = "cloud.provider";
    public static final String CLOUD_USERNAME = "cloud.username";
    public static final String CLOUD_ACCESS_KEY = "cloud.access.key";
    public static final String CLOUD_URL = "cloud.url";

    // Grid settings
    public static final String GRID_HUB_URL = "grid.hub.url";

    // Docker settings
    public static final String DOCKER_SELENIUM_URL = "docker.selenium.url";

    /**
     * Get current execution environment
     */
    public static String getExecutionEnvironment() {
        return ConfigReader.getProperty(EXECUTION_ENVIRONMENT, "local");
    }

    /**
     * Check if running in cloud environment
     */
    public static boolean isCloudExecution() {
        return "cloud".equalsIgnoreCase(getExecutionEnvironment());
    }

    /**
     * Check if running in grid environment
     */
    public static boolean isGridExecution() {
        return "grid".equalsIgnoreCase(getExecutionEnvironment());
    }

    /**
     * Check if running in docker environment
     */
    public static boolean isDockerExecution() {
        return "docker".equalsIgnoreCase(getExecutionEnvironment());
    }

    /**
     * Check if running in CI/CD environment
     */
    public static boolean isCIExecution() {
        String env = getExecutionEnvironment();
        return "ci".equalsIgnoreCase(env) || "cicd".equalsIgnoreCase(env);
    }

    /**
     * Check if running locally
     */
    public static boolean isLocalExecution() {
        return "local".equalsIgnoreCase(getExecutionEnvironment());
    }

    /**
     * Validate environment configuration
     *
     * @return true if configuration is valid for current environment
     */
    public static boolean validateConfiguration() {
        ExecutionStrategy strategy = ExecutionStrategyFactory.getStrategy();
        return strategy.isConfigured();
    }

    /**
     * Print current environment configuration
     */
    public static void printConfiguration() {
        System.out.println("========================================");
        System.out.println("🔧 ENVIRONMENT CONFIGURATION");
        System.out.println("========================================");
        System.out.println("Environment: " + getExecutionEnvironment());

        ExecutionStrategy strategy = ExecutionStrategyFactory.getStrategy();
        System.out.println("Strategy: " + strategy.getClass().getSimpleName());
        System.out.println("Configured: " + strategy.isConfigured());
        System.out.println("\nRequired Configuration:");
        System.out.println(strategy.getRequiredConfiguration());
        System.out.println("========================================");
    }

    /**
     * Get cloud provider configuration
     */
    public static CloudConfig getCloudConfig() {
        return new CloudConfig(
                ConfigReader.getProperty(CLOUD_PROVIDER),
                ConfigReader.getProperty(CLOUD_USERNAME),
                ConfigReader.getProperty(CLOUD_ACCESS_KEY),
                ConfigReader.getProperty(CLOUD_URL)
        );
    }

    /**
     * Inner class for cloud configuration
     */
    public static class CloudConfig {
        private final String provider;
        private final String username;
        private final String accessKey;
        private final String url;

        public CloudConfig(String provider, String username, String accessKey, String url) {
            this.provider = provider;
            this.username = username;
            this.accessKey = accessKey;
            this.url = url;
        }

        public String getProvider() { return provider; }
        public String getUsername() { return username; }
        public String getAccessKey() { return accessKey; }
        public String getUrl() { return url; }

        public boolean isConfigured() {
            return provider != null && username != null &&
                    accessKey != null && url != null;
        }
    }
}

