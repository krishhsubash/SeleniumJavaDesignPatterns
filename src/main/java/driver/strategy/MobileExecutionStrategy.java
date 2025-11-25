package driver.strategy;

import adapter.AdapterFactory;
import adapter.AppiumAdapter;
import adapter.WebAutomationAdapter;
import driver.ConfigReader;
import org.openqa.selenium.WebDriver;

/**
 * Mobile Execution Strategy using Appium
 *
 * Implements execution strategy for mobile testing using Appium.
 * Supports both Android and iOS devices/simulators/emulators.
 *
 * <h3>Design Pattern: STRATEGY</h3>
 *
 * <h4>When to Use:</h4>
 * - Mobile app testing (Android/iOS)
 * - Hybrid app testing (Native + WebView)
 * - Testing on real devices
 * - Testing on emulators/simulators
 * - Mobile web testing
 *
 * <h4>Configuration Properties:</h4>
 * <pre>
 * execution.environment=mobile
 * mobile.platform=android         # or ios
 * mobile.deviceName=Android Emulator
 * mobile.platformVersion=11.0
 * mobile.app=/path/to/app.apk     # or app.ipa
 * mobile.appPackage=com.example.app
 * mobile.appActivity=.MainActivity
 * appium.server.url=http://localhost:4723
 * </pre>
 *
 * <h4>Features:</h4>
 * - Android testing via AndroidDriver
 * - iOS testing via IOSDriver
 * - Real device and simulator/emulator support
 * - Mobile-specific gestures and actions
 * - Context switching (Native/WebView)
 * - App installation and management
 *
 * <h4>Usage:</h4>
 * <pre>
 * // Set environment
 * System.setProperty("execution.environment", "mobile");
 * System.setProperty("mobile.platform", "android");
 *
 * // Get strategy
 * ExecutionStrategy strategy = ExecutionStrategyFactory.getStrategy();
 * WebDriver driver = strategy.createDriver("android", false);
 * </pre>
 *
 * @author Selenium Test Automation Framework
 * @version 1.0
 */
public class MobileExecutionStrategy implements ExecutionStrategy {

    private static final String MOBILE_PLATFORM_KEY = "mobile.platform";
    private static final String APPIUM_SERVER_URL_KEY = "appium.server.url";
    private static final String MOBILE_DEVICE_NAME_KEY = "mobile.deviceName";
    private static final String MOBILE_APP_KEY = "mobile.app";

    private static final String DEFAULT_PLATFORM = "android";
    private static final String DEFAULT_APPIUM_URL = "http://localhost:4723";

    /**
     * Create mobile driver (Android or iOS)
     *
     * @param deviceType Device type: "android", "ios", "android-emulator", "ios-simulator"
     * @param headless Not applicable for mobile (ignored)
     * @return AppiumDriver instance wrapped as WebDriver
     */
    @Override
    public WebDriver createDriver(String deviceType, boolean headless) {
        try {
            System.out.println("========================================");
            System.out.println("📱 MOBILE EXECUTION STRATEGY");
            System.out.println("========================================");

            // Determine platform if not specified
            if (deviceType == null || deviceType.trim().isEmpty()) {
                deviceType = ConfigReader.getProperty(MOBILE_PLATFORM_KEY, DEFAULT_PLATFORM);
            }

            System.out.println("Platform: " + deviceType);
            System.out.println("Appium Server: " + getAppiumServerUrl());

            // Create Appium adapter
            WebAutomationAdapter adapter = AdapterFactory.createAdapter("appium", deviceType, false);

            // Configure Appium server URL
            if (adapter instanceof AppiumAdapter) {
                ((AppiumAdapter) adapter).setAppiumServerUrl(getAppiumServerUrl());
            }

            System.out.println("✓ Mobile driver created successfully");
            System.out.println("========================================");

            // Return the underlying AppiumDriver
            return ((AppiumAdapter) adapter).getDriver();

        } catch (Exception e) {
            System.err.println("❌ Failed to create mobile driver: " + e.getMessage());
            System.err.println("Make sure Appium server is running at: " + getAppiumServerUrl());
            System.err.println("Start Appium with: appium");
            throw new RuntimeException("Mobile driver creation failed", e);
        }
    }

