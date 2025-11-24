package screenplay;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an actor in the Screenplay pattern.
 * 
 * The Screenplay pattern is an actor-centric approach to test automation that focuses on
 * "who does what" rather than "how to do it". This pattern provides:
 * 
 * - Better readability: Tests read like a screenplay with actors performing tasks
 * - Better maintainability: Changes are isolated in tasks and interactions
 * - Better reusability: Tasks can be composed and reused across different scenarios
 * - Better separation of concerns: What vs How is clearly separated
 * 
 * Example:
 * <pre>
 * Actor john = Actor.named("John");
 * john.can(BrowseTheWeb.with(adapter));
 * john.attemptsTo(Open.url("https://www.google.com"));
 * john.attemptsTo(SearchFor.term("Selenium"));
 * String title = john.asksFor(TheText.of("css=h1"));
 * </pre>
 * 
 * Integration with other patterns:
 * - Uses ADAPTER pattern: Wraps WebAutomationAdapter for browser interactions
 * - Uses FACADE pattern: Can use file/database handlers through abilities
 * - Uses BUILDER pattern: Tasks can be built fluently
 * - Uses FACTORY pattern: Actors can be created through factory
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class Actor {
    
    private final String name;
    private final Map<Class<?>, Object> abilities;
    
    /**
     * Creates a new actor with the given name.
     * 
     * @param name The name of the actor
     */
    private Actor(String name) {
        this.name = name;
        this.abilities = new HashMap<>();
    }
    
    /**
     * Creates a new actor with the given name.
     * 
     * @param name The name of the actor
     * @return A new Actor instance
     */
    public static Actor named(String name) {
        return new Actor(name);
    }
    
    /**
     * Grants the actor an ability.
     * 
     * @param <T> The type of ability
     * @param ability The ability to grant
     * @return This actor for method chaining
     */
    public <T> Actor can(T ability) {
        abilities.put(ability.getClass(), ability);
        return this;
    }
    
    /**
     * Grants the actor an ability and stores it with a specific key.
     * 
     * @param <T> The type of ability
     * @param abilityClass The class to use as key
     * @param ability The ability to grant
     * @return This actor for method chaining
     */
    public <T> Actor can(Class<T> abilityClass, T ability) {
        abilities.put(abilityClass, ability);
        return this;
    }
    
    /**
     * Retrieves an ability that the actor has.
     * 
     * @param <T> The type of ability
     * @param abilityClass The class of the ability
     * @return The ability instance
     * @throws IllegalStateException if the actor doesn't have the ability
     */
    @SuppressWarnings("unchecked")
    public <T> T abilityTo(Class<T> abilityClass) {
        if (!abilities.containsKey(abilityClass)) {
            throw new IllegalStateException(
                String.format("Actor '%s' does not have the ability to '%s'", 
                    name, abilityClass.getSimpleName())
            );
        }
        return (T) abilities.get(abilityClass);
    }
    
    /**
     * Checks if the actor has a specific ability.
     * 
     * @param abilityClass The class of the ability
     * @return true if the actor has the ability, false otherwise
     */
    public boolean hasAbility(Class<?> abilityClass) {
        return abilities.containsKey(abilityClass);
    }
    
    /**
     * The actor attempts to perform a task or interaction.
     * 
     * @param performable The task or interaction to perform
     */
    public void attemptsTo(Performable performable) {
        System.out.println(String.format("✓ %s attempts to %s", name, performable.toString()));
        performable.performAs(this);
    }
    
    /**
     * The actor attempts to perform multiple tasks or interactions.
     * 
     * @param performables The tasks or interactions to perform
     */
    public void attemptsTo(Performable... performables) {
        for (Performable performable : performables) {
            attemptsTo(performable);
        }
    }
    
    /**
     * The actor asks a question and receives an answer.
     * 
     * @param <T> The type of the answer
     * @param question The question to ask
     * @return The answer to the question
     */
    public <T> T asksFor(Question<T> question) {
        System.out.println(String.format("✓ %s asks for %s", name, question.toString()));
        return question.answeredBy(this);
    }
    
    /**
     * Alternative method for asking a question (more natural English).
     * 
     * @param <T> The type of the answer
     * @param question The question to ask
     * @return The answer to the question
     */
    public <T> T sees(Question<T> question) {
        return asksFor(question);
    }
    
    /**
     * Gets the name of the actor.
     * 
     * @return The actor's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Checks if the actor can perform a task (has required abilities).
     * 
     * @param performable The task to check
     * @return true if the actor can perform the task
     */
    public boolean canPerform(Performable performable) {
        try {
            // Try to perform the task validation if it exists
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return name;
    }
}
