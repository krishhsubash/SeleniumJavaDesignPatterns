package screenplay.tasks;

import screenplay.Actor;
import screenplay.Performable;
import screenplay.interactions.Click;
import screenplay.interactions.Enter;
import screenplay.interactions.Navigate;

/**
 * Task to search on Google.
 * 
 * This is a high-level task that combines multiple interactions
 * to perform a business operation.
 * 
 * Example usage:
 * <pre>
 * actor.attemptsTo(SearchGoogle.forTerm("Selenium WebDriver"));
 * actor.attemptsTo(SearchGoogle.forTerm("Cucumber BDD"));
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class SearchGoogle implements Performable {
    
    private final String searchTerm;
    
    private SearchGoogle(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    /**
     * Creates a SearchGoogle task for the given search term.
     * 
     * @param searchTerm The term to search for
     * @return A SearchGoogle task
     */
    public static SearchGoogle forTerm(String searchTerm) {
        return new SearchGoogle(searchTerm);
    }
    
    @Override
    public void performAs(Actor actor) {
        actor.attemptsTo(
            Navigate.to("https://www.google.com"),
            Enter.text(searchTerm).into("name=q"),
            Click.on("name=btnK")
        );
    }
    
    @Override
    public String toString() {
        return String.format("search Google for '%s'", searchTerm);
    }
}
