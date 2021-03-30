# Vansah Java API-Binding


------------
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


- **add_test_Log**: this API adds a new test log linked to the project_identifier and the testcase_key. It will return invalid case_key error with status code 400 if the case_key doesn't exist. If the case_key exists, it will return a success message showing the test log have been submitted successfully with 200 status code. This is a mandatory call that needs to happen always before calling quick_test or quic_test_update. 

- **quick_test**: this API adds a new test log sequence linked to the test log created by add_test_log endpoint. By calling this endpoint, you will create a new log entry in Vansah with the respective **overal** result. (0 = N/A, 1= FAIL, 2= PASS, 3 = Not Tested). Quick_test is useful for test cases which there are no steps in the test script, where only the overal result is important. It will also return invalid case_key error with status code 400 if the case_key doesn't exist. If the case_key exists, it will return a success message showing the test log have been submitted successfully with 200 status code.

- quick_test_update: this API updates test log sequence linked to the project_identifier and log_identifier. Use this endpoint if you want to send results for each step executed. This is important if you want to have a full visibilty on what step failed or passed. It will also return invalid case_key error with status code 400 if the case_key doesn't exist. If the case_key exists, it will return a success message showing the test log have been submitted successfully with 200 status code.

- dataset
- session_variable
- add_session_variable
- test_script
- email_reporting_logs



You may refer to the APIs here: http://apidoc.vansah.com/
https://help.vansahapp.net/kb/vansah-api-reference-guide/
------

--------
For questions, suggestions, or other requests, please reach out to us through our support channels:

http://www.vansah.com/

-- 

Copyright Testpoint Pty Ltd. See license.md for details.

http://www.vansah.com/

![TP](https://user-images.githubusercontent.com/30623282/112829492-f0145000-90dc-11eb-902d-7d1b4a3d0e22.png)
