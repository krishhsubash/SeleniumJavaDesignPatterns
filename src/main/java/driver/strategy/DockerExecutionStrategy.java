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
 * Docker Execution Strategy
 *
 * Runs tests in Docker containers using Selenium Docker images
 * Ideal for CI/CD pipelines and isolated test environments
 *
 * Required Configuration (config.properties):
 * - docker.selenium.url: Docker Selenium URL (e.g., http://localhost:4444/wd/hub)
 *
 * Docker Setup:
 * docker run -d -p 4444:4444 -p 7900:7900 --shm-size="2g" \
 *   selenium/standalone-chrome:latest
 *
 * Features:
 * - Isolated test environment
 * - Consistent browser versions
 * - Easy CI/CD integration
 * - VNC support for debugging (port 7900)
 */
public class DockerExecutionStrategy implements ExecutionStrategy {

    private static final String DOCKER_URL_KEY = "docker.selenium.url";
    private static final String DEFAULT_DOCKER_URL = "http://localhost:4444/wd/hub";

    @Override
    public WebDriver createDriver(String browserType, boolean headless) {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "Docker execution strategy is not properly configured. " +
                            "Please check config.properties: " + getRequiredConfiguration()
            );
        }

        String dockerUrl = ConfigReader.getProperty(DOCKER_URL_KEY, DEFAULT_DOCKER_URL);

        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            // Set browser-specific options
            switch (browserType.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = ChromeOptionsBuilder.builder()
                            .headless(headless)
                            .withStabilitySettings()
                            .addArgument("--disable-dev-shm-usage") // Docker-specific
                            .addArgument("--no-sandbox") // Docker-specific
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

            // Docker-specific capabilities
            capabilities.setCapability("se:recordVideo", true);
            capabilities.setCapability("se:screenResolution", "1920x1080");

            System.out.println("========================================");
            System.out.println("🐳 DOCKER EXECUTION STRATEGY");
            System.out.println("========================================");
            System.out.println("Docker URL: " + dockerUrl);
            System.out.println("Browser: " + browserType);
            System.out.println("Headless: " + headless);
            System.out.println("VNC Debug: http://localhost:7900");
            System.out.println("========================================");

            URL url = new URL(dockerUrl);
            return new RemoteWebDriver(url, capabilities);

        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Docker URL: " + dockerUrl, e);
        }
    }

    @Override
    public String getEnvironmentName() {
        return "Docker";
    }

    @Override
    public String getRequiredConfiguration() {
        return String.format(
                "Required properties:\n" +
                        "  %s: Docker Selenium URL (default: %s)\n\n" +
                        "Docker Setup:\n" +
                        "  docker run -d -p 4444:4444 -p 7900:7900 --shm-size=\"2g\" \\\n" +
                        "    selenium/standalone-chrome:latest",
                DOCKER_URL_KEY, DEFAULT_DOCKER_URL
        );
    }

    @Override
    public boolean isConfigured() {
        // Docker strategy is configured if URL is provided or defaults to localhost
        String dockerUrl = ConfigReader.getProperty(DOCKER_URL_KEY, DEFAULT_DOCKER_URL);
        return dockerUrl != null && !dockerUrl.trim().isEmpty();
    }

    @Override
    public void cleanup() {
        System.out.println("✅ Docker execution cleanup complete");
        System.out.println("   Video recordings available in Docker container");
    }
}

