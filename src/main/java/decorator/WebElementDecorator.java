package decorator;


import org.openqa.selenium.*;

import java.util.List;

/**
 * Base Decorator for WebElement
 * Implements WebElement interface and delegates all calls to the wrapped element
 * This serves as the foundation for all concrete decorators
 */
public abstract class WebElementDecorator implements WebElement {

    protected final WebElement element;
    protected final WebDriver driver;

    public WebElementDecorator(WebElement element, WebDriver driver) {
        this.element = element;
        this.driver = driver;
    }

    /**
     * Get the original unwrapped WebElement
     * Recursively unwraps decorator chains to get the base element
     */
    protected WebElement getOriginalElement() {
        WebElement current = this.element;
        while (current instanceof WebElementDecorator) {
            current = ((WebElementDecorator) current).element;
        }
        return current;
    }

    // Delegate all WebElement methods to the wrapped element

    @Override
    public void click() {
        element.click();
    }

    @Override
    public void submit() {
        element.submit();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        element.sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        element.clear();
    }

    @Override
    public String getTagName() {
        return element.getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return element.getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return element.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }

    @Override
    public String getText() {
        return element.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return element.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return element.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return element.getLocation();
    }

    @Override
    public Dimension getSize() {
        return element.getSize();
    }

    @Override
    public Rectangle getRect() {
        return element.getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return element.getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return element.getScreenshotAs(target);
    }

    @Override
    public SearchContext getShadowRoot() {
        return element.getShadowRoot();
    }

    @Override
    public String getAccessibleName() {
        return element.getAccessibleName();
    }

    @Override
    public String getAriaRole() {
        return element.getAriaRole();
    }

    @Override
    public String getDomAttribute(String name) {
        return element.getDomAttribute(name);
    }

    @Override
    public String getDomProperty(String name) {
        return element.getDomProperty(name);
    }

    /**
     * Get the underlying WebElement
     * @return wrapped WebElement
     */
    public WebElement getWrappedElement() {
        return element;
    }
}
