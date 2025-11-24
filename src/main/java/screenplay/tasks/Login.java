package screenplay.tasks;

import screenplay.Actor;
import screenplay.Performable;
import screenplay.interactions.Click;
import screenplay.interactions.Enter;
import screenplay.interactions.Navigate;

/**
 * Task to log into a website.
 * 
 * This is a high-level task that combines multiple interactions
 * to perform the login operation.
 * 
 * Example usage:
 * <pre>
 * actor.attemptsTo(
 *     Login.withCredentials("user@example.com", "password123")
 *          .on("https://example.com/login")
 * );
 * </pre>
 * 
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class Login implements Performable {
    
    private final String username;
    private final String password;
    private String loginUrl;
    private String usernameLocator = "id=username";
    private String passwordLocator = "id=password";
    private String loginButtonLocator = "id=loginButton";
    
    private Login(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Creates a Login task with the given credentials.
     * 
     * @param username The username
     * @param password The password
     * @return A Login task builder
     */
    public static Login withCredentials(String username, String password) {
        return new Login(username, password);
    }
    
    /**
     * Specifies the login page URL.
     * 
     * @param url The login page URL
     * @return This Login task
     */
    public Login on(String url) {
        this.loginUrl = url;
        return this;
    }
    
    /**
     * Specifies custom locators for the login form elements.
     * 
     * @param usernameLocator The username field locator
     * @param passwordLocator The password field locator
     * @param loginButtonLocator The login button locator
     * @return This Login task
     */
    public Login using(String usernameLocator, String passwordLocator, String loginButtonLocator) {
        this.usernameLocator = usernameLocator;
        this.passwordLocator = passwordLocator;
        this.loginButtonLocator = loginButtonLocator;
        return this;
    }
    
    @Override
    public void performAs(Actor actor) {
        if (loginUrl != null) {
            actor.attemptsTo(Navigate.to(loginUrl));
        }
        
        actor.attemptsTo(
            Enter.text(username).into(usernameLocator),
            Enter.text(password).into(passwordLocator),
            Click.on(loginButtonLocator)
        );
    }
    
    @Override
    public String toString() {
        return String.format("login as '%s'", username);
    }
}
