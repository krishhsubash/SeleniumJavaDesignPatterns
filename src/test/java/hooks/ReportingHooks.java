package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import utils.PlaywrightManager;
import utils.ScenarioContext;

/**
 * Cucumber hooks for basic test reporting
 */
public class ReportingHooks {
    
    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("🎬 Starting scenario: " + scenario.getName());
        System.out.println("📋 Tags: " + scenario.getSourceTagNames());
        
        // Set scenario in context for other step definitions to use
        ScenarioContext.setScenario(scenario);
    }
    
    @After
    public void afterScenario(Scenario scenario) {
        String status = scenario.getStatus().toString();
        System.out.println("🏁 Scenario completed: " + scenario.getName() + " - Status: " + status);
        
        // Take screenshot if Playwright is available (for both failed and passed scenarios)
        if (PlaywrightManager.isInitialized()) {
            try {
                String screenshotType = scenario.isFailed() ? "❌ Failed Scenario" : "✅ Completed Scenario";
                String screenshotName = screenshotType + ": " + scenario.getName();
                
                // Use enhanced screenshot utility for better reporting
                utils.ScreenshotUtils.attachScreenshotToScenario(scenario, PlaywrightManager.getPage(), screenshotName);
                
            } catch (Exception e) {
                System.err.println("❌ Failed to take screenshot: " + e.getMessage());
            }
        }
        
        // Clear scenario context
        ScenarioContext.clearScenario();
    }
}