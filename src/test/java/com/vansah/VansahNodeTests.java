package com.vansah;

public class VansahNodeTests {
	
	//Optional if IssueKey is provided
	private static String testFolderID = "1ba1372f-54ed-11ed-8e52-5658ef8eadd5"; //TestFolder ID to which test Execution is to be perform
	
	//Optional if TestFolder ID is provided
	private static String issueKey = "VAN-1"; //IssueKey to which test Execution is to be perform
	
	//Optional 
	private static String sprintName = "VAN Sprint 1"; //Sprint Name for current sprint for which test execution is to be perform
	
	//Optional
	private static String releaseName = "v1"; //Release Name linked with the current sprint and to the test case.
	
	//Optional
	private static String environment = "SYS"; //Environment Name to which test execution of a test case is to be perform
	
	//Required
	private static String testCase = "VAN-C3";

	public static void main(String[] args) throws Exception {
		
		
		//Provide TestFolder ID , JIRA Issue, Sprint Key, Sprint Release and Environment
		VansahNode testExecute = new VansahNode(testFolderID,issueKey);
				
		//From Jira Issue Screen
		testExecute.addTestRunFromJIRAIssue(testCase);
		
		for(int i = 1;i<=testExecute.testStepCount(testCase);i++) {
			
			//Add logs for each step function(ResultID, AcutalResultComment, TestStepID, screenshotTrueorFalse, screenshot file);
			testExecute.addTestLog(2, "This is From Java Binder Add test log", i, false, null);
			
			//Will update the current test log
			testExecute.updateTestLog(1, "This is From Java Binder Update Test log", false, null);
			
			//To remove the current test log
			testExecute.removeTestLog();
			
		}
		
		testExecute.removeTestRun(); //Removing current test run
	
		//From Test folder screen
		testExecute.addTestRunFromTestFolder(testCase);
		
		for(int i = 1;i<=testExecute.testStepCount(testCase);i++) {
			
			//Add logs for each step    function(ResultID, AcutalResultComment, TestStepID, screenshotTrueorFalse, screenshot file);
			
			testExecute.addTestLog(2, "This is From Java Binder Add test log", i, false, null);
			
			//Will update the current test log
			testExecute.updateTestLog(1, "This is From Java Binder Update Test log", false, null);
			
			//To remove the current test log
			//testExecute.remove_test_log();
			
		}
		
		//Add Quick test for Jira issue  function(testCaseKey, ResultID, AcutalResultComment, screenshotTrueorFalse, screenshot file);
		
		testExecute.addQuickTestFromJiraIssue(testCase, 0);
		
		//Add Quick test for Test folders
		testExecute.addQuickTestFromTestFolders(testCase, 0);
	}


}

