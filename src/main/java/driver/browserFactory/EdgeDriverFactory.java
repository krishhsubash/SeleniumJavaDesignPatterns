package driver.browserFactory;


import driver.builder.EdgeOptionsBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * Factory implementation for creating Edge browser instances
 * Uses Builder Pattern for flexible options configuration
 */
public class EdgeDriverFactory implements BrowserFactory {

    @Override
    public WebDriver createDriver(boolean headless) {

        // Use Builder Pattern for flexible options configuration
        EdgeOptions options = EdgeOptionsBuilder.builder()
                .headless(headless)
                .withStabilitySettings()
                .windowSize(1920, 1080)
                .build();

        return new EdgeDriver(options);
    }

    @Override
    public String getBrowserName() {
        return "Edge";
    }
}
