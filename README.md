# Vansah Java API-Binding
---------
## Introduction

Vansah's API can be used to integrate Vansah with various tools, automation and development frameworks including third-party applications. The main usage for the API is to allow our customers to integrate their automated tests and submit test results to Vansah. For a full detail of what other tasks you can perform using the API the list can be obtained from this documentation.



## Overview
Vansah's API is HTTP-based and can used from any framework, programming language and tool. Submitting data to Vansah via the API is done via simple POST requests. Requesting data is done through GET requests. All requests and responses use the JSON format and UTF-8 encoding.

Vansah's API is HTTP-based and you can use simple HTTP requests to interact with it. All written requests must use the HTTP POST method, and all read requests must use the HTTP GET method. Data is transferred in the JSON format and UTF-8 encoding.



## Authentication
Vansah expects the authentication credentials to be provided via standard HTTP basic authentication for the API. There are two ways to authenticate API requests with Vansah.

**Username and Password:** You can either use your standard Vansah email (which is your username) and your Vansah password for the API authentication. This will generate a user-token to be used as a header token. This needs to be sent via HTTP basic authentication.

**Workspace and User Token API Key:** Vansah also supports API keys. API keys can be generated in Vansah under Settings. With a username API token key, you would also be required to send your Vansah workspace token provided by your Vansah administrator. Multiple API keys can be generated and revoked at any time under your settings.




## List of the main Endpoints for Automation Testing:


- **add_test_Log**: POST API adds a new test log linked to the project_identifier and the testcase_key. This is a mandatory call before calling quick_test or quick_test_update. 

- **quick_test**: POST API adds a new test log sequence linked to the test log created by add_test_log endpoint. By calling this endpoint, you will create a new log entry in Vansah with the respective **overal** result. (0 = N/A, 1= FAIL, 2= PASS, 3 = Not Tested). Quick_test is useful for test cases which there are no steps in the test script, where only the overal result is important. 

- **quick_test_update**: POST API updates test log sequence linked to the project_identifier and log_identifier. Use this endpoint if you want to send results for each step executed. This is important if you want to have a full visibilty on what step failed or passed.

- **dataset**: GET API returns a copy of the current un-tested/processed test data set which has been mapped against a test case within a Test Cycle and Environment.

