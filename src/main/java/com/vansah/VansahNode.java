package com.vansah;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;


public class VansahNode {
	
	/**
	 * The API version used for the Vansah API calls. This versioning helps in targeting
	 * the specific iteration of the API for compatibility and feature availability.
	 */
	private static final String API_VERSION = "v1";

	/**
	 * The base URL of the Vansah API. This URL is the entry point for all API requests,
	 * ensuring that requests are directed to the correct environment and domain.
	 */
	private static final String VANSAH_URL = "https://prod.vansahnode.app";

	/**
	 * The endpoint URL for adding a test run. This URL is constructed by appending the
	 * specific API operation path to the base Vansah URL and API version, facilitating
	 * the creation of new test runs in the Vansah system.
	 */
	private static final String ADD_TEST_RUN = VANSAH_URL + "/api/" + API_VERSION + "/run";

	/**
	 * The endpoint URL for adding a test log. Similar to the add test run URL, this
	 * endpoint allows for the logging of individual test results within a test run, providing
	 * a way to programmatically report overall test outcomes.
	 */
	private static final String ADD_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION + "/logs";

	/**
	 * The endpoint URL for updating a test log. This URL supports modifying existing test
	 * logs, enabling updates to the outcomes or details of a test after its initial logging.
	 */
	private static final String UPDATE_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION + "/logs/";

	/**
	 * The endpoint URL for removing a test log. This operation allows for the deletion of
	 * specific test logs from a test run, offering flexibility in managing test records.
	 */
	private static final String REMOVE_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION + "/logs/";

	/**
	 * The endpoint URL for removing a test run. This URL enables the deletion of entire test
	 * runs, facilitating the cleanup of test records or the removal of erroneously created entries.
	 */
	private static final String REMOVE_TEST_RUN = VANSAH_URL + "/api/" + API_VERSION + "/run/";

	/**
	 * The endpoint URL for retrieving test scripts associated with a test case. This URL
	 * provides access to the list of test scripts that are part of a specific test case,
	 * supporting the automation and management of test executions.
	 */
	private static final String TEST_SCRIPT = VANSAH_URL + "/api/" + API_VERSION + "/testCase/list/testScripts";


	/**
	 * The authentication token required for making requests to the Vansah API. This token
	 * authenticates the client to the Vansah system, ensuring secure access to API functions.
	 * Replace "Your Token Here" with the actual token provided by Vansah. Note that this token
	 * should be kept confidential to prevent unauthorized access to the API.
	 */
	private static String VANSAH_TOKEN = "Your Token Here";

	/**
	 * Sets the Vansah API authentication token to the specified value. This method allows for
	 * dynamically updating the token used for authenticating requests to the Vansah API. 
	 * Changing the token can be necessary if the current token expires or if switching to 
	 * a different Vansah environment.
	 * 
	 * @param vansahToken The new authentication token to be used for API requests.
	 */
	public static void setVansahToken(String vansahToken) {
		VANSAH_TOKEN = vansahToken;
	}
	/**
	 * The hostname or IP address of the proxy server used when the Vansah API binding operates behind a proxy.
	 * Specify the address of the proxy to enable communication with the Vansah API through it. Leave blank if
	 * not using a proxy.
	 */
	private static final String hostAddr = "";

	/**
	 * The port number on which the proxy server is listening. This should be specified alongside the hostAddr
	 * to configure the proxy settings correctly. Leave blank if not using a proxy.
	 */
	private static final String portNo = "";



	/**
	 * Controls whether test results are sent to Vansah. This flag determines the behavior of the application
	 * in terms of communicating with the Vansah API to update test outcomes.
	 * 
	 * - "0": No results will be sent to Vansah. Use this setting if you wish to run tests without updating
	 *        the Vansah system with the results, which can be useful in development or testing environments.
	 * - "1": Results will be sent to Vansah. This should be set when you want to ensure that the outcomes of
	 *        test executions are recorded in Vansah, typically in staging or production environments.
	 */
	private static final String updateVansah = "1";


	/** The unique identifier for the test folder. Mandatory unless a JIRA issue key is provided. */
	private String TESTFOLDERS_ID;

	/** The key for the JIRA issue. Mandatory unless a test folder identifier is provided. */
	private String JIRA_ISSUE_KEY;

