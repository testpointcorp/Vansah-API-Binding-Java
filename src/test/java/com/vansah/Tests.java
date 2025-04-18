package com.vansah;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Tests {
	
	private final VansahNode sendResults = new VansahNode();
	
	private final String vansahURL = "https://prodau.vansah.com";
	
	private final String testfolderPath = "vansah test automation/regression 2025/";
	
	private final String testCaseKey = "KAN-C17";
	
	private final String projectKey = "KAN";
	
	private final String testPlanKeyforATP = "KAN-P17";
	
	private final String testPlanAssetType = "folder"; //or issue only for ATP - Advanced Test Plan
	
	private final String testPlanKeyforSTP = "KAN-P18"; //required for Standard Test Plan
	
	
	@SuppressWarnings("static-access")
	@BeforeEach
	void setup() {
		
		sendResults.setVansahURL(vansahURL);

		sendResults.setVansahToken(System.getenv("CONNECT_DEMO_TOKEN"));
		
		sendResults.setProjectKey(projectKey);
		
		sendResults.setFOLDERPATH(testfolderPath);
		
		sendResults.setAdvancedTestPlanKey(testPlanKeyforATP);
		
		sendResults.setStandardTestPlanKey(testPlanKeyforSTP);
	
		
	}
	
	@Test
	void sendingResultstoVansah_usingTestFolderPath() throws Exception {
		
		sendResults.addTestRunFromTestFolder(testCaseKey);
		
		sendResults.addTestLog("passed", "Actual result for the Test Step", 1);
		
		
	}
	
	@Test
	void sendingResultstoVansahforATP() throws Exception {
		
		sendResults.addTestRunFromAdvancedTestPlan(testPlanAssetType,testCaseKey);
		
		sendResults.addTestLog("passed", "Actual result for the Test Step", 1);
		
		
	}
	
	@Test
	void sendingResultstoVansahforSTP() throws Exception {
		
		sendResults.addTestRunFromStandardTestPlan(testCaseKey);
		
		sendResults.addTestLog("passed", "Actual result for the Test Step", 1);
		
		
	}

}
