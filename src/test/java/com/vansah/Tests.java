package com.vansah;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Tests {
	
	private final VansahNode sendResults = new VansahNode();
	
	private final String vansahURL = "https://prodau.vansah.com";
	
	private final String testfolderPath = "vansah test automation/regression 2025/";
	
	private final String testCaseKey = "KAN-C17";
	
	private final String projectKey = "KAN";
	
	
	@SuppressWarnings("static-access")
	@BeforeEach
	void setup() {
		
		sendResults.setVansahURL(vansahURL);

		sendResults.setVansahToken(System.getenv("CONNECT_DEMO_TOKEN"));
		
		sendResults.setProjectKey(projectKey);
		
		sendResults.setFOLDERPATH(testfolderPath);
	
		
	}
	
	@Test
	void sendingResultstoVansah_usingTestFolderPath() throws Exception {
		
		sendResults.addTestRunFromTestFolder(testCaseKey);
		
		sendResults.addTestLog("passed", "Actual result for the Test Step", 1);
		
		
	}

}
