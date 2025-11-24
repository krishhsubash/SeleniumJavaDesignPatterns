package decorator;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Decorator that adds error handling and retry logic to WebElement actions
 * Handles common exceptions and retries operations with waits
 */
public class ErrorHandlingWebElement extends WebElementDecorator {

    private static final int MAX_RETRIES = 3;
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration RETRY_DELAY = Duration.ofMillis(500);

    public ErrorHandlingWebElement(WebElement element, WebDriver driver) {
        super(element, driver);
    }

    @Override
    public void click() {
        executeWithRetry(() -> {
            waitForClickable();
            super.click();
            return null;
        }, "click");
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        executeWithRetry(() -> {
            waitForVisible();
            super.sendKeys(keysToSend);
            return null;
        }, "sendKeys");
    }

    @Override
    public void clear() {
        executeWithRetry(() -> {
            waitForVisible();
            super.clear();
            return null;
        }, "clear");
    }

    @Override
    public void submit() {
        executeWithRetry(() -> {
            waitForVisible();
            super.submit();
            return null;
        }, "submit");
    }

    @Override
    public String getText() {
        return executeWithRetry(() -> {
            waitForVisible();
            return super.getText();
        }, "getText");
    }

    @Override
    public String getAttribute(String name) {
        return executeWithRetry(() -> {
            waitForPresent();
            return super.getAttribute(name);
        }, "getAttribute");
    }

    @Override
    public boolean isDisplayed() {
        return executeWithRetry(() -> {
            try {
                return super.isDisplayed();
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        }, "isDisplayed");
    }

    /**
     * Execute an action with retry logic
     * @param action the action to execute
     * @param actionName name of the action for error messages
     * @param <T> return type
     * @return result of the action
     */
    private <T> T executeWithRetry(SupplierWithException<T> action, String actionName) {
        Exception lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return action.get();
            } catch (StaleElementReferenceException e) {
                lastException = e;
                if (attempt < MAX_RETRIES) {
                    sleep(RETRY_DELAY);
                    continue;
                }
            } catch (ElementClickInterceptedException e) {
                lastException = e;
                if (attempt < MAX_RETRIES) {
                    sleep(RETRY_DELAY);
                    scrollIntoView();
                    continue;
                }
            } catch (ElementNotInteractableException e) {
                lastException = e;
                if (attempt < MAX_RETRIES) {
                    sleep(RETRY_DELAY);
                    continue;
                }
            } catch (TimeoutException e) {
                lastException = e;
                if (attempt < MAX_RETRIES) {
                    sleep(RETRY_DELAY);
                    continue;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to execute " + actionName + " on element", e);
            }
        }

        throw new RuntimeException(
                String.format("Failed to execute %s after %d retries", actionName, MAX_RETRIES),
                lastException
        );
    }

    /**
     * Wait for element to be clickable
     */
    private void waitForClickable() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not clickable within timeout: " + WAIT_TIMEOUT.getSeconds() + "s");
        }
    }

    /**
     * Wait for element to be visible
     */
    private void waitForVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not visible within timeout: " + WAIT_TIMEOUT.getSeconds() + "s");
        }
    }

    /**
     * Wait for element to be present
     */
    private void waitForPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
            wait.until(driver -> element != null);
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not present within timeout: " + WAIT_TIMEOUT.getSeconds() + "s");
        }
    }

    /**
     * Scroll element into view
     */
    private void scrollIntoView() {
        if (driver instanceof JavascriptExecutor) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            sleep(Duration.ofMillis(300));
        }
    }

    /**
     * Sleep for a duration
     */
    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Functional interface for suppliers that can throw exceptions
     */
    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}

