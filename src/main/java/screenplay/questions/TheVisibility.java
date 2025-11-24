package screenplay.questions;

import screenplay.Actor;
import screenplay.Question;
import screenplay.abilities.BrowseTheWeb;

/**
 * Question to check if an element is visible.
 * 
 * Example usage:
 * <pre>
 * boolean isVisible = actor.asksFor(TheVisibility.of("css=#modal"));
 * boolean isDisplayed = actor.sees(TheVisibility.of("id=errorMessage"));
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class TheVisibility implements Question<Boolean> {
    
    private final String locator;
    
    private TheVisibility(String locator) {
        this.locator = locator;
    }
    
    /**
     * Creates a TheVisibility question for the given locator.
     * 
     * @param locator The element locator
     * @return A TheVisibility question
     */
    public static TheVisibility of(String locator) {
        return new TheVisibility(locator);
    }
    
    @Override
    public Boolean answeredBy(Actor actor) {
        BrowseTheWeb browser = actor.abilityTo(BrowseTheWeb.class);
        return browser.isDisplayed(locator);
    }
    
    @Override
    public String toString() {
        return String.format("the visibility of '%s'", locator);
    }
}
