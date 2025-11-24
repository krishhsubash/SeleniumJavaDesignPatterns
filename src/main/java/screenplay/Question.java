package screenplay;

/**
 * Represents a question that can be asked by an Actor.
 * 
 * Questions are used to retrieve information from the application under test.
 * They return a value that can be used in assertions or decision-making.
 * 
 * Example implementations:
 * - TheText.of("css=.title")
 * - TheVisibility.of("id=loginButton")
 * - TheTitle.ofThePage()
 * - TheValue.of("name=username")
 * 
 * @param <T> The type of answer this question returns
 * @author Selenium Automation Framework
 * @version 1.0
 */
@FunctionalInterface
public interface Question<T> {
    
    /**
     * Answers this question as the given actor.
     * 
     * @param actor The actor asking the question
     * @return The answer to the question
     */
    T answeredBy(Actor actor);
}
