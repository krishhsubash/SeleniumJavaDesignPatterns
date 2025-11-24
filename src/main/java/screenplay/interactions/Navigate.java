package screenplay.interactions;

import screenplay.Actor;
import screenplay.Performable;
import screenplay.abilities.BrowseTheWeb;

/**
 * Interaction to navigate to a URL.
 * 
 * Example usage:
 * <pre>
 * actor.attemptsTo(Navigate.to("https://www.google.com"));
 * actor.attemptsTo(Navigate.to("https://example.com/login"));
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class Navigate implements Performable {
    
    private final String url;
    
    private Navigate(String url) {
        this.url = url;
    }
    
    /**
     * Creates a Navigate interaction for the given URL.
     * 
     * @param url The URL to navigate to
     * @return A Navigate interaction
     */
    public static Navigate to(String url) {
        return new Navigate(url);
    }
    
    @Override
    public void performAs(Actor actor) {
        BrowseTheWeb browser = actor.abilityTo(BrowseTheWeb.class);
        browser.navigateTo(url);
    }
    
    @Override
    public String toString() {
        return String.format("navigate to '%s'", url);
    }
}
