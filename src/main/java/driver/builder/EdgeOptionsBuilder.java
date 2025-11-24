package driver.builder;


import org.openqa.selenium.edge.EdgeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder Pattern for EdgeOptions
 * Provides step-by-step construction of complex EdgeOptions objects
 */
public class EdgeOptionsBuilder {

    private final EdgeOptions options;
    private final List<String> arguments;
    private final Map<String, Object> experimentalOptions;
    private final Map<String, Object> preferences;

    private EdgeOptionsBuilder() {
        this.options = new EdgeOptions();
        this.arguments = new ArrayList<>();
        this.experimentalOptions = new HashMap<>();
        this.preferences = new HashMap<>();
    }

    /**
     * Create a new EdgeOptionsBuilder instance
     *
     * @return new builder instance
     */
    public static EdgeOptionsBuilder builder() {
        return new EdgeOptionsBuilder();
    }

    /**
     * Enable headless mode
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder headless() {
        arguments.add("--headless=new");
        return this;
    }

    /**
     * Set headless mode conditionally
     *
     * @param enable true to enable headless
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder headless(boolean enable) {
        if (enable) {
            arguments.add("--headless=new");
        }
        return this;
    }

    /**
     * Enable InPrivate browsing mode
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder inPrivate() {
        arguments.add("--inprivate");
        return this;
    }

    /**
     * Maximize window on start
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder maximized() {
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
    public EdgeOptionsBuilder windowSize(int width, int height) {
        arguments.add("--window-size=" + width + "," + height);
        return this;
    }

    /**
     * Disable GPU acceleration
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder disableGpu() {
        arguments.add("--disable-gpu");
        return this;
    }

    /**
     * Disable sandbox mode
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder noSandbox() {
        arguments.add("--no-sandbox");
        return this;
    }

    /**
     * Disable shared memory usage
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder disableDevShmUsage() {
        arguments.add("--disable-dev-shm-usage");
        return this;
    }

    /**
     * Disable notifications
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder disableNotifications() {
        arguments.add("--disable-notifications");
        return this;
    }

    /**
     * Disable extensions
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder disableExtensions() {
        arguments.add("--disable-extensions");
        return this;
    }

    /**
     * Disable popup blocking
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder disablePopupBlocking() {
        arguments.add("--disable-popup-blocking");
        return this;
    }

    /**
     * Disable automation flags
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder disableAutomationFlags() {
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
    public EdgeOptionsBuilder userAgent(String userAgent) {
        arguments.add("--user-agent=" + userAgent);
        return this;
    }

    /**
     * Set download directory
     *
     * @param downloadPath path to download directory
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder downloadPath(String downloadPath) {
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
    public EdgeOptionsBuilder acceptInsecureCerts() {
        options.setAcceptInsecureCerts(true);
        return this;
    }

    /**
     * Add custom argument
     *
     * @param argument custom Edge argument
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder addArgument(String argument) {
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
    public EdgeOptionsBuilder addExperimentalOption(String name, Object value) {
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
    public EdgeOptionsBuilder addPreference(String name, Object value) {
        preferences.put(name, value);
        return this;
    }

    /**
     * Apply standard stability settings for CI/CD environments
     *
     * @return this builder for chaining
     */
    public EdgeOptionsBuilder withStabilitySettings() {
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
    public EdgeOptionsBuilder forCICD() {
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
    public EdgeOptionsBuilder forDebugging() {
        return this
                .maximized()
                .windowSize(1920, 1080);
    }

    /**
     * Build and return the configured EdgeOptions
     *
     * @return configured EdgeOptions instance
     */
    public EdgeOptions build() {
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