    /**
     * Get environment name
     *
     * @return "Mobile (Appium)"
     */
    @Override
    public String getEnvironmentName() {
        String platform = ConfigReader.getProperty(MOBILE_PLATFORM_KEY, DEFAULT_PLATFORM);
        return "Mobile (" + platform.toUpperCase() + " via Appium)";
    }

    /**
     * Get required configuration documentation
     *
     * @return Configuration requirements
     */
    @Override
    public String getRequiredConfiguration() {
        return  "Mobile Execution Strategy Configuration:\n" +
                "=========================================\n" +
                "Required Properties (config.properties):\n" +
                "  execution.environment=mobile\n" +
                "  mobile.platform=android|ios\n" +
                "\n" +
                "Optional Properties:\n" +
                "  appium.server.url=http://localhost:4723\n" +
                "  mobile.deviceName=Android Emulator|iPhone 14\n" +
                "  mobile.platformVersion=11.0|16.0\n" +
                "  mobile.app=/path/to/app.apk|app.ipa\n" +
                "  mobile.appPackage=com.example.app (Android)\n" +
                "  mobile.appActivity=.MainActivity (Android)\n" +
                "  mobile.bundleId=com.example.app (iOS)\n" +
                "\n" +
                "Prerequisites:\n" +
                "  1. Appium Server installed and running\n" +
                "     Install: npm install -g appium\n" +
                "     Run: appium\n" +
                "  2. For Android:\n" +
                "     - Android SDK installed\n" +
                "     - Android emulator or real device\n" +
                "     - UiAutomator2 driver: appium driver install uiautomator2\n" +
                "  3. For iOS:\n" +
                "     - Xcode installed (macOS only)\n" +
                "     - iOS Simulator or real device\n" +
                "     - XCUITest driver: appium driver install xcuitest\n" +
                "\n" +
                "Example Commands:\n" +
                "  # Install Appium\n" +
                "  npm install -g appium\n" +
                "  \n" +
                "  # Install drivers\n" +
                "  appium driver install uiautomator2\n" +
                "  appium driver install xcuitest\n" +
                "  \n" +
                "  # Start Appium server\n" +
                "  appium\n" +
                "  \n" +
                "  # List Android devices\n" +
                "  adb devices\n" +
                "  \n" +
                "  # List iOS simulators\n" +
                "  xcrun simctl list devices\n";
    }

    /**
     * Check if strategy is properly configured
     *
     * @return true if Appium can be used
     */
    @Override
    public boolean isConfigured() {
        try {
            // Check if Appium server URL is configured
            String appiumUrl = getAppiumServerUrl();
            if (appiumUrl == null || appiumUrl.trim().isEmpty()) {
                System.out.println("⚠️ Appium server URL not configured");
                return false;
            }

            // Check if platform is configured
            String platform = ConfigReader.getProperty(MOBILE_PLATFORM_KEY);
            if (platform == null || platform.trim().isEmpty()) {
                System.out.println("ℹ️ Mobile platform not specified, will use default: " + DEFAULT_PLATFORM);
            }

            // Note: We can't check if Appium is actually running without attempting connection
            System.out.println("✓ Mobile strategy configuration is valid");
            return true;

        } catch (Exception e) {
            System.err.println("❌ Configuration check failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cleanup resources
     */
    @Override
    public void cleanup() {
        System.out.println("✓ Mobile execution strategy cleanup completed");
    }

    /**
     * Get Appium server URL from configuration
     */
    private String getAppiumServerUrl() {
        return ConfigReader.getProperty(APPIUM_SERVER_URL_KEY, DEFAULT_APPIUM_URL);
    }

    /**
     * Check if Appium server is running
     *
     * @param appiumUrl Appium server URL
     * @return true if server is accessible
     */
    public static boolean isAppiumServerRunning(String appiumUrl) {
        try {
            java.net.URL url = new java.net.URL(appiumUrl + "/status");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }
}

