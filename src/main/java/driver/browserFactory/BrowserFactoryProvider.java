package driver.browserFactory;

/**
 * Factory Provider to get the appropriate browser factory
 * Implements Factory of Factories pattern
 */
public class BrowserFactoryProvider {

    /**
     * Get the browser factory based on browser type
     *
     * @param browserType type of browser (chrome, firefox, edge)
     * @return BrowserFactory implementation
     * @throws IllegalArgumentException if browser type is not supported
     */
    public static BrowserFactory getBrowserFactory(String browserType) {
        if (browserType == null || browserType.trim().isEmpty()) {
            throw new IllegalArgumentException("Browser type cannot be null or empty");
        }

        switch (browserType.toLowerCase().trim()) {
            case "chrome":
                return new ChromeDriverFactory();

            case "firefox":
                return new FirefoxDriverFactory();

            case "edge":
                return new EdgeDriverFactory();

            default:
                throw new IllegalArgumentException(
                        "Unsupported browser type: " + browserType +
                                ". Supported browsers: chrome, firefox, edge"
                );
        }
    }
}
