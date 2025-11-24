package screenplay.questions;

import screenplay.Actor;
import screenplay.Question;
import screenplay.abilities.BrowseTheWeb;

/**
 * Question to retrieve the page title.
 * 
 * Example usage:
 * <pre>
 * String title = actor.asksFor(TheTitle.ofThePage());
 * String title = actor.sees(TheTitle.ofThePage());
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class TheTitle implements Question<String> {
    
    private TheTitle() {
        // Private constructor to enforce factory method usage
    }
    
    /**
     * Creates a TheTitle question for the current page.
     * 
     * @return A TheTitle question
     */
    public static TheTitle ofThePage() {
        return new TheTitle();
    }
    
    @Override
    public String answeredBy(Actor actor) {
        BrowseTheWeb browser = actor.abilityTo(BrowseTheWeb.class);
        return browser.getPageTitle();
    }
    
    @Override
    public String toString() {
        return "the page title";
    }
}
