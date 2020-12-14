package testpoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.xml.serializer.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/*
 * StreamTest Class is designed to perform different function like
 * 1--- 
 */
public class Vansah {

	
	private String VANSAH_CYCLE;
	private String VANSAH_CASE;
	private String VANSAH_RELEASE;
	private String VANSAH_BUILD;
	private String VANSAH_ENVIRONMENT;
	private String VANSAH_RESULT;
	private boolean SEND_SCREENSHOT;
	private String VANSAH_COMMENT;
	private String VANSAH_AGENT = "";
	private String VANSAH_TOKEN;
	private Integer VANSAH_TESTSTEP_ROW;
	private Integer VANSAH_TESTSTEP_IDENTIFIER;
	private String USER_TOKEN;
	private String PROJECT_IDENTIFIER;
	private String LOG_IDENTIFIER;
	private String TESTLOG_IDENTIFIER;
	private int RESULT;
	private String COMMENT;
	private int VANSAH_STEPID;
	private int VANSAH_DATA_ROW_NUM;
	private String VANSAH_DATA_COLUMN_NAME;
	private String VANSAH_ATTACHMENT;
	private String HTTPS_RESPONSE_TAG;
	private String HTTPS_RESULT;
	private String MESSAGE_FROM_VANSAH;
	private static final String VANSAH_HOST = "https://vansahapp.net/api/v1/auto/testlog/";
	private static final String ADD_TEST_LOG = "add_test_log";
	private static final String QUICK_TEST_UPDATE = "quick_test_update";
	private HashMap<Integer, String> testSteps = new HashMap<Integer, String>();
	private HashMap<Integer, String> testTransactions = new HashMap<Integer, String>();
	private HashMap<Integer, String> testResults = new HashMap<Integer, String>();
	private HashMap<String, List<String>> testData = new HashMap<String, List<String>>();
	private HashMap<Integer, String> testCycles = new HashMap<Integer, String>();
	private HashMap<String, String> testRequirements = new HashMap<String, String>();
	private LinkedHashMap<String, HashMap<String, String>> testFields = new LinkedHashMap<String, HashMap<String, String>>();
	private List<Integer> listOfSteps;
	private int testRows;
	private int testDataRows;
	private ReadConfigVansah configReader;
	private Host computer;
	private VansahLogHandler vlh;
	private HttpClientBuilder clientBuilder;
	private CredentialsProvider credsProvider;
	private String version = "V2.3.5";

	public Vansah() {

		vlh = new VansahLogHandler();
		configReader = new ReadConfigVansah();
		computer = new Host();
	}

	public LinkedHashMap<String, HashMap<String, String>> getTestFields() {
		return testFields;
	}

	public Map<String, List<String>> getTestData() {
		return testData;
	}

	public HashMap<Integer, String> getTestCycles() {
		return testCycles;
	}

	public HashMap<String, String> getTestRequirements() {
		return testRequirements;
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

	public HashMap<Integer, String> getTestTransactions() {
		return testTransactions;
	}

	public HashMap<Integer, String> getTestResults() {
		return testResults;
	}

	public String getVersion() {
		return version;
	}

	

	public String getCurrentDate() {
		// 07/01/2015 06:24:13
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		// sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		// String dateStr = sdf.format(new Date());
		// return dateStr;
		Date date = new Date();
		String cDate = sdf.format(date);
		return cDate;
	}

	private String getDateAndTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateStr = sdf.format(new Date());
		// System.out.println(dateStr);
		return dateStr;
	}
	
	
	public void addTestLog(String cycle, String testcase, String release, String build, String environment) throws Exception {

		// this.VANSAH_PACKAGE = packageName;
		this.VANSAH_TOKEN = this.configReader.getVansahToken();
		this.USER_TOKEN = this.configReader.getUserToken();
		this.PROJECT_IDENTIFIER = this.configReader.getProjectIdentifier();
		this.VANSAH_CYCLE = String.valueOf(cycle);
		this.VANSAH_CASE = testcase;
		this.VANSAH_RELEASE = release;
		this.VANSAH_ENVIRONMENT = environment;
		this.SEND_SCREENSHOT = false;

		if (this.VANSAH_AGENT.isEmpty()) {
			if (this.configReader.getsAgentName().equals("")) {
				String c_name = this.computer.getComputerName();
				this.VANSAH_AGENT = c_name;
			} else {
				String c_name = this.configReader.getsAgentName();
				this.VANSAH_AGENT = c_name;
			}
		}

		connectToVansahRest("addTestLog", null);
	}
	
