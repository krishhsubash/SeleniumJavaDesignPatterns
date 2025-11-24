package decorator;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Decorator that captures screenshots of WebElement actions
 * Automatically saves screenshots before and after critical operations
 */
public class ScreenshotWebElement extends WebElementDecorator {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotWebElement.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");

    private final String elementName;
    private final boolean captureOnError;
    private final boolean captureOnSuccess;

    public ScreenshotWebElement(WebElement element, WebDriver driver, String elementName) {
        this(element, driver, elementName, true, false);
    }

    public ScreenshotWebElement(WebElement element, WebDriver driver, String elementName,
                                boolean captureOnError, boolean captureOnSuccess) {
        super(element, driver);
        this.elementName = elementName;
        this.captureOnError = captureOnError;
        this.captureOnSuccess = captureOnSuccess;
        ensureScreenshotDirectoryExists();
    }

    @Override
    public void click() {
        String action = "click";
        try {
            if (captureOnSuccess) {
                captureScreenshot(action, "before");
            }
            super.click();
            if (captureOnSuccess) {
                captureScreenshot(action, "after");
            }
        } catch (Exception e) {
            if (captureOnError) {
                captureScreenshot(action, "error");
            }
            throw e;
        }
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        String action = "sendKeys";
        try {
            if (captureOnSuccess) {
                captureScreenshot(action, "before");
            }
            super.sendKeys(keysToSend);
            if (captureOnSuccess) {
                captureScreenshot(action, "after");
            }
        } catch (Exception e) {
            if (captureOnError) {
                captureScreenshot(action, "error");
            }
            throw e;
        }
    }

    @Override
    public void clear() {
        String action = "clear";
        try {
            if (captureOnSuccess) {
                captureScreenshot(action, "before");
            }
            super.clear();
            if (captureOnSuccess) {
                captureScreenshot(action, "after");
            }
        } catch (Exception e) {
            if (captureOnError) {
                captureScreenshot(action, "error");
            }
            throw e;
        }
    }

    @Override
    public void submit() {
        String action = "submit";
        try {
            if (captureOnSuccess) {
                captureScreenshot(action, "before");
            }
            super.submit();
            if (captureOnSuccess) {
                captureScreenshot(action, "after");
            }
        } catch (Exception e) {
            if (captureOnError) {
                captureScreenshot(action, "error");
            }
            throw e;
        }
    }

    /**
     * Capture screenshot of the entire page
     * @param action the action being performed
     * @param stage the stage (before/after/error)
     */
    private void captureScreenshot(String action, String stage) {
        try {
            if (driver instanceof TakesScreenshot) {
                TakesScreenshot screenshotDriver = (TakesScreenshot) driver;
                File screenshot = screenshotDriver.getScreenshotAs(OutputType.FILE);

                String timestamp = dateFormat.format(new Date());
                String fileName = String.format("%s_%s_%s_%s.png",
                        sanitizeFileName(elementName), action, stage, timestamp);

                Path targetPath = Paths.get(SCREENSHOT_DIR, fileName);
                FileUtils.copyFile(screenshot, targetPath.toFile());

                logger.info("Screenshot captured: {}", targetPath);
            }
        } catch (IOException e) {
            logger.error("Failed to capture screenshot for {} - {}", elementName, action, e);
        }
    }

    /**
     * Capture element screenshot (Selenium 4+)
     * @param action the action being performed
     * @param stage the stage (before/after/error)
     */
    public void captureElementScreenshot(String action, String stage) {
        try {
            File screenshot = element.getScreenshotAs(OutputType.FILE);

            String timestamp = dateFormat.format(new Date());
            String fileName = String.format("%s_element_%s_%s_%s.png",
                    sanitizeFileName(elementName), action, stage, timestamp);

            Path targetPath = Paths.get(SCREENSHOT_DIR, fileName);
            FileUtils.copyFile(screenshot, targetPath.toFile());

            logger.info("Element screenshot captured: {}", targetPath);
        } catch (Exception e) {
            logger.error("Failed to capture element screenshot for {} - {}", elementName, action, e);
        }
    }

    /**
     * Ensure screenshot directory exists
     */
    private void ensureScreenshotDirectoryExists() {
        try {
            Path screenshotPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotPath)) {
                Files.createDirectories(screenshotPath);
                logger.info("Created screenshot directory: {}", SCREENSHOT_DIR);
            }
        } catch (IOException e) {
            logger.error("Failed to create screenshot directory", e);
        }
    }

    /**
     * Sanitize filename to remove invalid characters
     * @param name the original name
     * @return sanitized name
     */
    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}

