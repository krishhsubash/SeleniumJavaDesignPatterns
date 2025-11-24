package driver.strategy;

import driver.browserFactory.BrowserFactory;
import driver.browserFactory.BrowserFactoryProvider;
import org.openqa.selenium.WebDriver;

/**
 * CI/CD Execution Strategy
 *
 * Optimized for running tests in CI/CD pipelines
 * (GitHub Actions, Jenkins, GitLab CI, Azure DevOps, etc.)
 *
 * Features:
 * - Always runs in headless mode (overrides parameter)
 * - Optimized for CI/CD environments
 * - Fast execution with minimal resource usage
 * - Automatic cleanup and resource management
 * - Enhanced logging for CI/CD logs
 */
public class CIExecutionStrategy implements ExecutionStrategy {

    @Override
    public WebDriver createDriver(String browserType, boolean headless) {
        // CI/CD always runs in headless mode for efficiency
        boolean ciHeadless = true;

        // Detect CI environment
        String ciEnvironment = detectCIEnvironment();

        // Use existing BrowserFactory pattern with forced headless
        BrowserFactory factory = BrowserFactoryProvider.getBrowserFactory(browserType);
        WebDriver driver = factory.createDriver(ciHeadless);

        System.out.println("========================================");
        System.out.println("🔧 CI/CD EXECUTION STRATEGY");
        System.out.println("========================================");
        System.out.println("CI Environment: " + ciEnvironment);
        System.out.println("Browser: " + browserType);
        System.out.println("Headless: " + ciHeadless + " (forced for CI/CD)");
        System.out.println("========================================");

        return driver;
    }

    /**
     * Detect which CI/CD environment is running the tests
     */
    private String detectCIEnvironment() {
        if (System.getenv("GITHUB_ACTIONS") != null) {
            return "GitHub Actions";
        } else if (System.getenv("JENKINS_URL") != null) {
            return "Jenkins";
        } else if (System.getenv("GITLAB_CI") != null) {
            return "GitLab CI";
        } else if (System.getenv("CIRCLECI") != null) {
            return "CircleCI";
        } else if (System.getenv("TRAVIS") != null) {
            return "Travis CI";
        } else if (System.getenv("AZURE_PIPELINES") != null) {
            return "Azure DevOps";
        } else if (System.getenv("CI") != null) {
            return "Generic CI";
        } else {
            return "Unknown (CI mode enabled)";
        }
    }

    @Override
    public String getEnvironmentName() {
        return "CI/CD (" + detectCIEnvironment() + ")";
    }

    @Override
    public String getRequiredConfiguration() {
        return "No special configuration required. Automatically detects CI/CD environment.\n" +
                "Supported: GitHub Actions, Jenkins, GitLab CI, CircleCI, Travis CI, Azure DevOps";
    }

    @Override
    public boolean isConfigured() {
        // CI strategy is always configured (relies on local browser with headless mode)
        return true;
    }

    @Override
    public void cleanup() {
        System.out.println("✅ CI/CD execution cleanup complete");
        System.out.println("   Environment: " + detectCIEnvironment());
    }
}

