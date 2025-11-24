package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

/**
 * Utility class to handle Google CAPTCHA challenges programmatically
 * Provides automated strategies to avoid and bypass CAPTCHA challenges
 */
public class CaptchaHandler {

    private final WebDriver driver;
    private static final int MAX_RETRIES = 3;
    private static final long BASE_DELAY_MS = 2000;

    public CaptchaHandler(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Check if CAPTCHA is present on the page
     * @return true if CAPTCHA is detected
     */
    public boolean isCaptchaPresent() {
        try {
            // Check for reCAPTCHA iframe
            boolean hasRecaptcha = !driver.findElements(By.cssSelector("iframe[src*='recaptcha']")).isEmpty();

            // Check for "unusual traffic" text
            boolean hasUnusualTrafficText = driver.getPageSource().toLowerCase().contains("unusual traffic")
                    || driver.getPageSource().toLowerCase().contains("not a robot");

            // Check for CAPTCHA form
            boolean hasCaptchaForm = !driver.findElements(By.id("captcha-form")).isEmpty();

            return hasRecaptcha || hasUnusualTrafficText || hasCaptchaForm;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check and handle CAPTCHA programmatically if present
     * @return true if page is ready (no CAPTCHA or successfully bypassed), false otherwise
     */
    public boolean handleCaptchaAutomatically() {
        if (!isCaptchaPresent()) {
            return true;
        }

        System.out.println("⚠️ CAPTCHA detected - attempting automated bypass...");

        // Strategy 1: Wait and retry with new session
        if (bypassWithNewSession()) {
            System.out.println("✅ CAPTCHA bypassed using new session");
            return true;
        }

        // Strategy 2: Use alternative URL approach
        if (bypassWithAlternativeUrl()) {
            System.out.println("✅ CAPTCHA bypassed using alternative URL");
            return true;
        }

        // Strategy 3: Clear cookies and retry
        if (bypassWithCookieClear()) {
            System.out.println("✅ CAPTCHA bypassed after clearing cookies");
            return true;
        }

        System.out.println("❌ Could not bypass CAPTCHA automatically");
        return false;
    }

    /**
     * Bypass CAPTCHA by opening URL in new window/tab
     */
    private boolean bypassWithNewSession() {
        try {
            String currentUrl = driver.getCurrentUrl();
            String originalWindow = driver.getWindowHandle();

            // Open new tab
            ((JavascriptExecutor) driver).executeScript("window.open('about:blank', '_blank');");
            addDelay(1000);

            // Switch to new tab
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }

            // Add extra delays to appear human-like
            addDelay(BASE_DELAY_MS);

            // Navigate in new tab
            driver.get(currentUrl);
            addDelay(BASE_DELAY_MS);

            // Check if CAPTCHA is gone
            if (!isCaptchaPresent()) {
                // Close original tab
                driver.switchTo().window(originalWindow);
                driver.close();

                // Switch back to new tab
                for (String windowHandle : driver.getWindowHandles()) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
                return true;
            }

            // Failed - close new tab and return to original
            driver.close();
            driver.switchTo().window(originalWindow);
            return false;

        } catch (Exception e) {
            System.out.println("New session bypass failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Bypass CAPTCHA using alternative URL parameters
     */
    private boolean bypassWithAlternativeUrl() {
        try {
            String currentUrl = driver.getCurrentUrl();

            // Clear and reload with cache busting
            String urlWithParam = currentUrl + (currentUrl.contains("?") ? "&" : "?") +
                    "ts=" + System.currentTimeMillis();

            addDelay(BASE_DELAY_MS);
            driver.navigate().to(urlWithParam);
            addDelay(BASE_DELAY_MS * 2);

            return !isCaptchaPresent();

        } catch (Exception e) {
            System.out.println("Alternative URL bypass failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Bypass CAPTCHA by clearing cookies and retrying
     */
    private boolean bypassWithCookieClear() {
        try {
            String currentUrl = driver.getCurrentUrl();

            // Clear all cookies
            driver.manage().deleteAllCookies();
            addDelay(1000);

            // Clear local storage and session storage
            ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
            ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");

            // Wait before retry
            addDelay(BASE_DELAY_MS * 2);

            // Reload page
            driver.get(currentUrl);
            addDelay(BASE_DELAY_MS);

            return !isCaptchaPresent();

        } catch (Exception e) {
            System.out.println("Cookie clear bypass failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Wait for manual CAPTCHA resolution (fallback method)
     * @param timeoutSeconds maximum time to wait
     * @return true if CAPTCHA was resolved, false if timeout
     */
    @Deprecated
    public boolean waitForManualCaptchaResolution(int timeoutSeconds) {
        System.out.println("⚠️ CAPTCHA detected - waiting for manual resolution...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            // Wait until CAPTCHA disappears (any of these conditions)
            wait.until(d -> {
                return !isCaptchaPresent() ||
                        d.getCurrentUrl().contains("google.com/search") ||
                        !d.findElements(By.id("search")).isEmpty();
            });
            System.out.println("✅ CAPTCHA resolved - continuing test");
            return true;
        } catch (TimeoutException e) {
            System.out.println("❌ CAPTCHA resolution timeout after " + timeoutSeconds + " seconds");
            return false;
        }
    }

    /**
     * Add delay between requests to reduce CAPTCHA triggers
     * @param milliseconds delay duration
     */
    public static void addDelay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Configure ChromeOptions to reduce CAPTCHA detection
     * Add this to your ChromeOptionsBuilder
     */
    public static void configureChromeOptionsForCaptchaAvoidance(ChromeOptions options) {
        // Disable automation flags
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Add user agent to appear more human-like
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Set user agent
        options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
    }

    /**
     * Check and handle CAPTCHA if present (DEPRECATED - use handleCaptchaAutomatically)
     * @param maxWaitSeconds maximum time to wait for manual resolution
     * @return true if page is ready (no CAPTCHA or resolved), false otherwise
     */
    @Deprecated
    public boolean checkAndHandleCaptcha(int maxWaitSeconds) {
        if (isCaptchaPresent()) {
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║  🤖 CAPTCHA DETECTED - ATTEMPTING AUTOMATED BYPASS     ║");
            System.out.println("╚════════════════════════════════════════════════════════╝\n");

            return handleCaptchaAutomatically();
        }
        return true;
    }

    /**
     * Use search suggestions to avoid direct search (reduces CAPTCHA)
     */
    public boolean useSearchSuggestions(String searchTerm) {
        try {
            WebElement searchBox = driver.findElement(By.name("q"));

            // Type slowly with delays
            for (char c : searchTerm.toCharArray()) {
                searchBox.sendKeys(String.valueOf(c));
                addDelay(100 + (int)(Math.random() * 100)); // Random 100-200ms delay
            }

            // Wait for suggestions to appear
            addDelay(1000);

            // Try to click first suggestion instead of pressing Enter
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                WebElement firstSuggestion = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("ul[role='listbox'] li:first-child, .erkvQe li:first-child, .OBMEnb li:first-child")
                ));
                firstSuggestion.click();
                return true;
            } catch (Exception e) {
                // Fallback to Enter key
                searchBox.sendKeys(Keys.RETURN);
                return true;
            }

        } catch (Exception e) {
            System.out.println("Search suggestions approach failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Navigate using direct search URL (bypasses main page CAPTCHA)
     */
    public boolean navigateDirectToSearchResults(String searchTerm) {
        try {
            String encodedQuery = searchTerm.replace(" ", "+");
            String searchUrl = "https://www.google.com/search?q=" + encodedQuery;

            addDelay(BASE_DELAY_MS);
            driver.get(searchUrl);
            addDelay(BASE_DELAY_MS);

            // Check if successful
            if (!isCaptchaPresent() && driver.findElements(By.id("search")).size() > 0) {
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("Direct URL navigation failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Execute JavaScript to make automation less detectable
     */
    public void hideAutomationIndicators() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Remove webdriver property
            js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

            // Add chrome property (makes it look like a real Chrome browser)
            js.executeScript("window.chrome = { runtime: {} }");

            // Modify navigator properties
            js.executeScript("Object.defineProperty(navigator, 'plugins', {get: () => [1, 2, 3, 4, 5]})");
            js.executeScript("Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']})");

        } catch (Exception e) {
            System.out.println("⚠️ Could not hide automation indicators: " + e.getMessage());
        }
    }
}