	/** The name of the sprint. This field is mandatory. */
	private String SPRINT_NAME;

	/** The identifier for the test case, e.g., "TEST-C1". This field is mandatory. */
	private String CASE_KEY;

	/** The name of the release or version in JIRA. This field is mandatory. */
	private String RELEASE_NAME;

	/** The environment identifier from Vansah for the JIRA app, e.g., "SYS" or "UAT". This field is mandatory. */
	private String ENVIRONMENT_NAME;

	/** The result key indicating the test outcome. Options: 0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested. Mandatory. */
	private int RESULT_KEY;

	/** Whether to take a screenshot of the webpage being tested. True to take a screenshot; otherwise, false. */
	private boolean SEND_SCREENSHOT;

	/** A comment describing the actual result of the test. */
	private String COMMENT;

	/** The index of the test step within the test case. */
	private int STEP_ORDER;

	/** An identifier for the test run, generated by the API request. */
	private String TEST_RUN_IDENTIFIER;

	/** An identifier for the test log, generated by the API request. */
	private String TEST_LOG_IDENTIFIER;

	/** The path to a file associated with the test, if any. */
	private String FILE;

	/** An image file containing a screenshot related to the test, if required. */
	private File image;

	/** The HTTP client builder for making API requests. */
	private HttpClientBuilder clientBuilder;

	/** The credentials provider for the HTTP client, used in authenticated API requests. */
	private CredentialsProvider credsProvider;

	/** A map of headers to include in the API request. */
	private Map<String, String> headers = new HashMap<>();

	/** A mapping from result names to their corresponding numeric codes. */
	private HashMap<String, Integer> resultAsName = new HashMap<>();

	/** The JSON object representing the body of the API request. */
	private JSONObject requestBody = null;


	
	/**
	 * Constructs an instance of VansahNode with specified test folder ID and JIRA issue key.
	 * Initializes the mapping for test result codes. This constructor is used when both
	 * the test folder's identifier and the JIRA issue key are known and need to be set
	 * for the instance.
	 *
	 * @param TESTFOLDERS_ID The unique identifier for the test folder. Mandatory unless a JIRA issue key is provided.
	 * @param JIRA_ISSUE_KEY The key for the JIRA issue. Mandatory unless a test folder identifier is provided.
	 */
	public VansahNode(String TESTFOLDERS_ID, String JIRA_ISSUE_KEY) {
	    super();
	    this.TESTFOLDERS_ID = TESTFOLDERS_ID;
	    this.JIRA_ISSUE_KEY = JIRA_ISSUE_KEY;
	    // Initialize test result mapping
	    resultAsName.put("NA", 0);
	    resultAsName.put("FAILED", 1);
	    resultAsName.put("PASSED", 2);
	    resultAsName.put("UNTESTED", 3);
	}

	/**
	 * Default constructor for VansahNode. Initializes the mapping for test result codes
	 * without setting test folder ID or JIRA issue key. Use this constructor when these
	 * identifiers will be set later or in a different manner.
	 */
	public VansahNode() {
	    super();
	    // Initialize test result mapping
	    resultAsName.put("NA", 0);
	    resultAsName.put("FAILED", 1);
	    resultAsName.put("PASSED", 2);
	    resultAsName.put("UNTESTED", 3);
	}


	/**
	 * Creates a new test run identifier for a given test case based on a JIRA issue.
	 * This method sets the case key and prepares the instance for connecting to the
	 * Vansah REST API to initiate a test run. Screenshots are not sent by default.
	 *
	 * @param testcase The test case identifier (e.g., "TEST-C1").
	 * @throws Exception If there's an error in the API connection or request execution.
	 */
	public void addTestRunFromJIRAIssue(String testcase) throws Exception {
	    this.CASE_KEY = testcase;
	    this.SEND_SCREENSHOT = false;
	    connectToVansahRest("addTestRunFromJIRAIssue", null);
	}

	/**
	 * Creates a new test run identifier for a given test case based on the test folder.
	 * Similar to the JIRA issue-based method, this sets the case key and disables screenshot
	 * sending, preparing for a REST API call to create a test run within a specific test folder.
	 *
	 * @param testcase The test case identifier (e.g., "TEST-C1").
	 * @throws Exception If there's an error in the API connection or request execution.
	 */
	public void addTestRunFromTestFolder(String testcase) throws Exception {
	    this.CASE_KEY = testcase;
	    this.SEND_SCREENSHOT = false;
	    connectToVansahRest("addTestRunFromTestFolder", null);
	}



