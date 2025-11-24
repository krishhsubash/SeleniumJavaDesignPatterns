package screenplay.interactions;

import screenplay.Actor;
import screenplay.Performable;
import screenplay.abilities.BrowseTheWeb;

/**
 * Interaction to click on an element.
 * 
 * Example usage:
 * <pre>
 * actor.attemptsTo(Click.on("css=#loginButton"));
 * actor.attemptsTo(Click.on("xpath=//button[@id='submit']"));
 * actor.attemptsTo(Click.on("id=searchBtn"));
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class Click implements Performable {
    
    private final String locator;
    
    private Click(String locator) {
        this.locator = locator;
    }
    
    /**
     * Creates a Click interaction for the given locator.
     * 
     * @param locator The element locator
     * @return A Click interaction
     */
    public static Click on(String locator) {
        return new Click(locator);
    }
    
    @Override
    public void performAs(Actor actor) {
        BrowseTheWeb browser = actor.abilityTo(BrowseTheWeb.class);
        browser.click(locator);
    }
    
    @Override
    public String toString() {
        return String.format("click on '%s'", locator);
    }
}