- **Session_variable**: GET API returns session variable name and session variable value linked with the case_key, environment_key, cycle_key and project_identifier. This API is very useful when you need to transmit values from one automation script to another (e.g. Script A, **"Creates User"**. Script B, **"Deletes User"**. In order to send the userID from Script A to Script B for deletion, Vansah session variable does that for you.

- **add_session_variable**: POST API Adds a session variable along with its value against your test case being executed. (to be used along with above **session_variable** API). 

- **test_script**: GET API returns all test scripts linked to the project_identifier and case_key. 

- **email_reporting_logs**: GET API that returns grouped cases summary and detailed report linked to the project identifier, cycle_key and release_key. It also sends email if user email is added as parameter. 


You may refer to the APIs documentation here: http://apidoc.vansah.com/

![image](https://user-images.githubusercontent.com/30623282/112942783-61571000-917c-11eb-9f01-31048e211e0d.png)


------------
## Adding Vansah Java API-Binding to a Selenium project (Eclipse Example)
--------

1 - Download Vansah API-Binding here: https://github.com/testpointcorp/Vansah-API-Binding-Java.git

2 - Import Vansah API-Binding into your Selenium project:

    2.1 Choose "File System" option in the Import dialog:

   ![image](https://user-images.githubusercontent.com/30623282/116532223-5cc07b80-a923-11eb-81d7-b6a846ab3b0e.png)
    ---------

    2.2 - Make sure you select **tespoint** folder:

   ![image](https://user-images.githubusercontent.com/30623282/116532428-942f2800-a923-11eb-85f9-18102cc17dea.png)
    ---------


    2.3 - Select testpoint folder and click "Finish". Make sure you check "create top-level folder" option:

   ![image](https://user-images.githubusercontent.com/30623282/116533942-4ca99b80-a925-11eb-9872-41c72d3dda56.png)
    ---------


    2.4 - Right-Click on the testpoint folder and select option Move:

   ![image](https://user-images.githubusercontent.com/30623282/116534278-ac07ab80-a925-11eb-88fa-4dcea5c0f9ed.png)
    ---------
    
    2.5 - Select the project and navigate up to java folder as shown below and click OK:

   ![image](https://user-images.githubusercontent.com/30623282/116534482-ee30ed00-a925-11eb-8068-00053e15fcef.png)
    ---------
    
    2.6 - API-Binder classes should appear under java packages as below:

   ![image](https://user-images.githubusercontent.com/30623282/116534711-37813c80-a926-11eb-9fd9-629806b5f15a.png)
    ---------
    
    
    2.7 - Include these following dependencies in your POM.xml:

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

		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-api</artifactId>
			<version>1.7.25</version>
		</dependency>
	
	
---------
    
    
2.8 - Make sure you save your project and Build it.

---------
    
    
    
3 - Create a new folder and name it as "Vansah" like below:

![image](https://user-images.githubusercontent.com/30623282/116536766-ae1f3980-a928-11eb-847c-d9cff0b4e04f.png)
---------


3.1 - Create a file under Vansah folder and name it as "config.vns". See below:

![image](https://user-images.githubusercontent.com/30623282/116537927-37833b80-a92a-11eb-9ce8-0a5bdf69be39.png)
---------


3.2 - Copy the content below and paste it in the config.vns file you just created:

	sMaxLogResponse=120
	sHostAddr=www.host.com
	sVansahInstance=vansahapp.net
	sVansahConnectionType=1
	sRegScreenShotsDirectory=
	sUpdateVansah=1
	sPortNo=0
	sAgentName=agent1
	sDevMode=1
	sProjectIdentifier = **<you need to update this field> **
	sUserToken = **<you need to update this field> **
	sVansahToken = **<you need to update this field> **

---------
    
3.3 - How to get sProjectIdentifier parameter: Go to your Vansah Instance and get your Project Identifier here, and update your config.vns file 

![image](https://user-images.githubusercontent.com/30623282/116538567-09522b80-a92b-11eb-9c42-10aa7a4fd140.png)

---------
    
3.4 - How to get sUserToken parameter: Go to your Vansah Instance and get your User Token here, and update your config.vns file 


![image](https://user-images.githubusercontent.com/30623282/116539371-128fc800-a92c-11eb-910f-b3e661409fdc.png)

![image](https://user-images.githubusercontent.com/30623282/116539645-731f0500-a92c-11eb-9f5b-9876a000d9c2.png)

Select your Workspace, give it a label and click Create. You should see a new api token in the list. That's your User Token. Copy it and update your config.vns file accordingly.

---------


3.5 - How to get sVansahToken parameter: Go to your Vansah Instance and get your Vansah Token here, and update your config.vns file 

![image](https://user-images.githubusercontent.com/30623282/116539227-e411ed00-a92b-11eb-9359-3469e0cf6a2f.png)

![image](https://user-images.githubusercontent.com/30623282/116650058-c5f7cb80-a9c3-11eb-87a9-5b2decba0470.png)

![image](https://user-images.githubusercontent.com/30623282/116650153-fc354b00-a9c3-11eb-9276-5aee4dbd3d32.png)

Copy your Vansak Token from the list and update your config.vns file accordingly.

---------

------------
## How to use Vansah Java API-Binding in a Selenium project
--------

Now that all configurations are done, you are able to start using the API Binder. Find below what needs to be included in the Selenium script so you can start sending logs to Vansah.

1 - Add **import testpoint.Vansah;** so that you can import Vansah API Binder to your script
2 - Now, under class scope, define the following atributes:
	a) - Define a new string object named as build (any string value) - build of the application under test which all test logs will be written against it.
	b) - Define a new string object named as environment (any string value) - environment of the application under test which all test logs will be written against it.
	c) - Define a new string object named as release (any string value) - release of the application under test which all test logs will be written against it.
	d) - Define a new string object named as cycle (any string value) - cycle of the application under test which all test logs will be written against it.
	e) - Define a new string object named as testCase (any string value) - An existing Vansah testCase id.
	g) - Define a new hashmap object of type <String, String> and named as testFields (any string value) - TestField map where you can read/write session variables. (for further details, refer to [**session_variable**](#session_variable)**session_variable** section on this document).


		  
		//Vansah Test Step ID
		int testStepID;
		String testStep;
		Vansah vansah = new Vansah();


2 - Import Vansah API-Binding into your Selenium project:

    2.1 Choose "File System" option in the Import dialog:

   ![image](https://user-images.githubusercontent.com/30623282/116532223-5cc07b80-a923-11eb-81d7-b6a846ab3b0e.png)
    ---------

    2.2 - Make sure you select **tespoint** folder:

   ![image](https://user-images.githubusercontent.com/30623282/116532428-942f2800-a923-11eb-85f9-18102cc17dea.png)
    ---------


    2.3 - Select testpoint folder and click "Finish". Make sure you check "create top-level folder" option:

   ![image](https://user-images.githubusercontent.com/30623282/116533942-4ca99b80-a925-11eb-9872-41c72d3dda56.png)
    ---------


For questions, suggestions, or other requests, please reach out to us through our support channels:

http://www.vansah.com/

-- 

Copyright Testpoint Pty Ltd. See license.md for details.

http://www.vansah.com/

![TP](https://user-images.githubusercontent.com/30623282/112829492-f0145000-90dc-11eb-902d-7d1b4a3d0e22.png)
