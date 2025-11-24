package screenplay.interactions;

import screenplay.Actor;
import screenplay.Performable;
import screenplay.abilities.BrowseTheWeb;

/**
 * Interaction to wait for an element to be visible.
 * 
 * Example usage:
 * <pre>
 * actor.attemptsTo(WaitFor.element("css=#modal").toBeVisible());
 * actor.attemptsTo(WaitFor.element("id=loadingSpinner").toBeVisible().forSeconds(30));
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class WaitFor implements Performable {
    
    private final String locator;
    private int timeoutSeconds = 10; // default timeout
    
    private WaitFor(String locator) {
        this.locator = locator;
    }
    
    /**
     * Creates a WaitFor interaction for the given element.
     * 
     * @param locator The element locator
     * @return A WaitFor interaction builder
     */
    public static WaitFor element(String locator) {
        return new WaitFor(locator);
    }
    
    /**
     * Specifies that we're waiting for visibility.
     * 
     * @return This WaitFor interaction
     */
    public WaitFor toBeVisible() {
        return this;
    }
    
    /**
     * Specifies the timeout in seconds.
     * 
     * @param seconds The timeout in seconds
     * @return This WaitFor interaction
     */
    public WaitFor forSeconds(int seconds) {
        this.timeoutSeconds = seconds;
        return this;
    }
    
    @Override
    public void performAs(Actor actor) {
        BrowseTheWeb browser = actor.abilityTo(BrowseTheWeb.class);
        browser.waitForVisible(locator, timeoutSeconds);
    }
    
    @Override
    public String toString() {
        return String.format("wait for '%s' to be visible (timeout: %ds)", locator, timeoutSeconds);
    }
}
