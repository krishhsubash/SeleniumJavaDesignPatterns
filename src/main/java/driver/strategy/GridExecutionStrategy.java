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
 * Selenium Grid Execution Strategy
 *
 * Runs tests on Selenium Grid (standalone or distributed setup)
 * Supports parallel execution across multiple nodes
 *
 * Required Configuration (config.properties):
 * - grid.hub.url: Selenium Grid hub URL (e.g., http://localhost:4444/wd/hub)
 *
 * Features:
 * - Distributed test execution
 * - Cross-browser and cross-platform testing
 * - Scalable test infrastructure
 * - Support for custom node configurations
 */
public class GridExecutionStrategy implements ExecutionStrategy {

    private static final String GRID_HUB_URL_KEY = "grid.hub.url";
    private static final String DEFAULT_GRID_URL = "http://localhost:4444/wd/hub";

    @Override
    public WebDriver createDriver(String browserType, boolean headless) {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "Grid execution strategy is not properly configured. " +
                            "Please check config.properties: " + getRequiredConfiguration()
            );
        }

        String gridUrl = ConfigReader.getProperty(GRID_HUB_URL_KEY, DEFAULT_GRID_URL);

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
                    capabilities.setCapability("browserName", "MicrosoftEdge");
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browserType);
            }

            System.out.println("========================================");
            System.out.println("🌐 GRID EXECUTION STRATEGY");
            System.out.println("========================================");
            System.out.println("Grid Hub: " + gridUrl);
            System.out.println("Browser: " + browserType);
            System.out.println("Headless: " + headless);
            System.out.println("========================================");

            URL url = new URL(gridUrl);
            return new RemoteWebDriver(url, capabilities);

        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Grid URL: " + gridUrl, e);
        }
    }

    @Override
    public String getEnvironmentName() {
        return "Selenium Grid";
    }

    @Override
    public String getRequiredConfiguration() {
        return String.format(
                "Required properties:\n" +
                        "  %s: Selenium Grid hub URL (default: %s)",
                GRID_HUB_URL_KEY, DEFAULT_GRID_URL
        );
    }

    @Override
    public boolean isConfigured() {
        // Grid strategy is configured if URL is provided or defaults to localhost
        String gridUrl = ConfigReader.getProperty(GRID_HUB_URL_KEY, DEFAULT_GRID_URL);
        return gridUrl != null && !gridUrl.trim().isEmpty();
    }

    @Override
    public void cleanup() {
        System.out.println("✅ Grid execution cleanup complete");
    }
}
