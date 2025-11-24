package decorator;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Decorator that highlights WebElements before interaction
 * Useful for debugging and visual feedback during test execution
 */
public class HighlightingWebElement extends WebElementDecorator {

    private static final String HIGHLIGHT_STYLE = "border: 3px solid red; background-color: yellow;";
    private static final int HIGHLIGHT_DURATION_MS = 500;
    private final String originalStyle;

    public HighlightingWebElement(WebElement element, WebDriver driver) {
        super(element, driver);
        this.originalStyle = element.getAttribute("style");
    }

    @Override
    public void click() {
        highlightElement();
        super.click();
        restoreOriginalStyle();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        highlightElement();
        super.sendKeys(keysToSend);
        restoreOriginalStyle();
    }

    @Override
    public void clear() {
        highlightElement();
        super.clear();
        restoreOriginalStyle();
    }

    @Override
    public void submit() {
        highlightElement();
        super.submit();
        restoreOriginalStyle();
    }

    /**
     * Highlight the element with a border and background color
     */
    private void highlightElement() {
        if (driver instanceof JavascriptExecutor) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // Unwrap the element to get the original WebElement for JavaScript executor
            WebElement targetElement = getOriginalElement();
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
                    targetElement, HIGHLIGHT_STYLE);

            // Wait for visibility
            try {
                Thread.sleep(HIGHLIGHT_DURATION_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Restore the original style of the element
     */
    private void restoreOriginalStyle() {
        if (driver instanceof JavascriptExecutor) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String styleToRestore = originalStyle != null ? originalStyle : "";
            // Unwrap the element to get the original WebElement for JavaScript executor
            WebElement targetElement = getOriginalElement();
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
                    targetElement, styleToRestore);
        }
    }

    /**
     * Highlight element without interaction
     * Useful for visual feedback
     */
    public void highlight() {
        highlightElement();
        restoreOriginalStyle();
    }
}


