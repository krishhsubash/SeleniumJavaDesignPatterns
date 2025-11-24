package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Login Page Object
 *
 * Encapsulates login page elements and interactions
 *
 * Design Patterns:
 * - Page Object Model: Separates page structure from test logic
 * - Page Factory: Uses @FindBy for lazy element initialization
 *
 * Elements:
 * - Username field: User ID input
 * - Password field: Password input
 * - Login button: Submit credentials
 * - Error message: Login failure notification
 */
public class LoginPage extends BasePage {

    // PAGE FACTORY: Elements initialized lazily with @FindBy

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    @FindBy(className = "error-message")
    private WebElement errorMessage;

    @FindBy(id = "welcome-message")
    private WebElement welcomeMessage;

    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPageName() {
        return "Login Page";
    }

    /**
     * Navigate to login page
     *
     * @param url Login page URL
     * @return this page object for method chaining
     */
    public LoginPage open(String url) {
        log("Opening login page: " + url);
        navigateTo(url);
        return this;
    }

    /**
     * Enter username
     *
     * @param username Username to enter
     * @return this page object for method chaining
     */
    public LoginPage enterUsername(String username) {
        log("Entering username: " + username);
        type(usernameField, username);
        return this;
    }

    /**
     * Enter password
     *
     * @param password Password to enter
     * @return this page object for method chaining
     */
    public LoginPage enterPassword(String password) {
        log("Entering password: ****");
        type(passwordField, password);
        return this;
    }

    /**
     * Click login button
     *
     * @return this page object for method chaining
     */
    public LoginPage clickLogin() {
        log("Clicking login button");
        click(loginButton);
        return this;
    }

    /**
     * Perform complete login (enter credentials and submit)
     *
     * @param username Username
     * @param password Password
     * @return this page object for method chaining
     */
    public LoginPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return this;
    }

    /**
     * Check if login was successful
     *
     * @return true if welcome message displayed, false otherwise
     */
    public boolean isLoginSuccessful() {
        boolean success = isElementDisplayed(welcomeMessage);
        log("Login successful: " + success);
        return success;
    }

    /**
     * Check if error message is displayed
     *
     * @return true if error visible, false otherwise
     */
    public boolean isErrorDisplayed() {
        boolean errorVisible = isElementDisplayed(errorMessage);
        log("Error message displayed: " + errorVisible);
        return errorVisible;
    }

    /**
     * Get error message text
     *
     * @return Error message text
     */
    public String getErrorMessage() {
        if (isErrorDisplayed()) {
            String error = getText(errorMessage);
            log("Error message: " + error);
            return error;
        }
        return "";
    }

    /**
     * Get welcome message text
     *
     * @return Welcome message text
     */
    public String getWelcomeMessage() {
        if (isLoginSuccessful()) {
            String welcome = getText(welcomeMessage);
            log("Welcome message: " + welcome);
            return welcome;
        }
        return "";
    }

    /**
     * Get username field element (for decorator pattern integration)
     *
     * @return WebElement username field
     */
    public WebElement getUsernameField() {
        return usernameField;
    }

    /**
     * Get password field element (for decorator pattern integration)
     *
     * @return WebElement password field
     */
    public WebElement getPasswordField() {
        return passwordField;
    }

    /**
     * Get login button element (for decorator pattern integration)
     *
     * @return WebElement login button
     */
    public WebElement getLoginButton() {
        return loginButton;
    }

    /**
     * Check if all login elements are present
     *
     * @return true if username, password, and login button are displayed
     */
    public boolean isLoginFormDisplayed() {
        boolean formDisplayed = isElementDisplayed(usernameField)
                && isElementDisplayed(passwordField)
                && isElementDisplayed(loginButton);
        log("Login form displayed: " + formDisplayed);
        return formDisplayed;
    }

    /**
     * Clear login form
     *
     * @return this page object for method chaining
     */
    public LoginPage clearForm() {
        log("Clearing login form");
        usernameField.clear();
        passwordField.clear();
        return this;
    }
}