	/**
	 * Adds a new test log entry for a specific test case. This method is used after creating a test run
	 * to log individual test results. It requires a test run identifier obtained from a previous test run creation.
	 *
	 * @param result The result of the test as an integer. The possible values are:
	 *               0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested.
	 * @param comment A comment describing the test outcome or any relevant details.
	 * @param testStepRow The index of the test step within the test case to which this log belongs.
	 * @param sendScreenShot A boolean indicating whether a screenshot should be sent along with the test log.
	 * @param driver The WebDriver instance used to take a screenshot if sendScreenShot is true.
	 * @throws Exception If there's an error in connecting to the Vansah REST API or during request execution.
	 */
	public void addTestLog(int result, String comment, Integer testStepRow, boolean sendScreenShot, WebDriver driver) throws Exception {
	    this.RESULT_KEY = result;
	    this.COMMENT = comment;
	    this.STEP_ORDER = testStepRow;
	    this.SEND_SCREENSHOT = sendScreenShot;
	    connectToVansahRest("addTestLog", driver);
	}
	/**
	 * Adds a new test log entry for a specific test case, allowing the result to be specified as a string.
	 * This variant facilitates the use of human-readable result codes ("NA", "FAILED", "PASSED", "UNTESTED").
	 * It also requires a test run identifier from a previous test run creation.
	 *
	 * @param result The result of the test as a string. Accepts:
	 *               "NA", "FAILED", "PASSED", "UNTESTED", with case-insensitivity.
	 * @param comment A comment detailing the test outcome or any other pertinent information.
	 * @param testStepRow The test step's index within the test case, associated with this log.
	 * @param sendScreenShot Indicates whether to capture and send a screenshot with the test log.
	 * @param driver The WebDriver used for screenshot capture if sendScreenShot is true.
	 * @throws Exception If an error occurs in the Vansah REST API connection or during the execution of the request.
	 */
	public void addTestLog(String result, String comment, Integer testStepRow, boolean sendScreenShot, WebDriver driver) throws Exception {
	    this.RESULT_KEY = resultAsName.getOrDefault(result.toUpperCase(), 0); // Default to 0 (N/A) if result is unknown
	    this.COMMENT = comment;
	    this.STEP_ORDER = testStepRow;
	    this.SEND_SCREENSHOT = sendScreenShot;
	    connectToVansahRest("addTestLog", driver);
	}

	/**
	 * Creates a new test run and logs a result for a specified test case based on a JIRA issue.
	 * This method is ideal for test cases without detailed steps, where only the overall result is needed.
	 * It sets the case key and result, then connects to the Vansah REST API to create a new log entry.
	 *
	 * @param testcase The test case identifier (e.g., "TEST-C1") associated with a JIRA issue.
	 * @param result The overall result of the test case. Acceptable values are:
	 *               0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested.
	 * @throws Exception If there's an error in connecting to the Vansah REST API or during the execution of the request.
	 */
	public void addQuickTestFromJiraIssue(String testcase, int result) throws Exception {
	    this.CASE_KEY = testcase;
	    this.RESULT_KEY = result;
	    connectToVansahRest("addQuickTestFromJiraISSUE", null);
	}
	/**
	 * Creates a new test run and logs an overall result for a specified test case within a test folder.
	 * Similar to the JIRA issue variant, this method facilitates logging of test outcomes for cases without steps,
	 * focusing on the overall test result. It updates the instance with the test case key and result before
	 * making an API call to Vansah to create a new log entry.
	 *
	 * @param testcase The identifier of the test case (e.g., "TEST-C1") within a test folder.
	 * @param result The overall result of the test case. Possible values are:
	 *               0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested.
	 * @throws Exception If an error occurs during the API connection or request processing.
	 */
	public void addQuickTestFromTestFolders(String testcase, int result) throws Exception {
	    this.CASE_KEY = testcase;
	    this.RESULT_KEY = result;
	    connectToVansahRest("addQuickTestFromTestFolders", null);
	}


