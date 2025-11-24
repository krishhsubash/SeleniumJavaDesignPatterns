package driver.builder;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder Pattern for FirefoxOptions
 * Provides step-by-step construction of complex FirefoxOptions objects
 */
public class FirefoxOptionsBuilder {

    private final FirefoxOptions options;
    private final List<String> arguments;
    private final Map<String, Object> preferences;
    private FirefoxProfile profile;

    private FirefoxOptionsBuilder() {
        this.options = new FirefoxOptions();
        this.arguments = new ArrayList<>();
        this.preferences = new HashMap<>();
        this.profile = null;
    }

    /**
     * Create a new FirefoxOptionsBuilder instance
     *
     * @return new builder instance
     */
    public static FirefoxOptionsBuilder builder() {
        return new FirefoxOptionsBuilder();
    }

    /**
     * Enable headless mode
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder headless() {
        arguments.add("--headless");
        return this;
    }

    /**
     * Set headless mode conditionally
     *
     * @param enable true to enable headless
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder headless(boolean enable) {
        if (enable) {
            arguments.add("--headless");
        }
        return this;
    }

    /**
     * Enable private browsing mode
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder privateMode() {
        arguments.add("--private");
        return this;
    }

    /**
     * Set custom window size
     *
     * @param width window width
     * @param height window height
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder windowSize(int width, int height) {
        arguments.add("--width=" + width);
        arguments.add("--height=" + height);
        return this;
    }

    /**
     * Disable notifications
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder disableNotifications() {
        preferences.put("dom.webnotifications.enabled", false);
        preferences.put("dom.push.enabled", false);
        return this;
    }

    /**
     * Set download directory
     *
     * @param downloadPath path to download directory
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder downloadPath(String downloadPath) {
        preferences.put("browser.download.dir", downloadPath);
        preferences.put("browser.download.folderList", 2);
        preferences.put("browser.download.useDownloadDir", true);
        preferences.put("browser.helperApps.neverAsk.saveToDisk",
                "application/pdf,application/zip,application/octet-stream,text/csv,text/plain");
        return this;
    }

    /**
     * Accept insecure certificates
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder acceptInsecureCerts() {
        options.setAcceptInsecureCerts(true);
        return this;
    }

    /**
     * Disable images for faster page loading
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder disableImages() {
        preferences.put("permissions.default.image", 2);
        return this;
    }

    /**
     * Disable CSS for faster page loading
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder disableCSS() {
        preferences.put("permissions.default.stylesheet", 2);
        return this;
    }

    /**
     * Set custom user agent
     *
     * @param userAgent custom user agent string
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder userAgent(String userAgent) {
        preferences.put("general.useragent.override", userAgent);
        return this;
    }

    /**
     * Add custom argument
     *
     * @param argument custom Firefox argument
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder addArgument(String argument) {
        arguments.add(argument);
        return this;
    }

    /**
     * Add preference
     *
     * @param name preference name
     * @param value preference value
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder addPreference(String name, Object value) {
        preferences.put(name, value);
        return this;
    }

    /**
     * Use custom Firefox profile
     *
     * @param firefoxProfile custom profile
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder withProfile(FirefoxProfile firefoxProfile) {
        this.profile = firefoxProfile;
        return this;
    }

    /**
     * Apply standard stability settings
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder withStabilitySettings() {
        return this
                .disableNotifications()
                .windowSize(1920, 1080);
    }

    /**
     * Apply settings for CI/CD environments
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder forCICD() {
        return this
                .headless()
                .withStabilitySettings()
                .windowSize(1920, 1080);
    }

    /**
     * Apply settings for debugging
     *
     * @return this builder for chaining
     */
    public FirefoxOptionsBuilder forDebugging() {
        return this
                .windowSize(1920, 1080);
    }

    /**
     * Build and return the configured FirefoxOptions
     *
     * @return configured FirefoxOptions instance
     */
    public FirefoxOptions build() {
        // Add all arguments
        if (!arguments.isEmpty()) {
            options.addArguments(arguments);
        }

        // Add all preferences
        for (Map.Entry<String, Object> entry : preferences.entrySet()) {
            options.addPreference(entry.getKey(), entry.getValue());
        }

        // Set profile if provided
        if (profile != null) {
            options.setProfile(profile);
        }

        return options;
    }
}

