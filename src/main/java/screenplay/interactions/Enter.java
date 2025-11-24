package screenplay.interactions;

import screenplay.Actor;
import screenplay.Performable;
import screenplay.abilities.BrowseTheWeb;

/**
 * Interaction to enter text into an element.
 * 
 * Example usage:
 * <pre>
 * actor.attemptsTo(Enter.text("john@example.com").into("css=#email"));
 * actor.attemptsTo(Enter.text("password123").into("id=password"));
 * actor.attemptsTo(Enter.text("Selenium").into("name=search"));
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class Enter implements Performable {
    
    private final String text;
    private String locator;
    
    private Enter(String text) {
        this.text = text;
    }
    
    /**
     * Creates an Enter interaction for the given text.
     * 
     * @param text The text to enter
     * @return An Enter interaction builder
     */
    public static Enter text(String text) {
        return new Enter(text);
    }
    
    /**
     * Specifies the target element to enter text into.
     * 
     * @param locator The element locator
     * @return This Enter interaction
     */
    public Enter into(String locator) {
        this.locator = locator;
        return this;
    }
    
    @Override
    public void performAs(Actor actor) {
        if (locator == null) {
            throw new IllegalStateException("No locator specified. Use .into(locator) to specify the target element.");
        }
        BrowseTheWeb browser = actor.abilityTo(BrowseTheWeb.class);
        browser.type(locator, text);
    }
    
    @Override
    public String toString() {
        return String.format("enter '%s' into '%s'", text, locator != null ? locator : "<not specified>");
    }
}
