package utils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.sorting.SortingMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for generating MasterThought Cucumber Reports
 * Creates comprehensive HTML reports from Cucumber JSON output
 */
public class ReportGenerator {
    
    private static final String PROJECT_NAME = "Selenium Cucumber BDD Framework";
    private static final String BUILD_NUMBER = "1.0";
    
    /**
     * Generate MasterThought HTML report from Cucumber JSON
     * 
     * @param jsonReportPath path to cucumber.json file
     * @param outputDirectory directory where HTML report will be generated
     */
    public static void generateReport(String jsonReportPath, String outputDirectory) {
        try {
            File reportOutputDirectory = new File(outputDirectory);
            
            List<String> jsonFiles = new ArrayList<>();
            jsonFiles.add(jsonReportPath);
            
            // Create configuration
            Configuration configuration = new Configuration(reportOutputDirectory, PROJECT_NAME);
            
            // Optional: Add project metadata
            configuration.setBuildNumber(BUILD_NUMBER);
            configuration.addClassifications("Platform", System.getProperty("os.name"));
            configuration.addClassifications("Browser", System.getProperty("browser", "chrome"));
            configuration.addClassifications("Java Version", System.getProperty("java.version"));
            configuration.addClassifications("Environment", System.getProperty("env", "QA"));
            
            // Optional: Configure report presentation
            configuration.setSortingMethod(SortingMethod.NATURAL);
            configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);
            configuration.setTrendsStatsFile(new File(outputDirectory + "/trends.json"));
            
            // Generate the report
            ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
            Reportable result = reportBuilder.generateReports();
            
            System.out.println("===============================================");
            System.out.println("📊 MasterThought Report Generated Successfully!");
            System.out.println("===============================================");
            System.out.println("📁 Report Location: " + new File(outputDirectory + "/cucumber-html-reports/overview-features.html").getAbsolutePath());
            System.out.println("📈 Total Features: " + result.getFeatures());
            System.out.println("✅ Passed Scenarios: " + result.getPassedScenarios());
            System.out.println("❌ Failed Scenarios: " + result.getFailedScenarios());
            System.out.println("⏭️  Skipped Scenarios: " + (result.getScenarios() - result.getPassedScenarios() - result.getFailedScenarios()));
            System.out.println("===============================================");
            
        } catch (Exception e) {
            System.err.println("Error generating MasterThought report: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Generate report with default paths
     * JSON: target/cucumber-reports/cucumber.json
     * Output: target/cucumber-html-reports
     */
    public static void generateReport() {
        String jsonReportPath = "target/cucumber-reports/cucumber.json";
        String outputDirectory = "target";
        generateReport(jsonReportPath, outputDirectory);
    }
}
