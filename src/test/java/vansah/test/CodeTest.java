package vansah.test;

import org.openqa.selenium.WebDriver;

import vansah.node.vansahnode;

public class CodeTest {

	public static void main(String[] args) throws Exception {
		WebDriver driver = null;
		
		//Provide TestFolder ID , JIRA Issue, Sprint Key, Sprint Release and Environment
		vansahnode vn = new vansahnode("1ba1372f-54ed-11ed-8e52-5658ef8eadd5","VAN-1","VAN Sprint 1","v1","SYS");
		
		
		//From Jira Issue Screen
		vn.addTestRunFromJIRAIssue("VAN-C1");
		for(int i = 1;i<=vn.testStepCount("VAN-C1");i++) {
			
			//Add logs for each step
			vn.add_test_log(2, "This is From Java Binder Add test log", i, false, driver);
			
			//Will update the current test log
			vn.update_test_log(1, "This is From Java Binder Update Test log", false, driver);
			
			//To remove the current test log
			vn.remove_test_log();
			
		}
		
		vn.remove_test_run();
	
		//From Test folder screen
		vn.addTestRunFromTestFolder("VAN-C1");
		for(int i = 1;i<=vn.testStepCount("VAN-C1");i++) {
			
			//Add logs for each step
			vn.add_test_log(2, "This is From Java Binder Add test log", i, false, driver);
			
			//Will update the current test log
			vn.update_test_log(1, "This is From Java Binder Update Test log", false, driver);
			
			//To remove the current test log
			//vn.remove_test_log();
			
		}
		
		//Add Quick test for Jira issue
		
		vn.addQuickTestFromJiraISSUE("VAN-C1", 0, "This is from Quick Test Jira issue", false, driver);
		
		//Add Quick test for Test folders
		vn.addQuickTestFromTestFolders("VAN-C1", 0, "This is from Quick Test Test Folders", false, driver);
	}


}

