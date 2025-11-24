package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Google Search Page Object
 *
 * Encapsulates Google search page elements and interactions
 *
 * Design Patterns:
 * - Page Object Model: Separates page structure from test logic
 * - Page Factory: Uses @FindBy for lazy element initialization
 *
 * Elements:
 * - Search box: Input field for search queries
 * - Consent button: Cookie consent (may appear)
 * - Results container: Search results area
 */
public class GoogleSearchPage extends BasePage {

    // PAGE FACTORY: Elements initialized lazily with @FindBy

    @FindBy(name = "q")
    private WebElement searchBox;

    @FindBy(xpath = "//button[contains(., 'Accept') or contains(., 'I agree')]")
    private WebElement consentButton;

    @FindBy(id = "search")
    private WebElement searchResults;

    @FindBy(name = "btnK")
    private WebElement searchButton;

    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public GoogleSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getPageName() {
        return "Google Search Page";
    }

    /**
     * Navigate to Google homepage
     *
     * @return this page object for method chaining
     */
    public GoogleSearchPage open() {
        log("Opening Google homepage");
        navigateTo("https://www.google.com");
        return this;
    }

    /**
     * Handle cookie consent if present
     *
     * @return this page object for method chaining
     */
    public GoogleSearchPage handleConsent() {
        try {
            if (isElementDisplayed(consentButton)) {
                log("Accepting cookie consent");
                click(consentButton);
                Thread.sleep(500); // Brief wait for consent dialog to close
            }
        } catch (Exception e) {
            log("No consent dialog found or already dismissed");
        }
        return this;
    }

    /**
     * Enter search term
     *
     * @param searchTerm Text to search for
     * @return this page object for method chaining
     */
    public GoogleSearchPage enterSearchTerm(String searchTerm) {
        log("Entering search term: " + searchTerm);
        type(searchBox, searchTerm);
        return this;
    }

    /**
     * Submit search (press Enter)
     *
     * @return this page object for method chaining
     */
    public GoogleSearchPage submitSearch() {
        log("Submitting search");
        searchBox.submit();
        return this;
    }

    /**
     * Perform complete search (enter term and submit)
     *
     * @param searchTerm Text to search for
     * @return this page object for method chaining
     */
    public GoogleSearchPage search(String searchTerm) {
        enterSearchTerm(searchTerm);
        submitSearch();
        return this;
    }

    /**
     * Check if search results are displayed
     *
     * @return true if results visible, false otherwise
     */
    public boolean isResultsDisplayed() {
        boolean displayed = isElementDisplayed(searchResults);
        log("Search results displayed: " + displayed);
        return displayed;
    }

    /**
     * Get search box element (for decorator pattern integration)
     *
     * @return WebElement search box
     */
    public WebElement getSearchBox() {
        return searchBox;
    }

    /**
     * Check if on Google search page
     *
     * @return true if on Google, false otherwise
     */
    public boolean isOnGooglePage() {
        return getCurrentUrl().contains("google.com");
    }

    /**
     * Wait for search results to load
     *
     * @return this page object for method chaining
     */
    public GoogleSearchPage waitForResults() {
        log("Waiting for search results");
        waitForElementVisible(searchResults);
        return this;
    }
}

