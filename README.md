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
  - [Usage examples](#usage-examples)

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

### Example 1 using TestNG: Executing a Test Case with Steps against a Jira Issue

```java

        VansahNode app = new VansahNode();  
        
    //Set Jira Issue
     testExecute.setJIRA_ISSUE_KEY(issueKey);
     //Set Environment
     testExecute.setENVIRONMENT_NAME(environment);
     //Running Test Case for an Issue
      testExecute.addTestRunFromJIRAIssue(testCase);
    //Add logs for each step function(ResultID, ActualResultComment , TestStepID, screenshotTrueorFalse, chromedriver/OtherBrowserdriver);  
     testExecute.addTestLog(Result.PASSED.id, "As expected, Url is opened", TestStep.Step_1.number, Screenshot.TRUE.takeScreenshot, driver);
```
    
### Example 2 : Execute a Quick Test for a Test Case against a Jira Issue
```java
    
```


## Developed By

[Vansah](https://vansah.com/)
