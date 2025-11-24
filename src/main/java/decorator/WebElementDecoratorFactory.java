package decorator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Factory for creating decorated WebElements
 * Supports chaining multiple decorators for combined functionality
 */
public class WebElementDecoratorFactory {

    private WebElementDecoratorFactory() {
        throw new IllegalStateException("Utility class - cannot be instantiated");
    }

    /**
     * Create a WebElement with logging capability
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @param description element description for logs
     * @return decorated WebElement with logging
     */
    public static WebElement withLogging(WebElement element, WebDriver driver, String description) {
        return new LoggingWebElement(element, driver, description);
    }

    /**
     * Create a WebElement with error handling capability
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @return decorated WebElement with error handling
     */
    public static WebElement withErrorHandling(WebElement element, WebDriver driver) {
        return new ErrorHandlingWebElement(element, driver);
    }

    /**
     * Create a WebElement with highlighting capability
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @return decorated WebElement with highlighting
     */
    public static WebElement withHighlighting(WebElement element, WebDriver driver) {
        return new HighlightingWebElement(element, driver);
    }

    /**
     * Create a WebElement with screenshot capability
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @param elementName name for screenshot files
     * @return decorated WebElement with screenshot capture
     */
    public static WebElement withScreenshot(WebElement element, WebDriver driver, String elementName) {
        return new ScreenshotWebElement(element, driver, elementName);
    }

    /**
     * Create a fully decorated WebElement with all capabilities
     * Order: ErrorHandling -> Logging -> Highlighting -> Screenshot
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @param description element description
     * @return fully decorated WebElement
     */
    public static WebElement fullyDecorated(WebElement element, WebDriver driver, String description) {
        // Layer 1: Error Handling (innermost - handles retries and waits)
        WebElement errorHandled = new ErrorHandlingWebElement(element, driver);

        // Layer 2: Logging (logs actions)
        LoggingWebElement logged = new LoggingWebElement(errorHandled, driver, description);

        // Layer 3: Highlighting (visual feedback)
        HighlightingWebElement highlighted = new HighlightingWebElement(logged, driver);

        // Layer 4: Screenshot (captures evidence)
        return new ScreenshotWebElement(highlighted, driver, description, true, false);
    }

    /**
     * Create a WebElement with logging and error handling
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @param description element description
     * @return decorated WebElement
     */
    public static WebElement withLoggingAndErrorHandling(WebElement element, WebDriver driver, String description) {
        WebElement errorHandled = new ErrorHandlingWebElement(element, driver);
        return new LoggingWebElement(errorHandled, driver, description);
    }

    /**
     * Create a WebElement with highlighting and logging
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @param description element description
     * @return decorated WebElement
     */
    public static WebElement withHighlightingAndLogging(WebElement element, WebDriver driver, String description) {
        WebElement logged = new LoggingWebElement(element, driver, description);
        return new HighlightingWebElement(logged, driver);
    }

    /**
     * Create a debug mode WebElement (logging + highlighting + screenshot)
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @param description element description
     * @return decorated WebElement for debugging
     */
    public static WebElement debugMode(WebElement element, WebDriver driver, String description) {
        WebElement logged = new LoggingWebElement(element, driver, description);
        WebElement highlighted = new HighlightingWebElement(logged, driver);
        return new ScreenshotWebElement(highlighted, driver, description, true, true);
    }

    /**
     * Create a production mode WebElement (error handling + logging + highlighting)
     * Updated to include highlighting for better visual feedback during test execution
     * @param element the element to decorate
     * @param driver the WebDriver instance
     * @param description element description
     * @return decorated WebElement for production
     */
    public static WebElement productionMode(WebElement element, WebDriver driver, String description) {
        WebElement errorHandled = new ErrorHandlingWebElement(element, driver);
        WebElement logged = new LoggingWebElement(errorHandled, driver, description);
        WebElement highlighted = new HighlightingWebElement(logged, driver);
        return new ScreenshotWebElement(highlighted, driver, description, true, false);
    }

    /**
     * Builder for custom decorator combinations
     */
    public static class DecoratorBuilder {
        private WebElement element;
        private WebDriver driver;
        private String description;
        private boolean includeLogging = false;
        private boolean includeErrorHandling = false;
        private boolean includeHighlighting = false;
        private boolean includeScreenshot = false;
        private boolean screenshotOnError = true;
        private boolean screenshotOnSuccess = false;

        private DecoratorBuilder(WebElement element, WebDriver driver) {
            this.element = element;
            this.driver = driver;
            this.description = "WebElement";
        }

        public static DecoratorBuilder builder(WebElement element, WebDriver driver) {
            return new DecoratorBuilder(element, driver);
        }

        public DecoratorBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public DecoratorBuilder withLogging() {
            this.includeLogging = true;
            return this;
        }

        public DecoratorBuilder withErrorHandling() {
            this.includeErrorHandling = true;
            return this;
        }

        public DecoratorBuilder withHighlighting() {
            this.includeHighlighting = true;
            return this;
        }

        public DecoratorBuilder withScreenshot() {
            this.includeScreenshot = true;
            return this;
        }

        public DecoratorBuilder withScreenshot(boolean onError, boolean onSuccess) {
            this.includeScreenshot = true;
            this.screenshotOnError = onError;
            this.screenshotOnSuccess = onSuccess;
            return this;
        }

        public WebElement build() {
            WebElement decorated = element;

            // Apply decorators in order: ErrorHandling -> Logging -> Highlighting -> Screenshot
            if (includeErrorHandling) {
                decorated = new ErrorHandlingWebElement(decorated, driver);
            }

            if (includeLogging) {
                decorated = new LoggingWebElement(decorated, driver, description);
            }

            if (includeHighlighting) {
                decorated = new HighlightingWebElement(decorated, driver);
            }

            if (includeScreenshot) {
                decorated = new ScreenshotWebElement(decorated, driver, description,
                        screenshotOnError, screenshotOnSuccess);
            }

            return decorated;
        }
    }
}

