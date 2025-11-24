package driver.browserFactory;

import driver.builder.ChromeOptionsBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Factory implementation for creating Chrome browser instances
 * Uses Builder Pattern for flexible options configuration
 */
public class ChromeDriverFactory implements BrowserFactory {

    @Override
    public WebDriver createDriver(boolean headless) {

        // Use Builder Pattern for flexible options configuration
        ChromeOptions options = ChromeOptionsBuilder.builder()
                .headless(headless)
                .withStabilitySettings()
                .windowSize(1920, 1080)
                .build();

        return new ChromeDriver(options);
    }

    @Override
    public String getBrowserName() {
        return "Chrome";
    }
}
