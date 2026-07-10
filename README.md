<div align="center">
   <a href="https://vansah.com"><img src="https://vansah.com/app/logo/vansahjira-logo.svg" /></a><br>
</div>

<p align="center">The "Vansah API binding for Java" enables seamless integration with Maven, Selenium, Playwright, TestNG, Cucumber and more, while efficiently sending test results to Vansah Test Management for Jira</p>

<p align="center">
    <a href="https://vansah.com/"><b>Website</b></a> •
    <a href="https://vansah.com/connect-integrations/"><b>More Connect Integrations</b></a>
</p>

<hr>

> ⚠️ **API Token Update Required**
>
> Existing Vansah Connect tokens must be regenerated to continue using integrations. All Connect-based integrations and bindings must be updated to the Vansah API v2 endpoint (`<your-vansah-connect-url>/api/v2/`).
>
> - Generate a new Vansah API token
> - Test your integrations to ensure continuity post-release

> In the meantime, for more details check out:
> - [Vansah for Jira — May 2026 Forge Release](https://help.vansah.com/en/articles/13298303-vansah-for-jira-may-2026)
> - [What's new in Vansah — Migration to Forge Platform](https://vansah.com/blog/whats-new-in-vansah-migration-to-forge-platform/)
>
> Questions? [Open an issue](../../issues) or reach out via the [Vansah Support Portal](https://vansahapp.atlassian.net/servicedesk/customer/portals).


<hr>

## Table of Contents

  - [Features](#features)
  - [Prerequisite](#prerequisite)
  - [Configuration](#configuration)
  - [Dependencies](#dependencies)
  - [Usage examples](#usage-examples)
  - [Methods Overview](#methods-overview)

## Features

- Set custom API URL and tokens for authentication.
- Easily connect your Java applications with `Vansah Test Management for Jira` to report test results, and update test runs without manual intervention.
- Automatically send test results to `Vansah` as they are generated, ensuring that your test management system reflects the most current test outcomes.
- Attach screenshots to test runs in `Vansah` for more detailed reporting and analysis.
- Configure the library to suit your project's specific needs, including proxy settings.
- Robust error handling and logging mechanisms to troubleshoot issues during integration and test result reporting.
- Detailed documentation and usage examples to help you get started quickly and make the most out of the `Vansah Binding for Java`.


## Prerequisite

- Make sure that [`Vansah`](https://marketplace.atlassian.com/apps/1224250/vansah-test-management-for-jira?tab=overview&hosting=cloud) is installed in your Jira workspace
- You need to Generate Vansah [`connect`](https://help.vansah.com/en/articles/9824979-generate-a-vansah-api-token-from-jira) token to authenticate with Vansah APIs.
- Your Automation Project requires Java JDK version 8 or newer.
- You need to add Apache Commons Lang, and Unirest into your Maven project [pom.xml](#dependencies) .


## Configuration

- Download/Copy the latest Vansah Binding Java [VansahNode.java](/src/main/java/com/vansah/VansahNode.java) file and extract it into your maven project under test package.
- Now You need to provide your Vansah [`connect`](https://help.vansah.com/en/articles/9824979-generate-a-vansah-api-token-from-jira) token into your VansahNode.java file.
    - We have two options : 
	    - Add Directly into VansahNode.file
		    ```Java

			public class VansahNode {
			
			/**
			 * The authentication token required for making requests to the Vansah API. This token
			 * authenticates the client to the Vansah system, ensuring secure access to API functions.
			 * Replace "Your Token Here" with the actual token provided by Vansah. 
			 * Note that this token should be kept confidential to prevent unauthorized access to the API.
			 */
				private static String VANSAH_TOKEN = "Your Token Here";
			
			}
			
		    ```
	    - Use Setter functions to add the token
	         ```Java
	         
	         
	           VansahNode app = new VansahNode(); //Instance of VansahNode app
	         
	         /**
	             * Sets the Vansah API authentication token to the specified value. This method allows for
	             * dynamically updating the token used for authenticating requests to the Vansah API. 
	             * Changing the token can be necessary if the current token expires or if switching to 
	             * a different Vansah environment.
	       */

			app.setVansahToken("Add your Token here");
			
			/**
	 			* Sets a custom URL for the Vansah APIs. 
	 		*/
			app.setVansahURL("Your Vansah API URL") //Obtain your Vansah Connect URL from Vansah Settings > Vansah API Tokens
			
		    ```

## Dependencies

To Integrate Vansah Binding Java functions, you need to add the below dependencies into your pom.xml file.

```xml

	<dependencies>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.0</version>
		</dependency>
		<dependency>
			<groupId>com.mashape.unirest</groupId>
			<artifactId>unirest-java</artifactId>
			<version>1.4.9</version>
		</dependency>
	</dependencies>
	
```

## Usage examples
- Executing a Test Case with Steps against a Jira Issue 

```java
    public class LoginTest {

    private File screenshotFile;
    
    public WebDriver driver;

    @Before
    public void setUp() {
       // Method implementation...
    }

    @Test
    public void testLogin() {
    
        VansahNode apptest = new VansahNode();
        
        //Set Jira Issue or Test Folder Identifier
        apptest.setJIRA_ISSUE_KEY("TEST-1");
        
        //Set Environment
        apptest.setENVIRONMENT_NAME("QA");
        
        //Running Test Case for an Issue
        apptest.addTestRunFromJIRAIssue("TEST-C1");

 	// Step 1: Navigate to the login page
         try{
        driver.get("https://example.com/login");
        
        //Add logs for each step function without screenshot(ResultID, ActualResultComment , TestStepID);  
        apptest.addTestLog("passed", "Website loaded successfully",1);

        }catch(Exception e){
        
        //Updates an existing test log with new information or failure screenshot when there is any Exception
        apptest.updateTestLog("failed","Failed to load the website URL",screenshotFile);
        }


        // Step 2: Enter credentials and submit the form
        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));
    
        try{
        usernameInput.sendKeys("your_username");
        passwordInput.sendKeys("your_password");
        loginButton.click();
        
        //Add logs for each step function with screenshot(ResultID, ActualResultComment , TestStepID, File Screenshot);  
        apptest.addTestLog("passed", "As expected, User is able to enter the username and password",2,screenshotFile);

        }catch(Exception e){
        
        //Updates an existing test log with new information or failure screenshot when there is any Exception
        apptest.updateTestLog("failed","User is not able to click on Login Button",screenshotFile);
        }


        // Step 3: Verify successful login
        WebElement welcomeMessage = driver.findElement(By.id("welcomeMessage"));
        try{
        assertTrue("Login was successful", welcomeMessage.isDisplayed());
        //Add logs for each step function with screenshot(ResultID, ActualResultComment , TestStepID, File Screenshot);  
        apptest.addTestLog("passed", "As expected, Welcome Message is shown as "+welcomeMessage.isDisplayed(),3,screenshotFile);
        }catch(Exception e){
        
        //Updates an existing test log with new information when there is any Exception
        apptest.updateTestLog("failed","Welcome Message is not shown");
        }
       
    }

    @After
    public void tearDown() {
        // Method implementation...
    }
}
```

- Adding Test Runs using Folder Path, Advanced Test Plan (ATP), and Standard Test Plan (STP)

```java

/**
 * Sample JUnit test class demonstrating how to send test execution results to Vansah.
 * 
 * This class uses VansahNode to:
 * - Connect to Vansah with a Space Key (Jira project key) and token
 * - Send test results using:
 *   1. Test Folder path
 *   2. Advanced Test Plan (ATP)
 *   3. Standard Test Plan (STP)
 */
class Tests {

    // VansahNode instance for sending results
    private final VansahNode sendResults = new VansahNode();

    // Vansah server URL
    private final String vansahURL = "https://prodau.vansah.com";

    // Folder path in Vansah where the test cases are organized
    private final String testfolderPath = "vansah test automation/regression 2025/";

    // Test Case key from Jira
    private final String testCaseKey = "KAN-C17";

    // Space Key (the Jira project key)
    private final String projectKey = "KAN";

    // Advanced Test Plan key used for ATP-based test executions
    private final String testPlanKeyforATP = "KAN-P17";

    // Asset type for ATP (can be "folder" or "issue")
    private final String testPlanAssetType = "folder";

    // Standard Test Plan key used for STP-based test executions
    private final String testPlanKeyforSTP = "KAN-P18";

    /**
     * Setup method that runs before each test.
     * It initializes VansahNode with URL, API token, Space Key (Jira project key),
     * folder path, and test plan keys for both ATP and STP.
     */
    @SuppressWarnings("static-access")
    @BeforeEach
    void setup() {
        sendResults.setVansahURL(vansahURL);
        sendResults.setVansahToken(System.getenv("CONNECT_DEMO_TOKEN")); // Token should be set as environment variable
        sendResults.setProjectKey(projectKey);
        sendResults.setFOLDERPATH(testfolderPath);
        sendResults.setAdvancedTestPlanKey(testPlanKeyforATP);
        sendResults.setStandardTestPlanKey(testPlanKeyforSTP);
    }

    /**
     * Sends a test result to Vansah using the configured test folder path.
     * This is useful when test cases are organized and executed based on folders.
     */
    @Test
    void sendingResultstoVansah_usingTestFolderPath() throws Exception {
        sendResults.addTestRunFromTestFolder(testCaseKey);
        sendResults.addTestLog("passed", "Actual result for the Test Step", 1);
    }

    /**
     * Sends a test result to Vansah using an Advanced Test Plan (ATP).
     * ATP helps manage test execution based on either folder or issue assets.
     */
    @Test
    void sendingResultstoVansahforATP() throws Exception {
        sendResults.addTestRunFromAdvancedTestPlan(testPlanAssetType, testCaseKey);
        sendResults.addTestLog("passed", "Actual result for the Test Step", 1);
    }

    /**
     * Sends a test result to Vansah using a Standard Test Plan (STP).
     * STPs are used to group test runs without needing folder or issue context.
     */
    @Test
    void sendingResultstoVansahforSTP() throws Exception {
        sendResults.addTestRunFromStandardTestPlan(testCaseKey);
        sendResults.addTestLog("passed", "Actual result for the Test Step", 1);
    }
}
```


## Methods Overview
The `VansahNode` class provides a comprehensive interface for interacting with Vansah Test Management for Jira directly from Java applications. Below is a description of its public methods, designed to facilitate various test management tasks such as creating test runs, logging test results, and managing test assets.
### `addTestRunFromJIRAIssue(String testcase)`

Creates a new test run linked to a specific JIRA issue. This method is ideal for tests associated directly with JIRA issues, allowing for automated test run creation within Vansah based on the issue key provided.

- **Parameters**:
  - `testcase`: The test case identifier linked to the JIRA issue.

### `addTestRunFromTestFolder(String testcase)`

Initiates a new test run within a specified test folder. Use this method to organize your test runs within Vansah's folder structure, facilitating structured test management.

- **Parameters**:
  - `testcase`: The identifier of the test case to be included in the test folder.

### `addTestLog(String/Integer result, String comment, Integer testStepRow, File screenshotFile (optional))`

Logs the result of a specific test step, optionally including a comment and a screenshot. This method provides detailed tracking of test execution outcomes.

- **Parameters**:
  - `result`: The outcome of the test step. Accepts either a **String** (`NA`, `FAILED`, `PASSED`, `UNTESTED` — case-insensitive) or an **int** (0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested). Note the string is `NA`, not `N/A`.
  - `comment`: An optional comment describing the test step outcome.
  - `testStepRow`: The index of the test step within the test case.
  - `screenshotFile`: (Optional) The File Object of the screenshot taken to upload : Provide file object or Path of the screenshot.

### `addQuickTestFromJiraIssue(String testcase, int result)` and `addQuickTestFromTestFolders(String testcase, int result)`

Quickly logs the overall result of a test case associated with either a JIRA issue or a test folder. These methods are suited for tests that do not require detailed step-by-step logging.

- **Parameters**:
  - `testcase`: The test case identifier.
  - `result`: The overall test result as an **int** — 0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested.
  
### `addTestRunFromAdvancedTestPlan(String testPlanAssetType, String testCaseKey)`

Adds a new test run in Vansah under an Advanced Test Plan (ATP). The method links the given test case to a specific asset type (either a folder or an issue) defined in the ATP, enabling organized execution tracking.

- **Parameters**:
  - `testPlanAssetType`: The type of asset linked to the ATP. Accepted values are:
  	- `folder` – If the ATP is structured by test folder.
  	- `issue` – If the ATP is linked to a specific Jira issue.
  - `testCaseKey`: The key of the test case to be executed (e.g., "KAN-C17").
  
### `addTestRunFromStandardTestPlan(String testCaseKey)`

Adds a new test run in Vansah under a Standard Test Plan (STP). This method links the specified test case to the configured Standard Test Plan, enabling execution tracking and reporting without the need for a specific folder or issue reference.

- **Parameters**:
  - `testCaseKey`: The key of the test case to be executed under the Standard Test Plan (e.g., "KAN-C17").

### `removeTestRun()` and `removeTestLog()`

Deletes a previously created test run or log. These methods are useful for cleaning up data in Vansah that is no longer relevant or was created in error.

### `updateTestLog(String result, String comment, File screenshotFile (optional))`

Updates an existing test log with new information, such as a revised result or an additional comment, and optionally includes a new screenshot.

- **Parameters**:
  - Similar to `addTestLog`, with the same purpose of detailed logging but for updates.

## Setter Methods of Vansah Binding

The `VansahNode` class provides a set of setter methods to configure your test management context before performing operations such as creating test runs, adding test logs, and more. Here's a detailed overview of each setter method:

### `setProjectKey(String projectKey)`

Sets the **Space Key** (the Jira project key) that scopes your test runs and logs. This is **required for the Vansah API v2** — it is sent as the top-level `project` object on every request.

- **Parameters**:
  - `projectKey`: The Space Key / Jira project key (e.g., `"KAN"`).

> **Note:** A null or empty value prints a warning (`⚠️ Warning: Provided Space Key is null or empty. Value not updated.`) and is ignored.

### `setFOLDERPATH(String FOLDERPATH)`

Configures the **Test Folder Path** for the VansahNode instance. This path is essential for associating your test runs and logs with the correct test folder structure in Vansah.

- **Parameters**:
  - `FOLDERPATH`: The folder path for the test folder in Vansah. The path must contain at least one `/` and must not start with `/`.

> **Note:** Invalid paths (e.g., those starting with `/` or lacking `/`) will print a warning and be ignored.

### `setJIRA_ISSUE_KEY(String JIRA_ISSUE_KEY)`

Sets the JIRA issue key. Use this method to link your test runs or logs with a specific issue in Jira, facilitating better traceability and integration between testing and issue tracking.

- **Parameters**:
  - `JIRA_ISSUE_KEY`: The key of the Jira issue you want to associate with your test runs or logs.

### `setSPRINT_NAME(String SPRINT_NAME)`

Defines the sprint name related to the test activities. Setting the sprint name helps in organizing and filtering test results by specific development sprints.

- **Parameters**:
  - `SPRINT_NAME`: The name of the sprint to associate with your test runs or logs.

### `setRELEASE_NAME(String RELEASE_NAME)`

Assigns the release name to the VansahNode instance. This information is used to group test runs and logs under specific release cycles in Vansah, aiding in release management and reporting.

- **Parameters**:
  - `RELEASE_NAME`: The name of the release or version you're testing against.

### `setENVIRONMENT_NAME(String ENVIRONMENT_NAME)`

Sets the testing environment's name. This helps in categorizing and understanding the context of test runs or logs, especially when managing tests across multiple environments (e.g., development, staging, production).

- **Parameters**:
  - `ENVIRONMENT_NAME`: The name of the environment where the tests are executed.
  
### `setAdvancedTestPlanKey(String testPlanKey);`

Sets the key for the Advanced Test Plan (ATP). This key is used to associate test runs with a specific Advanced Test Plan in Vansah. It is essential when executing test cases that belong to structured test planning under ATP.

- **Parameters**:
  - `testPlanKey`: The unique key of the Advanced Test Plan (e.g., "KAN-P17"), used to link test executions to a defined test plan in Vansah.
  
### `setStandardTestPlanKey(String testPlanKey);`

Sets the key for the Standard Test Plan (STP). This key is used to associate test runs with a specific Standard Test Plan in Vansah. It allows you to group and track test executions under a defined test plan without linking to a specific folder or issue.

- **Parameters**:
  - `testPlanKey`: The unique key of the Standard Test Plan (e.g., "KAN-P17"), used to organize and manage test executions in Vansah.

### `setDebug(boolean debug)`

Enables or disables **request-payload logging**. When enabled, the exact JSON body sent to Vansah is printed before each API call — useful for diagnosing issues such as a malformed folder path or a missing Space Key. Screenshot attachments are logged after the payload so base64 data does not flood the output.

Debug logging is also enabled automatically when the `VANSAH_DEBUG` environment variable is set to `true` or `1`.

- **Parameters**:
  - `debug`: `true` to log outgoing request payloads, `false` to disable.


### Usage

To use these setter methods in your application, create an instance of `VansahNode` and call the relevant setter methods with the appropriate values before proceeding with any test management operations. For example:

```java
VansahNode vansahNode = new VansahNode();
vansahNode.setVansahToken("Add your Token here");
vansahNode.setProjectKey("KAN"); // Space Key (the Jira project key) — required for API v2
vansahNode.setFOLDERPATH("feature-tests/login");
vansahNode.setJIRA_ISSUE_KEY("your-jira-issue-key");
vansahNode.setSPRINT_NAME("your-sprint-name");
vansahNode.setRELEASE_NAME("your-release-name");
vansahNode.setENVIRONMENT_NAME("your-environment-name");
vansahNode.setAdvancedTestPlanKey("KAN-P17");
vansahNode.setStandardTestPlanKey("KAN-P18");
```
## Developed By

[Vansah](https://vansah.com/)
