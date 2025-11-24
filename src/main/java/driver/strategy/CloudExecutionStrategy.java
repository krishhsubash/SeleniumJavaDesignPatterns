package driver.strategy;

import driver.ConfigReader;
import driver.builder.ChromeOptionsBuilder;
import driver.builder.FirefoxOptionsBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Cloud Execution Strategy
 *
 * Runs tests on cloud-based browser testing platforms
 * Supports: BrowserStack, Sauce Labs, LambdaTest, etc.
 *
 * Required Configuration (config.properties):
 * - cloud.provider: browserstack|saucelabs|lambdatest
 * - cloud.username: Your cloud provider username
 * - cloud.access.key: Your cloud provider access key
 * - cloud.url: Cloud provider hub URL
 *
 * Features:
 * - Cross-browser testing on real devices
 * - Parallel execution on cloud infrastructure
 * - Video recording and screenshots
 * - Network logs and debugging tools
 */
public class CloudExecutionStrategy implements ExecutionStrategy {

    private static final String CLOUD_PROVIDER_KEY = "cloud.provider";
    private static final String CLOUD_USERNAME_KEY = "cloud.username";
    private static final String CLOUD_ACCESS_KEY = "cloud.access.key";
    private static final String CLOUD_URL_KEY = "cloud.url";

    @Override
    public WebDriver createDriver(String browserType, boolean headless) {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "Cloud execution strategy is not properly configured. " +
                            "Please check config.properties: " + getRequiredConfiguration()
            );
        }

        String cloudProvider = ConfigReader.getProperty(CLOUD_PROVIDER_KEY);
        String username = ConfigReader.getProperty(CLOUD_USERNAME_KEY);
        String accessKey = ConfigReader.getProperty(CLOUD_ACCESS_KEY);
        String cloudUrl = ConfigReader.getProperty(CLOUD_URL_KEY);

        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            // Set browser-specific options
            switch (browserType.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = ChromeOptionsBuilder.builder()
                            .headless(headless)
                            .withStabilitySettings()
                            .build();
                    capabilities.merge(chromeOptions);
                    capabilities.setCapability("browserName", "chrome");
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = FirefoxOptionsBuilder.builder()
                            .headless(headless)
                            .withStabilitySettings()
                            .build();
                    capabilities.merge(firefoxOptions);
                    capabilities.setCapability("browserName", "firefox");
                    break;

                case "edge":
                    capabilities.setCapability("browserName", "edge");
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browserType);
            }

            // Add cloud-specific capabilities based on provider
            addCloudCapabilities(capabilities, cloudProvider);

            System.out.println("========================================");
            System.out.println("☁️  CLOUD EXECUTION STRATEGY");
            System.out.println("========================================");
            System.out.println("Provider: " + cloudProvider);
            System.out.println("Browser: " + browserType);
            System.out.println("Headless: " + headless);
            System.out.println("Cloud URL: " + cloudUrl);
            System.out.println("========================================");

            URL url = new URL(cloudUrl);
            return new RemoteWebDriver(url, capabilities);

        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid cloud URL: " + cloudUrl, e);
        }
    }

    /**
     * Add cloud provider-specific capabilities
     */
    private void addCloudCapabilities(DesiredCapabilities capabilities, String provider) {
        String username = ConfigReader.getProperty(CLOUD_USERNAME_KEY);
        String accessKey = ConfigReader.getProperty(CLOUD_ACCESS_KEY);

        switch (provider.toLowerCase()) {
            case "browserstack":
                capabilities.setCapability("browserstack.user", username);
                capabilities.setCapability("browserstack.key", accessKey);
                capabilities.setCapability("browserstack.debug", "true");
                capabilities.setCapability("browserstack.console", "verbose");
                break;

            case "saucelabs":
                capabilities.setCapability("username", username);
                capabilities.setCapability("accessKey", accessKey);
                capabilities.setCapability("sauce:options", getSauceOptions());
                break;

            case "lambdatest":
                capabilities.setCapability("user", username);
                capabilities.setCapability("accessKey", accessKey);
                capabilities.setCapability("build", "Selenium BDD Framework");
                break;

            default:
                // Generic cloud setup
                capabilities.setCapability("username", username);
                capabilities.setCapability("accessKey", accessKey);
        }
    }

    /**
     * Get Sauce Labs specific options
     */
    private java.util.Map<String, Object> getSauceOptions() {
        java.util.Map<String, Object> sauceOptions = new java.util.HashMap<>();
        sauceOptions.put("build", "Selenium BDD Framework");
        sauceOptions.put("name", "Cloud Execution Test");
        return sauceOptions;
    }

    @Override
    public String getEnvironmentName() {
        String provider = ConfigReader.getProperty(CLOUD_PROVIDER_KEY, "Unknown");
        return "Cloud (" + provider + ")";
    }

    @Override
    public String getRequiredConfiguration() {
        return String.format(
                "Required properties:\n" +
                        "  %s: browserstack|saucelabs|lambdatest\n" +
                        "  %s: Your username\n" +
                        "  %s: Your access key\n" +
                        "  %s: Cloud hub URL",
                CLOUD_PROVIDER_KEY, CLOUD_USERNAME_KEY, CLOUD_ACCESS_KEY, CLOUD_URL_KEY
        );
    }

    @Override
    public boolean isConfigured() {
        return ConfigReader.getProperty(CLOUD_PROVIDER_KEY) != null &&
                ConfigReader.getProperty(CLOUD_USERNAME_KEY) != null &&
                ConfigReader.getProperty(CLOUD_ACCESS_KEY) != null &&
                ConfigReader.getProperty(CLOUD_URL_KEY) != null;
    }

    @Override
    public void cleanup() {
        System.out.println("✅ Cloud execution cleanup complete");
        System.out.println("   Check cloud dashboard for test results and recordings");
    }
}

