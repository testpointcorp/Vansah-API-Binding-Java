package testpoint;

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

public class Vansah {

	//***************************************** ENDPOINTS *************************************************
	private static final String API_VERSION = "v2";
	private static final String VANSAH_URL = "https://api.vansah.com";
	
	private static final String ADD_TEST_RUN = VANSAH_URL + "/api/" + API_VERSION + "/test_case/add_test_run";
	private static final String ADD_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION + "/test_case/add_test_log";
	private static final String UPDATE_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION + "/test_case/update_test_log";
	private static final String REMOVE_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION + "/test_case/remove_test_log";
	private static final String ADD_QUICK_TEST = VANSAH_URL + "/api/" + API_VERSION + "/auto/test_case/add_quick_test";
	private static final String REMOVE_TEST_RUN = VANSAH_URL + "/api/" + API_VERSION + "/test_case/remove_test_run";
	private static final String TEST_SCRIPT = VANSAH_URL + "/api/" + API_VERSION + "/test_case/test_script";
	//******************************************************************************************************

	
	//*********************************** VARIABLES ****************************************************************************
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
	private String USER_TOKEN;
	private String PROJECT_IDENTIFIER;
	private String TEST_RUN_IDENTIFIER;
	private String TEST_LOG_IDENTIFIER;
	private int VANSAH_DATA_ROW_NUM;
	private String VANSAH_DATA_COLUMN_NAME;
	private String FILE;
	private File image;
	private String HTTPS_RESULT;
	private ReadConfigVansah configReader;
	private VansahLogHandler vlh;
	private HttpClientBuilder clientBuilder;
	private CredentialsProvider credsProvider;
	private HashMap<Integer, String> testSteps = new HashMap<Integer, String>();
	private HashMap<Integer, String> testResults = new HashMap<Integer, String>();
	private List<Integer> listOfSteps;
	private int testRows;
	//************************************************************************************************************************************************
	
	
	public Vansah() {

		vlh = new VansahLogHandler();
		configReader = new ReadConfigVansah();
		USER_TOKEN = this.configReader.getUserToken();
		PROJECT_IDENTIFIER = this.configReader.getProjectIdentifier();
	}
	
	
	//************************** VANSAH ADD TEST RUN (TEST RUN IDENTIFIER CREATION) *************************************************
	public void addTestRun(String testcase, String release, String environment, String jiraIssue, String cycle, String build, String jiraReleaseIdentifier ) throws Exception {
		
		
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
	//*****************************************************************************************************************
	
	
	
	//************************** VANSAH ADD TEST LOG (LOG IDENTIFIER CREATION *************************************************
	public void addTestLog(int result, String comment, Integer testStepRow, Integer testStepIdentifier, String jiraIssues, String jiraHost, boolean sendScreenShot, WebDriver driver) throws Exception {
		
		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.STEP_ORDER = testStepRow;
		this.STEP_IDENTIFIER = testStepIdentifier;
		this.SEND_SCREENSHOT = sendScreenShot;
		this.JIRA_ISSUES = jiraIssues;
		this.JIRA_HOST = jiraHost;
		connectToVansahRest("addTestLog", driver);
	}
	//*****************************************************************************************************************
	
	
	
	
	//****************************************** VANSAH ADD QUICK TEST *********************************************
	public void addQuickTest(String testcase, int result, String release, String environment, String comment, String jiraIssue, String cycle, String jiraReleaseIdentifier, String build,  boolean sendScreenShot, WebDriver driver) throws Exception {
		
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
	//*******************************************************************************************************************
	
	
	//****************************************** VANSAH REMOVE TEST RUN *********************************************
	public void removeTestRun() throws Exception {
		connectToVansahRest("removeTestRun", null);
	}
	//*******************************************************************************************************************

	//****************************************** VANSAH REMOVE TEST LOG *********************************************
	public void removeTestLog() throws Exception {	
		connectToVansahRest("removeTestLog", null);
	}
	//*******************************************************************************************************************
	
	//****************************************** VANSAH UPDATE TEST LOG *********************************************
	public void updateTestLog(int result, String comment, String jiraIssues, String jiraHost, boolean sendScreenShot, WebDriver driver) throws Exception {
		
		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.SEND_SCREENSHOT = sendScreenShot;
		this.JIRA_ISSUES = jiraIssues;
		this.JIRA_HOST = jiraHost;
		connectToVansahRest("updateTestLog", driver);
	}
	//*******************************************************************************************************************
	
	
	public void testScript(String case_key) {
		try {
			
			clientBuilder = HttpClientBuilder.create();
			// Detecting if the system using any proxy setting.
			String hostAddr = configReader.getsHostAddr();
			String portNo = configReader.getsPortNo();
			if (hostAddr.equals("www.host.com") && portNo.equals("0")) {
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
			get = Unirest.get(TEST_SCRIPT).header("user-token",USER_TOKEN).queryString("case_key", case_key).queryString("project_identifier", PROJECT_IDENTIFIER).asJson();
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
					//JSONObject jsonObject = jsonobj.getJSONArray("array").getJSONObject(0);
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
		
		
		if (configReader.getsUpdateVansah().equals("0")) {
			vlh.writeToV_LogFile(CASE_KEY, STEP_ORDER, RESULT_KEY, COMMENT, RELEASE_KEY, BUILD_KEY, ENVIRONMENT_KEY, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
			HTTPS_RESULT = "#STATUS_OFF";
			System.out.println(HTTPS_RESULT);
		} else {

			try {
				clientBuilder = HttpClientBuilder.create();
				// Detecting if binder is being used behind any proxy setting.
				String hostAddr = configReader.getsHostAddr();
				String portNo = configReader.getsPortNo();
				if (hostAddr.equals("www.host.com") && portNo.equals("0")) {
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
					jsonResponse = Unirest.post(ADD_TEST_RUN).header("user-token",USER_TOKEN).field("project_identifier", PROJECT_IDENTIFIER)
					.field("case_key", CASE_KEY).field("release_key", RELEASE_KEY).field("environment_key", ENVIRONMENT_KEY)
					.field("issue_key", JIRA_ISSUE_KEY).field("cycle_key", CYCLE_KEY).field("build_key", BUILD_KEY)
					.field("jira_release_identifier", JIRA_RELEASE_IDENTIFIER).asJson();
				}
				
				
				if(type == "addTestLog") {
					jsonResponse = Unirest.post(ADD_TEST_LOG).header("user-token",USER_TOKEN).field("test_run_identifier", TEST_RUN_IDENTIFIER)
					.field("result_key", RESULT_KEY).field("comment", COMMENT).field("step_order", STEP_ORDER).field("step_identifier", STEP_IDENTIFIER)
					.field("file", FILE).field("jira_issues", JIRA_ISSUES).field("jira_host", JIRA_HOST).asJson();
				}
				

				if(type == "addQuickTest") {
					jsonResponse = Unirest.post(ADD_QUICK_TEST).header("user-token",USER_TOKEN).field("project_identifier", PROJECT_IDENTIFIER)
					.field("case_key", CASE_KEY).field("result_key", RESULT_KEY).field("release_key", RELEASE_KEY).field("environment_key", ENVIRONMENT_KEY)
					.field("comment", COMMENT).field("issue_key", JIRA_ISSUE_KEY).field("cycle_key", CYCLE_KEY).field("file", FILE)
					.field("jira_release_identifier", JIRA_RELEASE_IDENTIFIER).field("build_key", BUILD_KEY).asJson();
				}
				
				
				if(type == "removeTestRun") {
					jsonResponse = Unirest.post(REMOVE_TEST_RUN).header("user-token",USER_TOKEN).field("test_run_identifier", TEST_RUN_IDENTIFIER).asJson();
				}
				
				
				if(type == "removeTestLog") {
					jsonResponse = Unirest.post(REMOVE_TEST_LOG).header("user-token",USER_TOKEN).field("log_identifier", TEST_LOG_IDENTIFIER).asJson();
				}
				
				
				if(type == "updateTestLog") {
					jsonResponse = Unirest.post(UPDATE_TEST_LOG).header("user-token",USER_TOKEN).field("log_identifier", TEST_LOG_IDENTIFIER)
					.field("result_key", RESULT_KEY).field("comment", COMMENT).field("file", FILE).field("jira_issues", JIRA_ISSUES)
					.field("jira_host", JIRA_HOST).asJson();
				}
				
				
				JSONObject fullBody = jsonResponse.getBody().getObject();
				if (jsonResponse.getBody().toString().equals("[]")) {
					System.out.println("Unexpected Response From Vansah with empty response: " + jsonResponse.getBody().toString());
					vlh.writeErrorToV_errorFile(CASE_KEY, STEP_ORDER, RESULT_KEY, COMMENT + ", Response from Vansah: " + fullBody, RELEASE_KEY, BUILD_KEY, ENVIRONMENT_KEY, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
				
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
						vlh.writeErrorToV_errorFile(CASE_KEY, STEP_ORDER, RESULT_KEY,COMMENT + ", Response from Vansah: " + vansah_message, RELEASE_KEY,BUILD_KEY, ENVIRONMENT_KEY, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);					
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				vlh.writeErrorToV_errorFile(CASE_KEY, STEP_ORDER, RESULT_KEY, ex.getMessage(), RELEASE_KEY,BUILD_KEY, ENVIRONMENT_KEY, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
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
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = Base64.getEncoder().encodeToString(bytes);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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