	/**
	 * Deletes a test run previously created using either the addTestRun or addQuickTest methods.
	 * This method makes a call to the Vansah REST API to remove the specified test run, identified
	 * by the test run identifier stored within the instance. It's crucial to ensure that the test run
	 * identifier is correctly set before calling this method to avoid unintended deletions.
	 *
	 * @throws Exception If there's an error during the API connection or the execution of the request.
	 *                   This could be due to network issues, authentication errors, or if the test run
	 *                   identifier does not exist.
	 */
	public void removeTestRun() throws Exception {
	    connectToVansahRest("removeTestRun", null);
	}

	/**
	 * Deletes a test log entry that was created using the addTestLog or addQuickTest methods.
	 * By calling this method, a request is made to the Vansah REST API to remove the test log
	 * identified by the test log identifier within this instance. Proper identification of the test log
	 * is necessary to ensure the correct log is removed without affecting other records.
	 *
	 * @throws Exception If an error occurs while connecting to the Vansah REST API or during the request
	 *                   processing. This could be due to various reasons, such as network failures, incorrect
	 *                   authentication, or non-existent test log identifiers.
	 */
	public void removeTestLog() throws Exception {	
	    connectToVansahRest("removeTestLog", null);
	}


	/**
	 * Updates an existing test log with new information. This method allows for changing the result,
	 * adding a comment, and deciding whether to include a screenshot for a test log identified by a
	 * specific test log identifier. It is intended to be used after a test log has been created with
	 * either addTestLog or addQuickTest methods.
	 *
	 * @param result The updated result of the test as an integer. Acceptable values are:
	 *               0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested.
	 * @param comment A new or updated comment about the test outcome or relevant details.
	 * @param sendScreenShot Indicates whether a new screenshot should be sent along with the test log update.
	 * @param driver The WebDriver instance used for taking a screenshot if sendScreenShot is true.
	 * @throws Exception If an error occurs during the connection to the Vansah REST API or the execution
	 *                   of the update request. This might be due to network issues, authentication errors,
	 *                   or invalid test log identifiers.
	 */
	public void updateTestLog(int result, String comment, boolean sendScreenShot, WebDriver driver) throws Exception {
	    this.RESULT_KEY = result;
	    this.COMMENT = comment;
	    this.SEND_SCREENSHOT = sendScreenShot;
	    connectToVansahRest("updateTestLog", driver);
	}

	/**
	 * Updates an existing test log, similar to the integer-based method, but allows specifying the result
	 * as a string for improved readability. This method updates the test log's result, comment, and screenshot
	 * status based on the provided parameters. Suitable for use when test log identifiers are already obtained
	 * from previous interactions with the Vansah API.
	 *
	 * @param result The test result as a string. Expected values are "NA", "FAIL", "PASSED", or "UNTESTED".
	 * @param comment An updated or new comment detailing the test outcome or any pertinent information.
	 * @param sendScreenShot A boolean flag indicating whether to attach a screenshot with the test log update.
	 * @param driver The WebDriver instance for screenshot capture if sendScreenShot is set to true.
	 * @throws Exception If there's an issue with the API connection or during the request's execution,
	 *                   such as network problems, authentication failures, or incorrect test log identifiers.
	 */
	public void updateTestLog(String result, String comment, boolean sendScreenShot, WebDriver driver) throws Exception {
	    this.RESULT_KEY = resultAsName.getOrDefault(result.toUpperCase(), 0); // Default to 0 (N/A) if result is unknown
	    this.COMMENT = comment;
	    this.SEND_SCREENSHOT = sendScreenShot;
	    connectToVansahRest("updateTestLog", driver);
	}
	
