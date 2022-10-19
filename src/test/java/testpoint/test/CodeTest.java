package testpoint.test;
import org.openqa.selenium.WebDriver;

import testpoint.node.vansahnode;
import testpoint.php.vansahjira;

public class CodeTest {

	public static void main(String[] args) throws Exception {
		WebDriver driver = null ;
		vansahnode vj = new vansahnode("","NART-1","NART Sprint 1","v1","UAT");
		vj.addTestRunFromJIRAIssue("NART-C599");
		
		
		//void vansahjira.add_test_log(int result, String comment, Integer testStepRow, String jiraIssues, boolean sendScreenShot, WebDriver driver) throws Exception
		//0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested
		
//		int stepCount = vj.test_script("VAN-C1");
//		
//		for(int i=1;i<=stepCount;i++) {
//			
//			vj.add_test_log(2, "This is Expected", i, "VAN-1", false, driver);
//			vj.update_test_log(1, "This is Updated", false, driver);
//			
//		}
//		
		
		//vj.add_quick_test("VAN-C1", 2, "v1", "UAT", "This is from quick test", "VAN-1", false, driver);


	}

}
