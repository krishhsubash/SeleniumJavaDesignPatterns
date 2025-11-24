package decorator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Decorator that adds logging to WebElement actions
 * Logs all interactions with the element for debugging and audit trails
 */
public class LoggingWebElement extends WebElementDecorator {

    private static final Logger logger = LoggerFactory.getLogger(LoggingWebElement.class);
    private final String elementDescription;

    public LoggingWebElement(WebElement element, WebDriver driver) {
        this(element, driver, "WebElement");
    }

    public LoggingWebElement(WebElement element, WebDriver driver, String description) {
        super(element, driver);
        this.elementDescription = description;
    }

    @Override
    public void click() {
        logger.info("Clicking on: {}", elementDescription);
        try {
            super.click();
            logger.info("Successfully clicked: {}", elementDescription);
        } catch (Exception e) {
            logger.error("Failed to click on: {}", elementDescription, e);
            throw e;
        }
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        logger.info("Sending keys to: {} | Keys: {}", elementDescription, maskSensitiveData(keysToSend));
        try {
            super.sendKeys(keysToSend);
            logger.info("Successfully sent keys to: {}", elementDescription);
        } catch (Exception e) {
            logger.error("Failed to send keys to: {}", elementDescription, e);
            throw e;
        }
    }

    @Override
    public void clear() {
        logger.info("Clearing: {}", elementDescription);
        try {
            super.clear();
            logger.info("Successfully cleared: {}", elementDescription);
        } catch (Exception e) {
            logger.error("Failed to clear: {}", elementDescription, e);
            throw e;
        }
    }

    @Override
    public void submit() {
        logger.info("Submitting: {}", elementDescription);
        try {
            super.submit();
            logger.info("Successfully submitted: {}", elementDescription);
        } catch (Exception e) {
            logger.error("Failed to submit: {}", elementDescription, e);
            throw e;
        }
    }

    @Override
    public String getText() {
        logger.debug("Getting text from: {}", elementDescription);
        String text = super.getText();
        logger.debug("Text retrieved from {}: {}", elementDescription, text);
        return text;
    }

    @Override
    public String getAttribute(String name) {
        logger.debug("Getting attribute '{}' from: {}", name, elementDescription);
        String value = super.getAttribute(name);
        logger.debug("Attribute '{}' value from {}: {}", name, elementDescription, value);
        return value;
    }

    @Override
    public WebElement findElement(By by) {
        logger.debug("Finding element within {}: {}", elementDescription, by);
        WebElement foundElement = super.findElement(by);
        logger.debug("Element found within {}: {}", elementDescription, by);
        // Wrap the found element with logging decorator
        return new LoggingWebElement(foundElement, driver, by.toString());
    }

    @Override
    public boolean isDisplayed() {
        logger.debug("Checking if displayed: {}", elementDescription);
        boolean displayed = super.isDisplayed();
        logger.debug("{} is displayed: {}", elementDescription, displayed);
        return displayed;
    }

    @Override
    public boolean isEnabled() {
        logger.debug("Checking if enabled: {}", elementDescription);
        boolean enabled = super.isEnabled();
        logger.debug("{} is enabled: {}", elementDescription, enabled);
        return enabled;
    }

    @Override
    public boolean isSelected() {
        logger.debug("Checking if selected: {}", elementDescription);
        boolean selected = super.isSelected();
        logger.debug("{} is selected: {}", elementDescription, selected);
        return selected;
    }

    /**
     * Mask sensitive data in logs (passwords, credit cards, etc.)
     * @param keys CharSequence array to mask
     * @return masked string
     */
    private String maskSensitiveData(CharSequence... keys) {
        if (elementDescription.toLowerCase().contains("password") ||
                elementDescription.toLowerCase().contains("secret") ||
                elementDescription.toLowerCase().contains("token")) {
            return "****";
        }
        return Arrays.toString(keys);
    }
}

