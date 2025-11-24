package driver.browserFactory;

import driver.builder.FirefoxOptionsBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Factory implementation for creating Firefox browser instances
 * Uses Builder Pattern for flexible options configuration
 */
public class FirefoxDriverFactory implements BrowserFactory {

    @Override
    public WebDriver createDriver(boolean headless) {

        // Use Builder Pattern for flexible options configuration
        FirefoxOptions options = FirefoxOptionsBuilder.builder()
                .headless(headless)
                .withStabilitySettings()
                .build();

        return new FirefoxDriver(options);
    }

    @Override
    public String getBrowserName() {
        return "Firefox";
    }
}
