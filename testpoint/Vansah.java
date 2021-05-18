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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Vansah {

	//***************************************** ENDPOINTS *************************************************
	private static final String API_VERSION = "v1";
	private static final String VANSAH_URL = "https://vansahapp.net";
	private static final String ADD_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION + "/auto/testlog/add_test_log";
	private static final String QUICK_TEST = VANSAH_URL + "/api/" + API_VERSION + "/auto/testlog/quick_test";
	private static final String QUICK_TEST_UPDATE = VANSAH_URL + "/api/" + API_VERSION + "/auto/testlog/quick_test_update";
	private static final String DATA_SET = VANSAH_URL + "/api/" + API_VERSION + "/test_case/dataset/";
	private static final String ADD_DATA_SET = VANSAH_URL + "/api/" + API_VERSION + "/test_case/add_dataset/";
	private static final String REMOVE_DATA_SET = VANSAH_URL + "/api/" + API_VERSION + "/test_case/remove_dataset/";
	private static final String SESSION_VARIABLE = VANSAH_URL + "/api/" + API_VERSION + "/test_case/session_variable/";
	private static final String ADD_SESSION_VARIABLE = VANSAH_URL + "/api/" + API_VERSION + "/test_case/add_session_variable/";
	private static final String EMAIL_REPORTING_LOGS = VANSAH_URL + "/api/" + API_VERSION + "/auto/reporting/logs/";
	private static final String TEST_SCRIPT = VANSAH_URL + "/api/" + API_VERSION + "/test_case/test_script";
	//******************************************************************************************************

	
	//*********************************** VARIABLES ****************************************************************************
	private String CYCLE_KEY;
	private String CASE_KEY;
	private String RELEASE_KEY;
	private String BUILD_KEY;
	private String ENVIRONMENT_KEY;
	private int RESULT_KEY;
	private boolean SEND_SCREENSHOT;
	private String COMMENT;
	private String WORKSPACE_TOKEN;
	private Integer STEP_ORDER;
	private Integer STEP_IDENTIFIER;
	private String USER_TOKEN;
	private String PROJECT_IDENTIFIER;
	private String LOG_IDENTIFIER;
	private int VANSAH_DATA_ROW_NUM;
	private String VANSAH_DATA_COLUMN_NAME;
	private String FILE;
	private String HTTPS_RESPONSE_TAG;
	private String HTTPS_RESULT;
	private String MESSAGE_FROM_VANSAH;
	private HashMap<Integer, String> testSteps = new HashMap<Integer, String>();
	private HashMap<Integer, String> testTransactions = new HashMap<Integer, String>();
	private HashMap<Integer, String> testResults = new HashMap<Integer, String>();
	private HashMap<String, List<String>> testData = new HashMap<String, List<String>>();
	private HashMap<String, String> testFields = new HashMap<String, String>();
	private HashMap<Integer, String> stepOrder = new HashMap<Integer, String>();
	private List<Integer> listOfSteps;
	private List<Integer> numberOfSteps;
	private int testRows;
	private int testDataRows;
	private ReadConfigVansah configReader;
	private VansahLogHandler vlh;
	private HttpClientBuilder clientBuilder;
	private CredentialsProvider credsProvider;
	//************************************************************************************************************************************************
	
	
	public Vansah() {

		vlh = new VansahLogHandler();
		configReader = new ReadConfigVansah();
	}
	
	//********************* SEND VANSAH REPORT *********************************************************************************************************
	public void sendReport(String cycle_key, String release_key, String environment_key,String build_key,String email) {
		try {
			System.out.println("Sending Report for CYCLE = " + cycle_key+", RELEASE = " + release_key + ", ENVIRONMENT = " + environment_key + ", BUILD = "+build_key+ ", EMAIL = "+email);
			this.USER_TOKEN = this.configReader.getUserToken();
			this.WORKSPACE_TOKEN = this.configReader.getVansahToken();
			this.PROJECT_IDENTIFIER = this.configReader.getProjectIdentifier();
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
			System.out.println(EMAIL_REPORTING_LOGS);
			get = Unirest.get(EMAIL_REPORTING_LOGS).header("workspace-token",WORKSPACE_TOKEN).header("user-token",USER_TOKEN).queryString("cycle_key", cycle_key)
					.queryString("environment_key", environment_key).queryString("report_by", "cases").queryString("export_type", "xlsx").queryString("build_key", build_key)
					.queryString("release_key", release_key).queryString("email", email).queryString("project_identifier", PROJECT_IDENTIFIER).asJson();
			if (get.getBody().toString().equals("[]")) {
				System.out.println("Unexpected Response From Server: " + get.getBody().toString());
			} else {
				HTTPS_RESPONSE_TAG = get.getBody().getObject().keys().next();
				if (HTTPS_RESPONSE_TAG.toLowerCase().equals("success")) {
					HTTPS_RESULT = get.getBody().getObject().get(HTTPS_RESPONSE_TAG).toString();
					System.out.println("Response From Server: " + HTTPS_RESULT);
				} else if (HTTPS_RESPONSE_TAG.toLowerCase().equals("error")) {
					HTTPS_RESULT = get.getBody().getObject().get(HTTPS_RESPONSE_TAG).toString();
					System.out.println("Response From Server: " + HTTPS_RESULT);
				} else {
					System.out.println("Unexpected response from Vansah: "
							+ get.getBody().getObject().get(HTTPS_RESPONSE_TAG));
				}
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//********************************************************************************************************************
	
	
	
	//************************** VANSAH ADD TEST LOG (LOG IDENTIFIER CREATION *************************************************
	public void addTestLog(String cycle, String testcase, String release, String build, String environment) throws Exception {
		
		this.USER_TOKEN = this.configReader.getUserToken();
		this.WORKSPACE_TOKEN = this.configReader.getVansahToken();
		this.PROJECT_IDENTIFIER = this.configReader.getProjectIdentifier();
		this.CYCLE_KEY = String.valueOf(cycle);
		this.CASE_KEY = testcase;
		this.RELEASE_KEY = release;
		this.ENVIRONMENT_KEY = environment;
		this.BUILD_KEY = build;
		this.SEND_SCREENSHOT = false;

		connectToVansahRest("addTestLog", null);
	}
	//*****************************************************************************************************************
	
	
	
	
	//****************************************** VANSAH QUICK TEST UPDATE *********************************************
	public void quickTestUpdate(int result, String comment, Integer testStepRow, Integer testStepIdentifier, boolean sendScreenShot, WebDriver driver) throws Exception {
		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.USER_TOKEN = this.configReader.getUserToken();
		this.WORKSPACE_TOKEN = this.configReader.getVansahToken();
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.SEND_SCREENSHOT = sendScreenShot;
		this.STEP_ORDER = testStepRow;
		this.STEP_IDENTIFIER = testStepIdentifier;

		connectToVansahRest("quickTestUpdate", driver);
	}
	//*******************************************************************************************************************
	
	
	
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
					//System.out.println("No proxy");
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

				if(type == "addTestLog") {
					jsonResponse = Unirest.post(ADD_TEST_LOG).header("workspace-token",WORKSPACE_TOKEN).header("user-token",USER_TOKEN)
					.field("case_key", CASE_KEY).field("cycle_key", CYCLE_KEY).field("release_key", RELEASE_KEY)
					.field("environment_key", ENVIRONMENT_KEY).field("build_key", BUILD_KEY).field("project_identifier", PROJECT_IDENTIFIER).asJson();
				}
				
				if(type == "quickTestUpdate") {
					
					if (SEND_SCREENSHOT) {
						try {
							System.out.println("Taking screenshot");
							WebDriver augmentedDriver = new Augmenter().augment(driver);
							File image = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
							String encodstring = encodeFileToBase64Binary(image);
							this.FILE = "data:image/png;base64," + encodstring;
						} catch (Exception e) {
							System.out.println("Taking Screenshot failed: " + e.toString());
						}
					}
					
					jsonResponse = Unirest.post(QUICK_TEST_UPDATE).header("workspace-token",WORKSPACE_TOKEN).header("user-token",USER_TOKEN)
					.field("log_identifier", LOG_IDENTIFIER).field("result_key", RESULT_KEY).field("comment", COMMENT).field("file", FILE)
					.field("step_identifier", STEP_IDENTIFIER).field("step_order", STEP_ORDER).asJson();
				}
				
				JSONObject fullBody = jsonResponse.getBody().getObject();
				if (jsonResponse.getBody().toString().equals("[]")) {
					System.out.println("Unexpected Response From Vansah with empty response: " + jsonResponse.getBody().toString());
					vlh.writeErrorToV_errorFile(CASE_KEY, STEP_ORDER, RESULT_KEY, COMMENT + ", Response from Vansah: " + fullBody, RELEASE_KEY, BUILD_KEY, ENVIRONMENT_KEY, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
				} else {
					JSONObject jsonobjInit = new JSONObject(jsonResponse.getBody().toString());
					boolean success = jsonobjInit.getBoolean("success");
					String vansah_message = jsonobjInit.getString("message");
					System.out.println("(SENDING QUICKTEST UPDATE) Return: " + success);
					System.out.println("(SENDING QUICKTEST UPDATE) Message: " + vansah_message);
					
					if (success){
						MESSAGE_FROM_VANSAH = jsonResponse.getBody().getObject().get("message").toString();
				    	System.out.println(vansah_message);
				    	
				    	if(type == "addTestLog") {
					    	LOG_IDENTIFIER = fullBody.getJSONObject("data").get("log_identifier").toString();
					    	System.out.println("Log Identifier: " + LOG_IDENTIFIER);
				    	}
						
					}else{
						MESSAGE_FROM_VANSAH = jsonResponse.getBody().getObject().get("message").toString();
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
	
	
	
	//*********************************** ADD SESSION VARIABLE ***********************************************************
	public void addSessionVariable(String case_key, String cycle_key, String environment_key, String fieldName, String fieldValue) {
		if ((fieldName.length() > 0) && (fieldValue.length() > 0)) {
			try {
				this.USER_TOKEN = this.configReader.getUserToken();
				this.WORKSPACE_TOKEN = this.configReader.getVansahToken();
				this.PROJECT_IDENTIFIER = this.configReader.getProjectIdentifier();
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

				HttpResponse<JsonNode> post;
				post = Unirest.post(ADD_SESSION_VARIABLE).header("workspace-token",WORKSPACE_TOKEN).header("user-token",USER_TOKEN).field("case_key", case_key)
						.field("environment_key", environment_key).field("cycle_key", cycle_key).field("field_name", fieldName).field("field_value", fieldValue).field("project_identifier", PROJECT_IDENTIFIER).asJson();
					JSONObject jsonobjInit = new JSONObject(post.getBody().toString());
					boolean success = jsonobjInit.getBoolean("success");
					String vansah_message = jsonobjInit.getString("message");
					System.out.println("(ADDING SESSION VARIABLES) Return: " + success);
					System.out.println("(ADDING SESSION VARIABLES) Message: " + vansah_message);
					
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			System.out.println("Either Field or Value contains no value");
		}
	}
	//****************************************************************************************************************
	
	
	
	
	//************************ READ SESSION VARIABLE *****************************************************************
	public void sessionVariable(String case_key, String cycle_key, String environment_key) {
		try {
			this.USER_TOKEN = this.configReader.getUserToken();
			this.WORKSPACE_TOKEN = this.configReader.getVansahToken();
			this.PROJECT_IDENTIFIER = this.configReader.getProjectIdentifier();
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
			get = Unirest.get(SESSION_VARIABLE).header("workspace-token",WORKSPACE_TOKEN).header("user-token",USER_TOKEN).queryString("case_key", case_key).queryString("environment_key", environment_key).queryString("cycle_key", cycle_key).queryString("project_identifier", PROJECT_IDENTIFIER).asJson();
			if (get.getBody().toString().equals("[]")) {
				System.out.println("Unexpected Response From Server: " + get.getBody().toString());
			} else {
				JSONObject jsonobjInit = new JSONObject(get.getBody().toString());
				boolean success = jsonobjInit.getBoolean("success");
				String vansah_message = jsonobjInit.getString("message");
				System.out.println("(READING SESSION VARIABLES) Return: " + success);
				System.out.println("(READING SESSION VARIABLES) Message: " + vansah_message);
				if (success) {
					JSONObject jsonobj = new JSONObject(get.getBody().toString());
					//JSONObject jsonObject = jsonobj.getJSONArray("array").getJSONObject(0);
					int sessionVariables = jsonobj.getInt("total");
					System.out.println("(READING SESSION VARIABLE) Number of Session Variables: " + sessionVariables);
					if (sessionVariables > 0) {
						for (int i = 0; i < sessionVariables; i++) {
							JSONObject record = jsonobj.getJSONArray("data").getJSONObject(i);
							HashMap<String, String> temp = new HashMap<String, String>();
							testFields.put(record.getString("field_name"), record.getString("field_value"));
							
						}
					}
				} else if (HTTPS_RESPONSE_TAG.toLowerCase().equals("error")) {
					String HTTPS_RESULT = get.getBody().getObject().get(HTTPS_RESPONSE_TAG).toString();
					System.out.println("Unexpected Response From Server: " + HTTPS_RESULT);
				} else {
					System.out.println(
							"Unexpected Response from Vansah: " + get.getBody().getObject().get(HTTPS_RESPONSE_TAG));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//**************************************************************************************************************************

	
	
	//******************************** READ DATA SET ****************************************************************************
	public void dataSet(String case_key, String cycle_key, String environment_key) {
		try {
			this.USER_TOKEN = this.configReader.getUserToken();
			this.WORKSPACE_TOKEN = this.configReader.getVansahToken();
			this.PROJECT_IDENTIFIER = this.configReader.getProjectIdentifier();
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
			get = Unirest.get(DATA_SET).header("workspace-token",WORKSPACE_TOKEN).header("user-token",USER_TOKEN).queryString("cycle_key", cycle_key)
					.queryString("environment_key", environment_key).queryString("case_key", case_key).queryString("project_identifier", PROJECT_IDENTIFIER).asJson();
			if (get.getBody().toString().equals("[]")) {
				System.out.println("Unexpected Response From Server: " + get.getBody().toString());
			} else {
				JSONObject jsonobjInit = new JSONObject(get.getBody().toString());
				boolean success = jsonobjInit.getBoolean("success");
				String vansah_message = jsonobjInit.getString("message");
				System.out.println("(READING DATA SET) Return: " + success);
				System.out.println("(READING DATA SET) Message: " + vansah_message);
				if (success) {
					JSONObject jsonobj = new JSONObject(get.getBody().toString());
					//JSONObject jsonObject = jsonobj.getJSONArray("data").getJSONObject(0);
					testDataRows = jsonobj.getInt("total");
					System.out.println("(READING DATA SET) Number of Data Rows: " + testDataRows);
					Set<String> rowID = new HashSet<String>();
					if (testDataRows > 0) {
						rowID = jsonobj.getJSONArray("data").getJSONObject(0).keySet();
					}
					for (String row : rowID) {
						List<String> tempList = new ArrayList<String>();
						for (int i = 0; i < testDataRows; i++) {
							JSONObject records = jsonobj.getJSONArray("data").getJSONObject(i);
							tempList.add((String) records.get(row));
						}
						testData.put(row, tempList);
					}
				} else if (HTTPS_RESPONSE_TAG.toLowerCase().equals("error")) {
					String HTTPS_RESULT = get.getBody().getObject().get(HTTPS_RESPONSE_TAG).toString();
					System.out.println("Unexpected Response From Server: " + HTTPS_RESULT);
				} else {
					System.out.println(
							"Unexpected Response from Vansah: " + get.getBody().getObject().get(HTTPS_RESPONSE_TAG));
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			
		}
	}
	//********************************************************************************************************************************
	
	
	
	// ************************************* READ TEST SCRIPT **************************************************************************
	public void testScript(String case_key) {
		try {
			this.USER_TOKEN = this.configReader.getUserToken();
			this.WORKSPACE_TOKEN = this.configReader.getVansahToken();
			this.PROJECT_IDENTIFIER = this.configReader.getProjectIdentifier();
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
			get = Unirest.get(TEST_SCRIPT).header("workspace-token",WORKSPACE_TOKEN).header("user-token",USER_TOKEN).queryString("case_key", case_key).queryString("project_identifier", PROJECT_IDENTIFIER).asJson();
			if (get.getBody().toString().equals("[]")) {
				System.out.println("Unexpected Response From Server: " + get.getBody().toString());
			} else {
				JSONObject jsonobjInit = new JSONObject(get.getBody().toString());
				boolean success = jsonobjInit.getBoolean("success");
				String vansah_message = jsonobjInit.getString("message");
				System.out.println("(READING TEST SCRIPT) Return: " + success);
				System.out.println("(READING TEST SCRIPT) Message:: " + vansah_message);
				if (success) {
					testSteps = new LinkedHashMap<Integer, String>();
					testTransactions = new HashMap<Integer, String>();
					testResults = new HashMap<Integer, String>();
					listOfSteps = new ArrayList<Integer>();
					numberOfSteps = new ArrayList<Integer>();
					stepOrder = new HashMap<Integer, String>();
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
						stepOrder.put(record.getInt("sort_order"), formatString(record, "sort_order"));
						
					}
	
				} else {
					System.out.println("Error - Response From Vansah: " + vansah_message);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//***********************************************************************************************************************
	
	
	public HashMap<String,String> getTestFields() {
		return testFields;
	}

	public Map<String, List<String>> getTestData() {
		return testData;
	}


	public int getNumberOfTestRows() {
		return testRows;
	}

	public int getNumberOfTestDataRows() {
		return testDataRows;
	}

	public List<Integer> getListOfSteps() {
		return listOfSteps;
	}

	
	public HashMap<Integer, String> getTestSteps() {
		return testSteps;
	}


	private String formatString(JSONObject record, String key) {
		if (record.isNull(key))
			return "";
		return record.getString(key);
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

}