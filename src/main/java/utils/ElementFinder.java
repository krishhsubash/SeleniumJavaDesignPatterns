package utils;

import decorator.WebElementDecoratorFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Utility class for finding and decorating WebElements
 * Provides convenient methods to get enhanced WebElements with various decorators
 */
public class ElementFinder {
    
    private final WebDriver driver;
    
    public ElementFinder(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Find element with logging capability
     */
    public WebElement findWithLogging(By locator, String description) {
        WebElement element = driver.findElement(locator);
        return WebElementDecoratorFactory.withLogging(element, driver, description);
    }
    
    /**
     * Find element with error handling capability
     */
    public WebElement findWithErrorHandling(By locator) {
        WebElement element = driver.findElement(locator);
        return WebElementDecoratorFactory.withErrorHandling(element, driver);
    }
    
    /**
     * Find element with highlighting capability
     */
    public WebElement findWithHighlighting(By locator) {
        WebElement element = driver.findElement(locator);
        return WebElementDecoratorFactory.withHighlighting(element, driver);
    }
    
    /**
     * Find element with screenshot capability
     */
    public WebElement findWithScreenshot(By locator, String elementName) {
        WebElement element = driver.findElement(locator);
        return WebElementDecoratorFactory.withScreenshot(element, driver, elementName);
    }
    
    /**
     * Find element with all decorators (production mode)
     * Includes: Error Handling + Logging + Screenshot on Error
     */
    public WebElement findProductionElement(By locator, String description) {
        WebElement element = driver.findElement(locator);
        return WebElementDecoratorFactory.productionMode(element, driver, description);
    }
    
    /**
     * Find element with all decorators (debug mode)
     * Includes: Logging + Highlighting + Screenshot on Error & Success
     */
    public WebElement findDebugElement(By locator, String description) {
        WebElement element = driver.findElement(locator);
        return WebElementDecoratorFactory.debugMode(element, driver, description);
    }
    
    /**
     * Find element with full decoration
     * Includes: Error Handling + Logging + Highlighting + Screenshot
     */
    public WebElement findFullyDecoratedElement(By locator, String description) {
        WebElement element = driver.findElement(locator);
        return WebElementDecoratorFactory.fullyDecorated(element, driver, description);
    }
    
    /**
     * Find element with custom decorator combination using builder
     */
    public WebElement findCustomElement(By locator, String description, 
                                       boolean logging, boolean errorHandling, 
                                       boolean highlighting, boolean screenshot) {
        WebElement element = driver.findElement(locator);
        
        WebElementDecoratorFactory.DecoratorBuilder builder = 
            WebElementDecoratorFactory.DecoratorBuilder.builder(element, driver)
                .withDescription(description);
        
        if (logging) builder.withLogging();
        if (errorHandling) builder.withErrorHandling();
        if (highlighting) builder.withHighlighting();
        if (screenshot) builder.withScreenshot();
        
        return builder.build();
    }
    
    /**
     * Quick method to find element with safe defaults (error handling + logging)
     */
    public WebElement findSafe(By locator, String description) {
        WebElement element = driver.findElement(locator);
        return WebElementDecoratorFactory.withLoggingAndErrorHandling(element, driver, description);
    }
}
