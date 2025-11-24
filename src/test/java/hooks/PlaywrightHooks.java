package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import utils.PlaywrightManager;

/**
 * Cucumber hooks for managing Playwright lifecycle
 */
public class PlaywrightHooks {
    
    private static boolean playwrightInitialized = false;
    
    @Before
    public void initializePlaywright() {
        if (!playwrightInitialized) {
            System.out.println("🎭 Initializing Playwright for Cucumber scenario...");
            PlaywrightManager.initializePlaywright();
            playwrightInitialized = true;
            System.out.println("✅ Playwright ready for scenarios!");
        }
    }
    
    @After
    public void cleanupAfterScenario() {
        // Don't cleanup after each scenario - let it persist for the test run
        // We'll only cleanup at the very end through the test runner
        System.out.println("📝 Scenario completed, Playwright remains active for next scenarios");
    }
}