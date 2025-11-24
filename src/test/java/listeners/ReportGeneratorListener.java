package listeners;


import utils.ReportGenerator;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

/**
 * Test Execution Listener for generating reports after all tests complete
 * Implements JUnit Platform TestExecutionListener to hook into test lifecycle
 */
public class ReportGeneratorListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        System.out.println("\n🔄 All tests completed. Generating MasterThought Report...\n");

        // Generate MasterThought HTML report
        ReportGenerator.generateReport();
    }
}

