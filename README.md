<div align="center">
   <a href="https://vansah.com"><img src="https://vansah.com/wp-content/uploads/2021/07/256x256-3.png" /></a><br>
</div>

<p align="center">The "Vansah API binding for Java" enables seamless integration with Maven, Selenium, TestNG, Cucumber, and more, while efficiently sending test results to Vansah Test Management for Jira</p>

<p align="center">
    <a href="https://vansah.com/"><b>Website</b></a> •
    <a href="https://vansah.com/connect-integrations/"><b>More Connect Integrations</b></a>
</p>

## Table of Contents

  - [Features](#features)
  - [Prerequisite](#Prerequisite)
  - [Configuration](#Configuration)
  - [Dependencies](#Dependencies)
  - [Usage examples](#Usage-examples)
  - [Methods Overview](#Methods-Overview)

## Features

- Easily connect your Java applications with `Vansah Test Management for Jira` to report test results, and update test runs without manual intervention.
- Automatically send test results to `Vansah` as they are generated, ensuring that your test management system reflects the most current test outcomes.
- Attach files, such as screenshots or logs, to test cases or test runs in `Vansah` for more detailed reporting and analysis.
- Configure the library to suit your project's specific needs, including proxy settings.
- Robust error handling and logging mechanisms to troubleshoot issues during integration and test result reporting.
- Detailed documentation and usage examples to help you get started quickly and make the most out of the `Vansah Binding for Java`.


## Prerequisite

- Make sure that [`Vansah`](https://marketplace.atlassian.com/apps/1224250/vansah-test-management-for-jira?tab=overview&hosting=cloud) is installed in your Jira workspace
- You need to Generate Vansah [`connect`](https://docs.vansah.com/docs-base/generate-a-vansah-api-token-from-jira-cloud/) token to authenticate with Vansah APIs.
- Your Automation Project requires Java JDK version 8 or newer.
- You need to add Selenium WebDriver, Apache Commons Lang, and Unirest into your Maven project (`[pom.xml](#Dependencies)`).


## Configuration

- Download/Copy the latest Vansah Binding Java [VansahNode.java](/src/main/java/com/vansah/VansahNode.java) file and extract it into your maven project under test package.
- Now You need to provide your Vansah [`connect`](https://docs.vansah.com/docs-base/generate-a-vansah-api-token-from-jira-cloud/) token into your VansahNode.java file.
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
	    - Use Setter functions to add the token.
	         ```Java
	         
	         
	           VansahNode app = new VansahNode(); //Instance of VansahNode app
	         
	         /**
	             * Sets the Vansah API authentication token to the specified value. This method allows for
	             * dynamically updating the token used for authenticating requests to the Vansah API. 
	             * Changing the token can be necessary if the current token expires or if switching to 
	             * a different Vansah environment.
	       */

			app.setVansahToken("Add your Token here");
			
		    ```

## Dependencies

To Integrate Vansah Binding Java functions, you need to add the below dependencies into your pom.xml file.

```java

	<dependencies>
		<dependency>
			<groupId>org.seleniumhq.selenium</groupId>
			<artifactId>selenium-remote-driver</artifactId>
			<version>3.8.1</version>
		</dependency>
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

    private WebDriver driver;

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
        driver.get("https://example.com/login");
        

        // Step 2: Enter credentials and submit the form
        WebElement usernameInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));
    
        try{
        usernameInput.sendKeys("your_username");
        passwordInput.sendKeys("your_password");
        loginButton.click();
        
        //Add logs for each step function(ResultID, ActualResultComment , TestStepID, screenshotTrueorFalse, chromedriver/OtherBrowserdriver);  
        apptest.addTestLog("passed", "As expected, User is able to enter the username and password",0, true, driver);

        }catch(Exception e){
        
        //Updates an existing test log with new information when there is any Exception
        apptest.updateTestLog("failed","User is not able to click on Login Button",true,driver);
        }


        // Step 3: Verify successful login
        WebElement welcomeMessage = driver.findElement(By.id("welcomeMessage"));
        try{
        assertTrue("Login was successful", welcomeMessage.isDisplayed());
        //Add logs for each step function(ResultID, ActualResultComment , TestStepID, screenshotTrueorFalse, chromedriver/OtherBrowserdriver);  
        apptest.addTestLog("passed", "As expected, Welcome Message is shown as "+welcomeMessage.isDisplayed(),0, true, driver);
        }catch(Exception e){
        
        //Updates an existing test log with new information when there is any Exception
        apptest.updateTestLog("failed","Welcome Message is not shown",true,driver);
        }
       
    }

    @After
    public void tearDown() {
        // Method implementation...
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

### `addTestLog(String result, String comment, Integer testStepRow, boolean sendScreenShot, WebDriver driver)`

Logs the result of a specific test step, optionally including a comment and a screenshot. This method provides detailed tracking of test execution outcomes.

- **Parameters**:
  - `result`: The outcome of the test step (e.g., PASSED, FAILED).
  - `comment`: An optional comment describing the test step outcome.
  - `testStepRow`: The index of the test step within the test case.
  - `sendScreenShot`: Flag indicating whether to include a screenshot.
  - `driver`: The Selenium WebDriver instance for screenshot capture.

### `addQuickTestFromJiraIssue(String testcase, int result)` and `addQuickTestFromTestFolders(String testcase, int result)`

Quickly logs the overall result of a test case associated with either a JIRA issue or a test folder. These methods are suited for tests that do not require detailed step-by-step logging.

- **Parameters**:
  - `testcase`: The test case identifier.
  - `result`: The overall test result (e.g., PASS, FAIL).

### `removeTestRun()` and `removeTestLog()`

Deletes a previously created test run or log. These methods are useful for cleaning up data in Vansah that is no longer relevant or was created in error.

### `updateTestLog(String result, String comment, boolean sendScreenShot, WebDriver driver)`

Updates an existing test log with new information, such as a revised result or an additional comment, and optionally includes a new screenshot.

- **Parameters**:
  - Similar to `addTestLog`, with the same purpose of detailed logging but for updates.

## Setter Methods of Vansah Binding

The `VansahNode` class provides a set of setter methods to configure your test management context before performing operations such as creating test runs, adding test logs, and more. Here's a detailed overview of each setter method:

### `setTESTFOLDERS_ID(String TESTFOLDERS_ID)`

Configures the test folder ID for the VansahNode instance. This ID is essential for associating your test runs and logs with the correct test folder in Vansah.

- **Parameters**:
  - `TESTFOLDERS_ID`: The unique identifier for the test folder in Vansah.

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

### Usage

To use these setter methods in your application, create an instance of `VansahNode` and call the relevant setter methods with the appropriate values before proceeding with any test management operations. For example:

```java
VansahNode vansahNode = new VansahNode();
vansahNode.setTESTFOLDERS_ID("your-test-folder-id");
vansahNode.setJIRA_ISSUE_KEY("your-jira-issue-key");
vansahNode.setSPRINT_NAME("your-sprint-name");
vansahNode.setRELEASE_NAME("your-release-name");
vansahNode.setENVIRONMENT_NAME("your-environment-name");
```
## Developed By

[Vansah](https://vansah.com/)
