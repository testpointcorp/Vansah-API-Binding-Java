package com.testpoint.binding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class vansahjira {

	//--------------------------- ENDPOINTS -------------------------------------------------------------------------------
	private static final String API_VERSION = "v2";
	private static final String VANSAH_URL = "https://id.vansahapi.app";
	private static final String ADD_TEST_RUN = VANSAH_URL + "/api/" + API_VERSION     + "/auto/test_case/add_test_run";
	private static final String ADD_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION     + "/auto/test_case/add_test_log";
	private static final String UPDATE_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION  + "/auto/test_case/update_test_log";
	private static final String REMOVE_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION  + "/auto/test_case/remove_test_log";
	private static final String ADD_QUICK_TEST = VANSAH_URL + "/api/" + API_VERSION   + "/auto/test_case/add_quick_test";
	private static final String REMOVE_TEST_RUN = VANSAH_URL + "/api/" + API_VERSION  + "/auto/test_case/remove_test_run";
	private static final String TEST_SCRIPT = VANSAH_URL + "/api/" + API_VERSION      + "/auto/test_case/test_script";
	//--------------------------------------------------------------------------------------------------------------------
	
	
	//--------------------------- INFORM YOUR UNIQUE VANSAH TOKEN HERE ---------------------------------------------------
	private static final String VANSAH_TOKEN = "";
	//--------------------------------------------------------------------------------------------------------------------
	
	
	//--------------------------- IF YOU ARE USING VANSAH BINDING BEHIND A PROXY, INFORM THE DETAILS HERE ----------------
	private static final String hostAddr = "";
	private static final String portNo = "";
	//--------------------------------------------------------------------------------------------------------------------	
		
	
	//--------------------------- INFORM IF YOU WANT TO UPDATE VANSAH HERE -----------------------------------------------
	// 0 = NO RESULTS WILL BE SENT TO VANSAH
	// 1 = RESULTS WILL BE SENT TO VANSAH
	private static final String updateVansah = "1";
	//--------------------------------------------------------------------------------------------------------------------	
	
	
	//--------------------------------------------------------------------------------------------------------------------
	private String CYCLE_KEY;
	private String CASE_KEY;
	private String RELEASE_KEY;
	private String BUILD_KEY;
	private String ENVIRONMENT_KEY;
	private String JIRA_ISSUE_KEY;
	private String JIRA_ISSUES;
	private String JIRA_HOST;
	private String JIRA_RELEASE_IDENTIFIER;
	private int RESULT_KEY;
	private boolean SEND_SCREENSHOT;
	private String COMMENT;
	private Integer STEP_ORDER;
	private Integer STEP_IDENTIFIER;
	private String TEST_RUN_IDENTIFIER;
	private String TEST_LOG_IDENTIFIER;
	private String FILE;
	private File image;
	private HttpClientBuilder clientBuilder;
	private CredentialsProvider credsProvider;
	private HashMap<Integer, String> testSteps = new HashMap<Integer, String>();
	private HashMap<Integer, String> testResults = new HashMap<Integer, String>();
	private List<Integer> listOfSteps;
	private int testRows;
	
	
	//------------------------ VANSAH ADD TEST RUN (TEST RUN IDENTIFIER CREATION) -------------------------------------------
	//POST add_test_run https://apidoc.vansah.com/#05aefccb-59bc-4dfb-a1b0-a85751180228
	//creates a new test run Identifier which is then used with the other testing methods: 1) add_test_log 2) remove_test_run
	
	public void add_test_run(String testcase, String release, String environment, String jiraIssue, String cycle, String build, String jiraReleaseIdentifier ) throws Exception {
		
		this.CASE_KEY = testcase;
		this.RELEASE_KEY = release;
		this.ENVIRONMENT_KEY = environment;
		this.JIRA_ISSUE_KEY = jiraIssue;
		this.CYCLE_KEY = String.valueOf(cycle);	
		this.BUILD_KEY = build;
		this.JIRA_RELEASE_IDENTIFIER = jiraReleaseIdentifier;
		this.SEND_SCREENSHOT = false;

		connectToVansahRest("addTestRun", null);
	}
	//------------------------------------------------------------------------------------------------------------------------
	
	
	
	//-------------------------- VANSAH ADD TEST LOG (LOG IDENTIFIER CREATION ------------------------------------------------
	//POST add_test_log https://apidoc.vansah.com/#a0788f90-e751-40b7-a5e3-5879df4cff2c
	//adds a new test log for the test case_key. Requires "test_run_identifier" from add_test_run
	
	public void add_test_log(int result, String comment, Integer testStepRow, Integer testStepIdentifier, String jiraIssues, String jiraHost, boolean sendScreenShot, WebDriver driver) throws Exception {
		
		//0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.STEP_ORDER = testStepRow;
		this.STEP_IDENTIFIER = testStepIdentifier;
		this.SEND_SCREENSHOT = sendScreenShot;
		this.JIRA_ISSUES = jiraIssues;
		this.JIRA_HOST = jiraHost;
		connectToVansahRest("addTestLog", driver);
	}
	//-------------------------------------------------------------------------------------------------------------------------
	

	
	//------------------------- VANSAH ADD QUICK TEST --------------------------------------------------------------------------
	//POST add_quick_test https://apidoc.vansah.com/#f9282559-7dc4-407a-a85e-c7e836b7281d
	//creates a new test run and a new test log for the test case_key. By calling this endpoint, 
	//you will create a new log entry in Vansah with the respective overal Result. 
	//(0 = N/A, 1= FAIL, 2= PASS, 3 = Not Tested). Add_Quick_Test is useful for test cases in which there are no steps in the test script, 
	//where only the overall result is important.
	
	public void add_quick_test(String testcase, int result, String release, String environment, String comment, String jiraIssue, String cycle, String jiraReleaseIdentifier, String build,  boolean sendScreenShot, WebDriver driver) throws Exception {
		
		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.CASE_KEY = testcase;
		this.RESULT_KEY = result;
		this.RELEASE_KEY = release;
		this.ENVIRONMENT_KEY = environment;
		this.COMMENT = comment;
		this.JIRA_ISSUE_KEY = jiraIssue;
		this.CYCLE_KEY = String.valueOf(cycle);	
		this.JIRA_RELEASE_IDENTIFIER = jiraReleaseIdentifier;
		this.BUILD_KEY = build;
		this.SEND_SCREENSHOT = sendScreenShot;
		connectToVansahRest("addQuickTest", driver);
	}
	//------------------------------------------------------------------------------------------------------------------------------
	
	
	//------------------------------------------ VANSAH REMOVE TEST RUN *********************************************
	//POST remove_test_run https://apidoc.vansah.com/#db054159-ee7b-4076-a6d5-4ee4797d86a1
	//will delete the test log created from add_test_run or add_quick_test
	
	public void remove_test_run() throws Exception {
		connectToVansahRest("removeTestRun", null);
	}
	//------------------------------------------------------------------------------------------------------------------------------

	//------------------------------------------ VANSAH REMOVE TEST LOG *********************************************
	//POST remove_test_log https://apidoc.vansah.com/#789414f9-43e7-4744-b2ca-1aaf9ee878e5
	//will delete a test_log_identifier created from add_test_log or add_quick_test
	
	public void remove_test_log() throws Exception {	
		connectToVansahRest("removeTestLog", null);
	}
	//------------------------------------------------------------------------------------------------------------------------------
	
	
	//------------------------------------------ VANSAH UPDATE TEST LOG ------------------------------------------------------------
	//POST update_test_log https://apidoc.vansah.com/#ae26f43a-b918-4ec9-8422-20553f880b48
	//will perform any updates required using the test log identifier which is returned from add_test_log or add_quick_test
	
	public void update_test_log(int result, String comment, String jiraIssues, String jiraHost, boolean sendScreenShot, WebDriver driver) throws Exception {
		
		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.SEND_SCREENSHOT = sendScreenShot;
		this.JIRA_ISSUES = jiraIssues;
		this.JIRA_HOST = jiraHost;
		connectToVansahRest("updateTestLog", driver);
	}
	//----------------------------------------------VANSAH GET TEST SCRIPT-----------------------------------------------------------
	//GET test_script https://apidoc.vansah.com/#91fe16a8-b2c4-440a-b5e6-96cb15f8e1a3
	//Returns the test script for a given case_key
	
	public void test_script(String case_key) {
		try {
			
			clientBuilder = HttpClientBuilder.create();
			// Detecting if the system using any proxy setting.
		
			if (hostAddr.equals("") && portNo.equals("")) {
				System.out.println("No proxy");
				Unirest.setHttpClient(clientBuilder.build());
			} else {
				System.out.println("Proxy Server");
				credsProvider = new BasicCredentialsProvider();
				clientBuilder.useSystemProperties();
				clientBuilder.setProxy(new HttpHost(hostAddr, Integer.parseInt(portNo)));
				clientBuilder.setDefaultCredentialsProvider(credsProvider);
				clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
				Unirest.setHttpClient(clientBuilder.build());
			}
			HttpResponse<JsonNode> get;
			get = Unirest.get(TEST_SCRIPT).header("vansah-token",VANSAH_TOKEN).queryString("case_key", case_key).asJson();
			if (get.getBody().toString().equals("[]")) {
				System.out.println("Unexpected Response From Server: " + get.getBody().toString());
			} else {
				JSONObject jsonobjInit = new JSONObject(get.getBody().toString());
				boolean success = jsonobjInit.getBoolean("success");
				String vansah_message = jsonobjInit.getString("message");
			
				if (success) {
					testSteps = new LinkedHashMap<Integer, String>();
					testResults = new HashMap<Integer, String>();
					listOfSteps = new ArrayList<Integer>();
					JSONObject jsonobj = new JSONObject(get.getBody().toString());
					int testRows = jsonobjInit.getJSONObject("pagination").getInt("page_total");
					System.out.println("NUMBER OF STEPS: " + testRows);
					
					JSONArray records = jsonobj.getJSONArray("data");
					for (int i = 0; i < testRows; i++) {
						JSONObject record = records.getJSONObject(i);
						listOfSteps.add(record.getInt("sort_order"));
						testSteps.put(record.getInt("sort_order"), formatString(record, "plain_step"));
						testResults.put(record.getInt("sort_order"), formatString(record, "plain_expected_result"));	
					}
	
				} else {
					System.out.println("Error - Response From Vansah: " + vansah_message);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//******************** MAIN METHOD THAT CONNECTS TO VANSAH (CENTRAL PLACE FOR QUICK TEST AND QUICK TEST UPDATE) ******************************************
	private void connectToVansahRest(String type, WebDriver driver) {
		
		HttpResponse<JsonNode> jsonResponse = null;
		
		if (updateVansah.equals("1")) {

			try {
				clientBuilder = HttpClientBuilder.create();
				// Detecting if binder is being used behind any proxy setting.
				if (hostAddr.equals("") && portNo.equals("")) {
					Unirest.setHttpClient(clientBuilder.build());
				} else {
					System.out.println("Proxy Server");credsProvider = new BasicCredentialsProvider();
					clientBuilder.useSystemProperties();clientBuilder.setProxy(new HttpHost(hostAddr, Integer.parseInt(portNo)));
					clientBuilder.setDefaultCredentialsProvider(credsProvider); clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
					Unirest.setHttpClient(clientBuilder.build());
				}
				
				if (SEND_SCREENSHOT) {
					try {
						System.out.print("Taking screenshot...");
						WebDriver augmentedDriver = new Augmenter().augment(driver);
						image = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
						String encodstring = encodeFileToBase64Binary(image);
						FILE = "data:image/png;base64," + encodstring;
						System.out.println("Screenshot succesfully taken.");
					} catch (Exception e) {
						System.out.println("Taking Screenshot failed: " + e.toString());
					}
				}

				
				if(type == "addTestRun") {
					jsonResponse = Unirest.post(ADD_TEST_RUN).header("vansah-token",VANSAH_TOKEN).field("case_key", CASE_KEY).field("release_key", RELEASE_KEY).field("environment_key", ENVIRONMENT_KEY)
					.field("issue_key", JIRA_ISSUE_KEY).field("cycle_key", CYCLE_KEY).field("build_key", BUILD_KEY)
					.field("jira_release_identifier", JIRA_RELEASE_IDENTIFIER).asJson();
				}
				
				
				if(type == "addTestLog") {
					jsonResponse = Unirest.post(ADD_TEST_LOG).header("vansah-token",VANSAH_TOKEN).field("test_run_identifier", TEST_RUN_IDENTIFIER)
					.field("result_key", RESULT_KEY).field("comment", COMMENT).field("step_order", STEP_ORDER).field("step_identifier", STEP_IDENTIFIER)
					.field("file", FILE).field("jira_issues", JIRA_ISSUES).field("jira_host", JIRA_HOST).asJson();
				}
				

				if(type == "addQuickTest") {
					jsonResponse = Unirest.post(ADD_QUICK_TEST).header("vansah-token",VANSAH_TOKEN).field("case_key", CASE_KEY).field("result_key", RESULT_KEY).field("release_key", RELEASE_KEY).field("environment_key", ENVIRONMENT_KEY)
					.field("comment", COMMENT).field("issue_key", JIRA_ISSUE_KEY).field("cycle_key", CYCLE_KEY).field("file", FILE)
					.field("jira_release_identifier", JIRA_RELEASE_IDENTIFIER).field("build_key", BUILD_KEY).asJson();
				}
				
				
				if(type == "removeTestRun") {
					jsonResponse = Unirest.post(REMOVE_TEST_RUN).header("vansah-token",VANSAH_TOKEN).field("test_run_identifier", TEST_RUN_IDENTIFIER).asJson();
				}
				
				
				if(type == "removeTestLog") {
					jsonResponse = Unirest.post(REMOVE_TEST_LOG).header("vansah-token",VANSAH_TOKEN).field("log_identifier", TEST_LOG_IDENTIFIER).asJson();
				}
				
				
				if(type == "updateTestLog") {
					jsonResponse = Unirest.post(UPDATE_TEST_LOG).header("vansah-token",VANSAH_TOKEN).field("log_identifier", TEST_LOG_IDENTIFIER)
					.field("result_key", RESULT_KEY).field("comment", COMMENT).field("file", FILE).field("jira_issues", JIRA_ISSUES)
					.field("jira_host", JIRA_HOST).asJson();
				}
				
				
				JSONObject fullBody = jsonResponse.getBody().getObject();
				if (jsonResponse.getBody().toString().equals("[]")) {
					System.out.println("Unexpected Response From Vansah with empty response: " + jsonResponse.getBody().toString());
					
				
				} else {
					JSONObject jsonobjInit = new JSONObject(jsonResponse.getBody().toString());
					boolean success = jsonobjInit.getBoolean("success");
					String vansah_message = jsonobjInit.getString("message");
					System.out.println("(" + StringUtils.capitalize(type) + ") Return: " + success + ". Vansah Message: " + vansah_message );
					
					if (success){
				
				    	if(type == "addTestRun") {
					    	TEST_RUN_IDENTIFIER = fullBody.getJSONObject("data").get("test_run_identifier").toString();
					    	System.out.println("Test Run Identifier: " + TEST_RUN_IDENTIFIER);
				    	}
				    	
				    	if(type == "addTestLog") {
					    	TEST_LOG_IDENTIFIER = fullBody.getJSONObject("data").get("log_identifier").toString();
					    	System.out.println("Test Log Identifier: " + TEST_LOG_IDENTIFIER);
				    	}
						
					}else{
						System.out.println("Response From Vansah: " + vansah_message);					
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	//*******************************************************************************************************************
	
	public int getNumberOfTestRows() {
		return testRows;
	}
	
	public HashMap<Integer, String> getTestSteps() {
		return testSteps;
	}


	private static String encodeFileToBase64Binary(File file) {
		String encodedfile = null;
		try {
			@SuppressWarnings("resource")
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = Base64.getEncoder().encodeToString(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return encodedfile;
	}
	
	private String formatString(JSONObject record, String key) {
		if (record.isNull(key))
			return "";
		return record.getString(key);
	}
}