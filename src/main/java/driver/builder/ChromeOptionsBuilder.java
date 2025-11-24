package driver.builder;

import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder Pattern for ChromeOptions
 * Provides step-by-step construction of complex ChromeOptions objects
 */
public class ChromeOptionsBuilder {

    private final ChromeOptions options;
    private final List<String> arguments;
    private final Map<String, Object> experimentalOptions;
    private final Map<String, Object> preferences;

    private ChromeOptionsBuilder() {
        this.options = new ChromeOptions();
        this.arguments = new ArrayList<>();
        this.experimentalOptions = new HashMap<>();
        this.preferences = new HashMap<>();
    }

    /**
     * Create a new ChromeOptionsBuilder instance
     *
     * @return new builder instance
     */
    public static ChromeOptionsBuilder builder() {
        return new ChromeOptionsBuilder();
    }

    /**
     * Enable headless mode
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder headless() {
        arguments.add("--headless=new");
        return this;
    }

    /**
     * Set headless mode conditionally
     *
     * @param enable true to enable headless
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder headless(boolean enable) {
        if (enable) {
            arguments.add("--headless=new");
        }
        return this;
    }

    /**
     * Enable incognito mode
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder incognito() {
        arguments.add("--incognito");
        return this;
    }

    /**
     * Maximize window on start
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder maximized() {
        arguments.add("--start-maximized");
        return this;
    }

    /**
     * Set custom window size
     *
     * @param width window width
     * @param height window height
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder windowSize(int width, int height) {
        arguments.add("--window-size=" + width + "," + height);
        return this;
    }

    /**
     * Disable GPU acceleration
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder disableGpu() {
        arguments.add("--disable-gpu");
        return this;
    }

    /**
     * Disable sandbox mode
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder noSandbox() {
        arguments.add("--no-sandbox");
        return this;
    }

    /**
     * Disable shared memory usage
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder disableDevShmUsage() {
        arguments.add("--disable-dev-shm-usage");
        return this;
    }

    /**
     * Disable notifications
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder disableNotifications() {
        arguments.add("--disable-notifications");
        return this;
    }

    /**
     * Disable extensions
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder disableExtensions() {
        arguments.add("--disable-extensions");
        return this;
    }

    /**
     * Disable popup blocking
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder disablePopupBlocking() {
        arguments.add("--disable-popup-blocking");
        return this;
    }

    /**
     * Disable automation flags
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder disableAutomationFlags() {
        arguments.add("--disable-blink-features=AutomationControlled");
        experimentalOptions.put("excludeSwitches", new String[]{"enable-automation"});
        experimentalOptions.put("useAutomationExtension", false);
        return this;
    }

    /**
     * Set custom user agent
     *
     * @param userAgent custom user agent string
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder userAgent(String userAgent) {
        arguments.add("--user-agent=" + userAgent);
        return this;
    }

    /**
     * Set download directory
     *
     * @param downloadPath path to download directory
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder downloadPath(String downloadPath) {
        preferences.put("download.default_directory", downloadPath);
        preferences.put("download.prompt_for_download", false);
        preferences.put("download.directory_upgrade", true);
        preferences.put("safebrowsing.enabled", true);
        return this;
    }

    /**
     * Accept insecure certificates
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder acceptInsecureCerts() {
        options.setAcceptInsecureCerts(true);
        return this;
    }

    /**
     * Add custom argument
     *
     * @param argument custom Chrome argument
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder addArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    /**
     * Add experimental option
     *
     * @param name option name
     * @param value option value
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder addExperimentalOption(String name, Object value) {
        experimentalOptions.put(name, value);
        return this;
    }

    /**
     * Add preference
     *
     * @param name preference name
     * @param value preference value
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder addPreference(String name, Object value) {
        preferences.put(name, value);
        return this;
    }

    /**
     * Apply standard stability settings for CI/CD environments
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder withStabilitySettings() {
        return this
                .noSandbox()
                .disableDevShmUsage()
                .disableGpu()
                .disableNotifications();
    }

    /**
     * Apply settings for CI/CD environments
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder forCICD() {
        return this
                .headless()
                .withStabilitySettings()
                .disableExtensions()
                .windowSize(1920, 1080);
    }

    /**
     * Apply settings for debugging
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder forDebugging() {
        return this
                .maximized()
                .windowSize(1920, 1080);
    }

    /**
     * Apply anti-CAPTCHA detection settings
     * Configures Chrome to appear more like a human-driven browser
     *
     * @return this builder for chaining
     */
    public ChromeOptionsBuilder withAntiCaptchaSettings() {
        // Disable automation flags
        this.disableAutomationFlags();

        // Set realistic user agent
        this.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        // Additional preferences to avoid detection
        preferences.put("credentials_enable_service", false);
        preferences.put("profile.password_manager_enabled", false);

        // Disable save password prompts
        experimentalOptions.put("excludeSwitches", new String[]{"enable-automation", "enable-logging"});

        return this;
    }

    /**
     * Build and return the configured ChromeOptions
     *
     * @return configured ChromeOptions instance
     */
    public ChromeOptions build() {
        // Add all arguments
        if (!arguments.isEmpty()) {
            options.addArguments(arguments);
        }

        // Add experimental options
        for (Map.Entry<String, Object> entry : experimentalOptions.entrySet()) {
            options.setExperimentalOption(entry.getKey(), entry.getValue());
        }

        // Add preferences
        if (!preferences.isEmpty()) {
            options.setExperimentalOption("prefs", preferences);
        }

        return options;
    }
}