	/**
	 * Retrieves the count of test steps for a given test case from Vansah. This method sends a GET request
	 * to the Vansah API to fetch the test script associated with the specified case key and calculates the
	 * number of steps in the test script. It also handles proxy settings if specified.
	 *
	 * @param case_key The unique identifier for the test case whose test script step count is to be retrieved.
	 * @return The number of test steps contained within the test script for the specified case key. Returns 0
	 *         if there's an error in fetching the test script, the response from Vansah is unexpected, or the
	 *         specified test case does not contain any steps.
	 * @throws Exception if there's an issue with network connectivity, parsing the response, or if the Vansah
	 *                   API endpoint is not reachable. In such cases, the exception is caught and printed to the
	 *                   console, and the method returns 0.
	 *
	 * Note: This method assumes that the Vansah API token and possibly proxy settings have been correctly set
	 *       prior to its invocation. It utilizes the `headers` and `clientBuilder` fields of the enclosing class
	 *       to configure the HTTP request.
	 */
	public int testStepCount(String case_key) {


		try {
			headers.put("Authorization",VANSAH_TOKEN);
			headers.put("Content-Type","application/json");

			clientBuilder = HttpClientBuilder.create();
			// Detecting if the system using any proxy setting.


			if (hostAddr.equals("") && portNo.equals("")) {
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
			get = Unirest.get(TEST_SCRIPT).headers(headers).queryString("caseKey", case_key).asJson();
			if (get.getBody().toString().equals("[]")) {
				System.out.println("Unexpected Response From Server: " + get.getBody().toString());
			} else {
				JSONObject jsonobjInit = new JSONObject(get.getBody().toString());
				boolean success = jsonobjInit.getBoolean("success");
				String vansah_message = jsonobjInit.getString("message");

				if (success) {

					int testRows = jsonobjInit.getJSONObject("data").getJSONArray("steps").length();
					System.out.println("NUMBER OF STEPS: " + testRows);
					return testRows;

				} else {
					System.out.println("Error - Response From Vansah: " + vansah_message);
					return 0;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;

	}

	/**
	 * Central method for interacting with the Vansah REST API. Depending on the specified type, it constructs
	 * and executes different API requests to add, update, or remove test runs and logs. This method also handles
	 * proxy settings, authorization headers, and the optional inclusion of screenshots for certain requests.
	 *
	 * @param type The type of operation to perform, which dictates the API endpoint to be called and the payload
	 *             structure. Supported types include "addTestRunFromJIRAIssue", "addTestRunFromTestFolder",
	 *             "addTestLog", "addQuickTestFromJiraISSUE", "addQuickTestFromTestFolders", "removeTestRun",
	 *             "removeTestLog", and "updateTestLog".
	 * @param driver The WebDriver instance used for taking screenshots, if required by the operation. Can be null
	 *               if screenshots are not to be sent.
	 * @throws Exception If an error occurs during the construction or execution of the API request. This includes
	 *                   network errors, JSON parsing errors, or errors from the Vansah API itself. Exceptions are
	 *                   caught and printed to the console.
	 *
	 * Note: This method checks the global "updateVansah" flag before proceeding with any operations to ensure that
	 *       interactions with Vansah are desired. It also sets up HTTP client and proxy configurations based on
	 *       class-level settings.
	 */
	private void connectToVansahRest(String type, WebDriver driver) {

		//Define Headers here
		headers.put("Authorization",VANSAH_TOKEN);
		headers.put("Content-Type","application/json");
		HttpResponse<JsonNode> jsonRequestBody = null;

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
						FILE = encodstring;
						System.out.println("Screenshot succesfully taken.");
					} catch (Exception e) {
						System.out.println("Taking Screenshot failed: " + e.toString());
					}
				}


				if(type == "addTestRunFromJIRAIssue") {

					requestBody = new JSONObject();
					requestBody.accumulate("case", testCase());
					requestBody.accumulate("asset", jiraIssueAsset());
					if(properties().length()!=0) {
						requestBody.accumulate("properties", properties());
					}

					

					jsonRequestBody = Unirest.post(ADD_TEST_RUN).headers(headers).body(requestBody).asJson();

				}
				if(type == "addTestRunFromTestFolder") {
					requestBody = new JSONObject();
					requestBody.accumulate("case", testCase());
					requestBody.accumulate("asset", testFolderAsset());
					if(properties().length()!=0) {
						requestBody.accumulate("properties", properties());
					}

					

					jsonRequestBody = Unirest.post(ADD_TEST_RUN).headers(headers).body(requestBody).asJson();

				}


				if(type == "addTestLog") {
					requestBody =  addTestLogProp();
					if(SEND_SCREENSHOT) {
						
						requestBody.append("attachments", addAttachment(FILE));
						
					}
					
					jsonRequestBody = Unirest.post(ADD_TEST_LOG).headers(headers).body(requestBody).asJson();
				}


				if(type == "addQuickTestFromJiraISSUE") {

					requestBody = new JSONObject();
					requestBody.accumulate("case", testCase());
					requestBody.accumulate("asset", jiraIssueAsset());
					if(properties().length()!=0) {
						requestBody.accumulate("properties", properties());
					}
					requestBody.accumulate("result", resultObj(RESULT_KEY));

					
					
					

					jsonRequestBody = Unirest.post(ADD_TEST_RUN).headers(headers).body(requestBody).asJson();
				}
				if(type == "addQuickTestFromTestFolders") {
					requestBody = new JSONObject();
					requestBody.accumulate("case", testCase());
					requestBody.accumulate("asset", testFolderAsset());
					if(properties().length()!=0) {
						requestBody.accumulate("properties", properties());
					}
					requestBody.accumulate("result", resultObj(RESULT_KEY));

					
					


					jsonRequestBody = Unirest.post(ADD_TEST_RUN).headers(headers).body(requestBody).asJson();
				}


				if(type == "removeTestRun") {
					jsonRequestBody = Unirest.delete(REMOVE_TEST_RUN+TEST_RUN_IDENTIFIER).headers(headers).asJson();
				}


				if(type == "removeTestLog") {
					jsonRequestBody = Unirest.delete(REMOVE_TEST_LOG+TEST_LOG_IDENTIFIER).headers(headers).asJson();
				}


				if(type == "updateTestLog") {
					requestBody = new JSONObject();
					requestBody.accumulate("result", resultObj(RESULT_KEY));
					requestBody.accumulate("actualResult", COMMENT);
					if(SEND_SCREENSHOT) {
						requestBody.append("attachments", addAttachment(FILE));
					}
					
					jsonRequestBody = Unirest.put(UPDATE_TEST_LOG+TEST_LOG_IDENTIFIER).headers(headers).body(requestBody).asJson();
				}


				JSONObject fullBody = jsonRequestBody.getBody().getObject();
				if (jsonRequestBody.getBody().toString().equals("[]")) {
					System.out.println("Unexpected Response From Vansah with empty response: " + jsonRequestBody.getBody().toString());


				} else {
					JSONObject jsonobjInit = new JSONObject(jsonRequestBody.getBody().toString());
					boolean success = jsonobjInit.getBoolean("success");
					String vansah_message = jsonobjInit.getString("message");
					System.out.println("(" + StringUtils.capitalize(type) + ") Return: " + success + ". Vansah Message: " + vansah_message );

					if (success){

						if(type == "addTestRunFromJIRAIssue") {
							TEST_RUN_IDENTIFIER = fullBody.getJSONObject("data").getJSONObject("run").get("identifier").toString();
							System.out.println("Test Run Identifier: " + TEST_RUN_IDENTIFIER);
						}
						if(type == "addTestRunFromTestFolder") {
							TEST_RUN_IDENTIFIER = fullBody.getJSONObject("data").getJSONObject("run").get("identifier").toString();
							System.out.println("Test Run Identifier: " + TEST_RUN_IDENTIFIER);
						}

						if(type == "addTestLog") {
							TEST_LOG_IDENTIFIER = fullBody.getJSONObject("data").getJSONObject("log").get("identifier").toString();
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

	/**
	 * Converts a file to a base64-encoded string. This is primarily used for encoding screenshot files
	 * before attaching them to test logs sent to Vansah.
	 *
	 * @param file The file to be encoded.
	 * @return The base64-encoded string representation of the file's contents, or null if an error occurs.
	 */
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
	
	/**
	 * Sets the test folder ID for the current instance. This ID is used to associate test runs and logs
	 * with a specific test folder in Vansah.
	 *
	 * @param TESTFOLDERS_ID The test folder ID to set.
	 */
	public void setTESTFOLDERS_ID(String TESTFOLDERS_ID) {
		this.TESTFOLDERS_ID = TESTFOLDERS_ID;
	}
	
	/**
	 * Sets the JIRA issue key for the current instance. This key is used to link test runs and logs to a
	 * specific issue in JIRA.
	 *
	 * @param JIRA_ISSUE_KEY The JIRA issue key to set.
	 */
	public void setJIRA_ISSUE_KEY(String JIRA_ISSUE_KEY) {
		this.JIRA_ISSUE_KEY = JIRA_ISSUE_KEY;
	}
	
	/**
	 * Sets the sprint name for the test context. This name is used to associate the test runs and logs
	 * with a specific sprint in the test management tool. It's essential for organizing test results
	 * within the appropriate sprint cycle.
	 *
	 * @param SPRINT_NAME The name of the sprint to be associated with the test runs and logs.
	 */
	public void setSPRINT_NAME(String SPRINT_NAME) {
		this.SPRINT_NAME = SPRINT_NAME;
	}

	/**
	 * Sets the release name for the test context. This name is crucial for linking test runs and logs
	 * to a specific release or version in the project management or test management system. It helps
	 * in tracking the testing progress and results against different releases.
	 *
	 * @param RELEASE_NAME The name of the release to be associated with the test runs and logs.
	 */
	public void setRELEASE_NAME(String RELEASE_NAME) {
		this.RELEASE_NAME = RELEASE_NAME;
	}

	/**
	 * Sets the environment name in which the testing is performed. This information is used to tag
	 * test runs and logs with the specific environment (e.g., Development, Testing, Staging, Production),
	 * enabling better organization and filtering of test results based on the environment.
	 *
	 * @param ENVIRONMENT_NAME The name of the testing environment to be associated with the test runs and logs.
	 */
	public void setENVIRONMENT_NAME(String ENVIRONMENT_NAME) {
		this.ENVIRONMENT_NAME = ENVIRONMENT_NAME;
	}

	/**
	 * Constructs a JSONObject containing test run properties based on the current instance's attributes.
	 * This includes environment, release, and sprint information, if available.
	 *
	 * @return A JSONObject with configured test run properties.
	 */
	private JSONObject properties(){
		JSONObject environment = new JSONObject();
		environment.accumulate("name",ENVIRONMENT_NAME);

		JSONObject release = new JSONObject();
		release.accumulate("name",RELEASE_NAME);

		JSONObject sprint = new JSONObject();
		sprint.accumulate("name",SPRINT_NAME);

		JSONObject Properties = new JSONObject();
		if(SPRINT_NAME!=null) {
			if(SPRINT_NAME.length()>=2) {
				Properties.accumulate("sprint", sprint);
			}
		}
		if(RELEASE_NAME!=null) {
			if(RELEASE_NAME.length()>=2) {
				Properties.accumulate("release", release);
			}
		}
		if(ENVIRONMENT_NAME!=null) {
			if(ENVIRONMENT_NAME.length()>=2) {
				Properties.accumulate("environment", environment);
			}
		}

		return Properties;
	}


	/**
	 * Constructs a JSONObject for the test case using the current case key.
	 *
	 * @return A JSONObject with the test case key, or logs a message if the case key is invalid.
	 */
	private JSONObject testCase() {

		JSONObject testCase = new JSONObject();
		if(CASE_KEY!=null) {
			if(CASE_KEY.length()>=2) {
				testCase.accumulate("key", CASE_KEY);
			}
		}
		else {
			System.out.println("Please Provide Valid TestCase Key");
		}

		return testCase;
	}
	/**
	 * Creates a JSONObject representing a test result identifier. This object is used to specify
	 * the outcome of a test when communicating with the Vansah API.
	 *
	 * @param result The numeric identifier of the test result, where the possible values are typically
	 *               defined by the API (e.g., 0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested).
	 * @return A JSONObject with a single key-value pair where the key is "id" and the value is the
	 *         provided result identifier.
	 */
	private JSONObject resultObj(int result) {
		
		JSONObject resultID = new JSONObject();
		
		resultID.accumulate("id", result);
		
		
		return resultID;
	}
	/**
	 * Constructs a JSONObject that represents a JIRA issue asset. This object is utilized when linking
	 * test runs or logs to a specific JIRA issue, thereby facilitating integration with JIRA for issue
	 * tracking and test management.
	 *
	 * Precondition: The JIRA_ISSUE_KEY field should be set to a valid issue key. If the key is not set
	 * or is invalid (less than 2 characters), the method will print a warning message.
	 *
	 * @return A JSONObject that includes the issue type and key, ready to be incorporated into API requests.
	 *         Returns an empty JSONObject if the JIRA_ISSUE_KEY is not valid.
	 */
	private JSONObject jiraIssueAsset() {

		JSONObject asset = new JSONObject();
		if(JIRA_ISSUE_KEY!=null){
			if(JIRA_ISSUE_KEY.length()>=2) {
				asset.accumulate("type", "issue");
				asset.accumulate("key", JIRA_ISSUE_KEY);
			}
		}
		else {
			System.out.println("Please Provide Valid JIRA Issue Key");
		}


		return asset;
	}
	/**
	 * Creates a JSONObject that represents a test folder asset. This object is used to specify the test folder
	 * when making API requests that require associating test runs or logs with a specific test folder in Vansah.
	 *
	 * Precondition: The TESTFOLDERS_ID field should be set to a valid test folder identifier. If the identifier
	 * is not set or is invalid (less than 2 characters), the method will print a warning message.
	 *
	 * @return A JSONObject that includes the folder type and its identifier, prepared for inclusion in API requests.
	 *         Returns an empty JSONObject if the TESTFOLDERS_ID is not valid.
	 */
	private JSONObject testFolderAsset() {

		JSONObject asset = new JSONObject();
		if(TESTFOLDERS_ID!=null){
			if(TESTFOLDERS_ID.length()>=2) {
				asset.accumulate("type", "folder");
				asset.accumulate("identifier", TESTFOLDERS_ID);
			}
		}
		else {
			System.out.println("Please Provide Valid TestFolder ID");
		}


		return asset;
	}

	/**
	 * Constructs a JSONObject with properties for adding a test log. This includes identifiers for the test run
	 * and step, the result of the test, and any additional comments. This method is instrumental in preparing
	 * the payload for API requests that add or update test logs in Vansah.
	 *
	 * Precondition: Fields for TEST_RUN_IDENTIFIER, STEP_ORDER, RESULT_KEY, and COMMENT must be set before calling
	 * this method to ensure the constructed JSONObject accurately reflects the test log's properties.
	 *
	 * @return A JSONObject containing the necessary properties for a test log, including the run identifier,
	 *         step number, result ID, and actual result comment. This object can be directly used as the body
	 *         of an API request to add or update test logs.
	 */
	private JSONObject addTestLogProp() {

		JSONObject testRun = new JSONObject();
		testRun.accumulate("identifier", TEST_RUN_IDENTIFIER);

		JSONObject stepNumber = new JSONObject();
		stepNumber.accumulate("number", STEP_ORDER);

		JSONObject testResult = new JSONObject();
		testResult.accumulate("id", RESULT_KEY);

		JSONObject testLogProp = new JSONObject();

		testLogProp.accumulate("run", testRun);

		testLogProp.accumulate("step", stepNumber);

		testLogProp.accumulate("result", testResult);

		testLogProp.accumulate("actualResult", COMMENT);


		return testLogProp;
	}
	/**
	 * Constructs a JSONObject that represents an attachment for a test log. This method is used to attach files,
	 * such as screenshots, to test logs by encoding the file content and specifying file metadata.
	 *
	 * @param FILE The base64-encoded string of the file content to be attached.
	 * @return A JSONObject containing the attachment's metadata, including its name, extension, and encoded content.
	 *         The file name is generated to include a timestamp and random characters for uniqueness.
	 */
	private JSONObject addAttachment(String FILE) {

		JSONObject attachmentsInfo = new JSONObject();
		attachmentsInfo.accumulate("name", FileName());
		attachmentsInfo.accumulate("extension", "png");
		//System.out.println(attachmentsInfo);
		attachmentsInfo.accumulate("file", FILE);

		return attachmentsInfo;

	}
	/**
	 * Generates a unique file name for an attachment. The name includes a random alphanumeric string and a
	 * timestamp to ensure uniqueness. This is particularly useful for naming screenshot files or other
	 * attachments to be included in test logs.
	 *
	 * @return A string representing the generated file name, incorporating random characters and the current
	 *         datetime stamp to ensure uniqueness.
	 */
	private String FileName() {

		String fileName = "";
		long millis = System.currentTimeMillis();
		String datetime = LocalDateTime.now().toString();

		datetime = datetime.replace(" ", "");
		datetime = datetime.replace(":", "");
		String rndchars = RandomStringUtils.randomAlphanumeric(16);
		fileName = rndchars + "_" + datetime + "_" + millis;


		return fileName;
	}
	
}
