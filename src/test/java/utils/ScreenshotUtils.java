package utils;

import io.cucumber.java.Scenario;
import com.microsoft.playwright.Page;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for attaching screenshots to Cucumber scenarios for Masterthought reports
 * Enhanced to save files and create both embedded previews and hyperlinks
 */
public class ScreenshotUtils {
    
    private static final String SCREENSHOTS_DIR = "target/cucumber-html-reports/screenshots";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");
    
    /**
     * Takes a screenshot and attaches it to the Cucumber scenario with both preview and file link
     * This will make the screenshot appear in Masterthought HTML reports with better visibility
     */
    public static void attachScreenshotToScenario(Scenario scenario, Page page, String screenshotName) {
        try {
            if (page != null && scenario != null) {
                // Take screenshot as byte array
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                
                // Create timestamp for unique filename
                String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
                String safeScreenshotName = screenshotName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
                String filename = safeScreenshotName + "_" + timestamp + ".png";
                
                // Save screenshot as file for hyperlink
                Path screenshotDir = Paths.get(SCREENSHOTS_DIR);
                Files.createDirectories(screenshotDir);
                Path screenshotPath = screenshotDir.resolve(filename);
                Files.write(screenshotPath, screenshot);
                
                // Attach embedded screenshot for inline preview
                scenario.attach(screenshot, "image/png", "📸 " + screenshotName);
                
                // Create enhanced HTML with thumbnail and link
                String relativePath = "screenshots/" + filename;
                String enhancedHtml = String.format(
                    "<div style='border: 1px solid #ddd; border-radius: 8px; margin: 10px 0; background: #f9f9f9; overflow: hidden;'>" +
                    "<div style='padding: 10px; text-align: center;'>" +
                    "<p style='margin: 0 0 10px 0; font-weight: bold; color: #333;'>📸 %s</p>" +
                    "<a href='%s' target='_blank' style='display: inline-block; margin: 5px; padding: 10px 16px; " +
                    "background: linear-gradient(135deg, #007bff, #0056b3); color: white; text-decoration: none; " +
                    "border-radius: 6px; font-size: 13px; font-weight: 500; box-shadow: 0 2px 4px rgba(0,123,255,0.3); " +
                    "transition: all 0.2s ease;' onmouseover='this.style.transform=\"translateY(-1px)\"; " +
                    "this.style.boxShadow=\"0 4px 8px rgba(0,123,255,0.4)\"' onmouseout='this.style.transform=\"translateY(0)\"; " +
                    "this.style.boxShadow=\"0 2px 4px rgba(0,123,255,0.3)\"'>" +
                    "� View Full Size Screenshot</a>" +
                    "<br><small style='color: #666; font-size: 11px;'>Click to open in new tab</small>" +
                    "</div></div>",
                    screenshotName, relativePath
                );
                scenario.attach(enhancedHtml.getBytes(), "text/html", "🔗 " + screenshotName + " - Link");
                
                System.out.println("📸 Screenshot '" + screenshotName + "' attached with preview and link");
                System.out.println("📁 File saved: " + screenshotPath.toString());
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to attach screenshot to scenario: " + e.getMessage());
        }
    }
    
    /**
     * Takes a screenshot and attaches it to the scenario with a timestamp
     */
    public static void attachTimestampedScreenshotToScenario(Scenario scenario, Page page, String baseName) {
        String timestampedName = baseName + "_" + System.currentTimeMillis();
        attachScreenshotToScenario(scenario, page, timestampedName);
    }
}