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

- **session_variable**: GET API returns session variable name and session variable value linked with the case_key, environment_key, cycle_key and project_identifier. This API is very useful when you need to transmit values from one automation script to another (e.g. Script A, **"Creates User"**. Script B, **"Deletes User"**. In order to send the userID from Script A to Script B for deletion, Vansah session variable does that for you.

- **add_session_variable**: POST API Adds a session variable along with its value against your test case being executed. (to be used along with above **session_variable** API). 

- **test_script**: GET API returns all test scripts linked to the project_identifier and case_key. 

- **email_reporting_logs**: GET API that returns grouped cases summary and detailed report linked to the project identifier, cycle_key and release_key. It also sends email if user email is added as parameter. 


You may refer to the APIs documentation here: http://apidoc.vansah.com/

![image](https://user-images.githubusercontent.com/30623282/112942783-61571000-917c-11eb-9f01-31048e211e0d.png)


------------
## Adding Vansah Java API-Binding to a Selenium project
--------

1) - Download Vansah API-Binding here: https://github.com/testpointcorp/Vansah-API-Binding-Java.git










For questions, suggestions, or other requests, please reach out to us through our support channels:

http://www.vansah.com/

-- 

Copyright Testpoint Pty Ltd. See license.md for details.

http://www.vansah.com/

![TP](https://user-images.githubusercontent.com/30623282/112829492-f0145000-90dc-11eb-902d-7d1b4a3d0e22.png)
