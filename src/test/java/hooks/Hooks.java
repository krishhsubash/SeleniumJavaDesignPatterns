package hooks;

import driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Cucumber Hooks for setup and teardown
 * Manages WebDriver lifecycle using Singleton pattern
 */
public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("========================================");
        System.out.println("Starting Scenario: " + scenario.getName());
        System.out.println("Tags: " + scenario.getSourceTagNames());
        System.out.println("========================================");

        // Driver will be initialized on first getDriver() call in step definitions
        // This is lazy initialization as part of Singleton pattern
    }

    @After
    public void tearDown(Scenario scenario) {
        // Take screenshot if scenario fails
        if (scenario.isFailed() && DriverManager.isDriverInitialized()) {
            try {
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failed Screenshot: " + scenario.getName());
                System.out.println("Screenshot captured for failed scenario");
            } catch (Exception e) {
                System.err.println("Error taking screenshot: " + e.getMessage());
            }
        }

        // Quit driver after each scenario
        if (DriverManager.isDriverInitialized()) {
            DriverManager.quitDriver();
            System.out.println("Browser closed");
        }

        System.out.println("========================================");
        System.out.println("Scenario Status: " + scenario.getStatus());
        System.out.println("========================================\n");
    }
}
