package screenplay.questions;

import screenplay.Actor;
import screenplay.Question;
import screenplay.abilities.BrowseTheWeb;

/**
 * Question to retrieve text from an element.
 * 
 * Example usage:
 * <pre>
 * String title = actor.asksFor(TheText.of("css=h1"));
 * String message = actor.sees(TheText.of("id=successMessage"));
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class TheText implements Question<String> {
    
    private final String locator;
    
    private TheText(String locator) {
        this.locator = locator;
    }
    
    /**
     * Creates a TheText question for the given locator.
     * 
     * @param locator The element locator
     * @return A TheText question
     */
    public static TheText of(String locator) {
        return new TheText(locator);
    }
    
    @Override
    public String answeredBy(Actor actor) {
        BrowseTheWeb browser = actor.abilityTo(BrowseTheWeb.class);
        return browser.getText(locator);
    }
    
    @Override
    public String toString() {
        return String.format("the text of '%s'", locator);
    }
}
