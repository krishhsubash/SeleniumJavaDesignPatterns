package driver.browserFactory;

import org.openqa.selenium.WebDriver;
/*Step 1: Create Interface
Step 2: Create Specific Factories
Step 3: Create Factory Manager
Step 4: Use in Tests
 */

/**
 * Factory interface for creating browser instances
 * Implements Factory Design Pattern for WebDriver creation
 */
public interface BrowserFactory {

    /**
     * Create and configure a WebDriver instance
     *
     * @param headless whether to run browser in headless mode
     * @return configured WebDriver instance
     */
    WebDriver createDriver(boolean headless);

    /**
     * Get the browser name
     *
     * @return browser name
     */
    String getBrowserName();
}