	public void quickTestUpdate(int result, String comment, Integer testStepRow, Integer testStepIdentifier, boolean sendScreenShot, WebDriver driver) throws Exception {
		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.VANSAH_TOKEN = this.configReader.getVansahToken();
		this.USER_TOKEN = this.configReader.getUserToken();
		this.RESULT = result;
		this.COMMENT = comment;
		this.SEND_SCREENSHOT = sendScreenShot;
		this.VANSAH_TESTSTEP_ROW = testStepRow;
		this.VANSAH_TESTSTEP_IDENTIFIER = testStepIdentifier;

		if (this.VANSAH_AGENT.isEmpty()) {
			if (this.configReader.getsAgentName().equals("")) {
				String c_name = this.computer.getComputerName();
				this.VANSAH_AGENT = c_name;
			} else {
				String c_name = this.configReader.getsAgentName();
				this.VANSAH_AGENT = c_name;
			}
		}

		connectToVansahRest("quickTestUpdate", driver);
	}
	
	
	
	
	private void connectToVansahRest(String type, WebDriver driver) {
		
		HttpResponse<JsonNode> jsonResponse = null;
		
		if (configReader.getsUpdateVansah().equals("0")) {
			vlh.writeToV_LogFile(VANSAH_CASE, VANSAH_STEPID, VANSAH_RESULT, VANSAH_COMMENT, VANSAH_RELEASE, VANSAH_BUILD, VANSAH_ENVIRONMENT, VANSAH_AGENT, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
			HTTPS_RESULT = "#STATUS_OFF";
			System.out.println(HTTPS_RESULT);
		} else {

			try {
				clientBuilder = HttpClientBuilder.create();
				// Detecting if the system using any proxy setting.
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
					jsonResponse = Unirest.post(VANSAH_HOST+ADD_TEST_LOG).header("workspace-token",VANSAH_TOKEN).header("user-token",USER_TOKEN)
					.field("case_key", VANSAH_CASE).field("cycle_key", VANSAH_CYCLE).field("release_key", VANSAH_RELEASE)
					.field("environment_key", VANSAH_ENVIRONMENT).field("build_key", VANSAH_BUILD).field("project_identifier", PROJECT_IDENTIFIER).asJson();
				}
				
				if(type == "quickTestUpdate") {
					
					if (SEND_SCREENSHOT) {
						try {
							System.out.println("Taking screenshot");
							WebDriver augmentedDriver = new Augmenter().augment(driver);
							File image = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
							String encodstring = encodeFileToBase64Binary(image);
							this.VANSAH_ATTACHMENT = "data:image/png;base64," + encodstring;
						} catch (Exception e) {
							System.out.println("Taking Screenshot failed: " + e.toString());
						}
					}
					
					jsonResponse = Unirest.post(VANSAH_HOST+QUICK_TEST_UPDATE).header("workspace-token",VANSAH_TOKEN).header("user-token",USER_TOKEN)
					.field("log_identifier", LOG_IDENTIFIER).field("result_key", RESULT).field("comment", COMMENT).field("file", VANSAH_ATTACHMENT)
					.field("step_identifier", VANSAH_TESTSTEP_IDENTIFIER).field("step_order", VANSAH_TESTSTEP_ROW).asJson();
				}
				
				
				
				JSONObject fullBody = jsonResponse.getBody().getObject();
			    //System.out.println(fullBody);
			   
			    if (fullBody.equals("[]")) {
					System.out.println("Unexpected response from Vansah with empty response!");
					vlh.writeErrorToV_errorFile(VANSAH_CASE, VANSAH_STEPID, VANSAH_RESULT, VANSAH_COMMENT + ", Response from Vansah: " + fullBody, VANSAH_RELEASE, VANSAH_BUILD, VANSAH_ENVIRONMENT, VANSAH_AGENT, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
				
			    }else{ 
				
					if (jsonResponse.getBody().getObject().getBoolean("success")){
						MESSAGE_FROM_VANSAH = jsonResponse.getBody().getObject().get("message").toString();
				    	System.out.println(MESSAGE_FROM_VANSAH);
				    	
				    	if(type == "addTestLog") {
					    	LOG_IDENTIFIER = fullBody.getJSONObject("data").get("log_identifier").toString();
					    	System.out.println("Log Identifier: " + LOG_IDENTIFIER);
				    	}
				    	
				    	if(type == "quickTestUpdate") {
				    		TESTLOG_IDENTIFIER = fullBody.getJSONObject("data").get("testlog_identifier").toString();
					    	System.out.println("TestLog Identifier: " + TESTLOG_IDENTIFIER);
				    	}
						
					}else if (!jsonResponse.getBody().getObject().getBoolean("success")){
						MESSAGE_FROM_VANSAH = jsonResponse.getBody().getObject().get("message").toString();
						System.out.println("Response From Server: " + MESSAGE_FROM_VANSAH);
						vlh.writeErrorToV_errorFile(VANSAH_CASE, VANSAH_STEPID, VANSAH_RESULT,VANSAH_COMMENT + ", Response from Vansah: " + MESSAGE_FROM_VANSAH, VANSAH_RELEASE,VANSAH_BUILD, VANSAH_ENVIRONMENT, VANSAH_AGENT, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
					} else {
						System.out.println("Unexpected response from Vansah: " + jsonResponse.getBody().getObject().get(HTTPS_RESPONSE_TAG));
						vlh.writeErrorToV_errorFile(VANSAH_CASE, VANSAH_STEPID, VANSAH_RESULT,VANSAH_COMMENT + ", Response from Vansah: " + MESSAGE_FROM_VANSAH, VANSAH_RELEASE,VANSAH_BUILD, VANSAH_ENVIRONMENT, VANSAH_AGENT, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
					}
				}

				
			} catch (Exception ex) {
				ex.printStackTrace();
				vlh.writeErrorToV_errorFile(VANSAH_CASE, VANSAH_STEPID, VANSAH_RESULT, ex.getMessage(), VANSAH_RELEASE,VANSAH_BUILD, VANSAH_ENVIRONMENT, VANSAH_AGENT, VANSAH_DATA_ROW_NUM, VANSAH_DATA_COLUMN_NAME);
			}
		}
	}


	public String getHttpsStatus() {
		return HTTPS_RESPONSE_TAG;

	}

	public String getUpdateStatus() {
		return HTTPS_RESULT;
	}

	

	public void setProperty(String propertyName, String value) {
		configReader.initialiseAgent(propertyName, value);
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