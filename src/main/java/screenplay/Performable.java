package screenplay;

/**
 * Represents something that can be performed by an Actor.
 * 
 * This is the base interface for both Tasks (high-level business operations)
 * and Interactions (low-level UI operations).
 * 
 * Example implementations:
 * - Tasks: SearchGoogle, Login, AddItemToCart
 * - Interactions: Click, Enter, Navigate, Select
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
@FunctionalInterface
public interface Performable {
    
    /**
     * Performs this action as the given actor.
     * 
     * @param actor The actor performing this action
     */
    void performAs(Actor actor);
}
