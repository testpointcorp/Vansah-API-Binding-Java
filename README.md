# Vansah Java API-Binding
---------
## Introduction

Vansah's API can be used to integrate Vansah with various tools, automation and development frameworks including third-party applications. The main usage for the API is to allow our customers to integrate their automated tests and submit test results to Vansah. For a full detail of what other tasks you can perform using the API the list can be obtained from this documentation.



## Overview
Vansah's API is HTTP-based and can used from any framework, programming language and tool. Submitting data to Vansah via the API is done via simple POST requests. Requesting data is done through GET requests. All requests and responses use the JSON format and UTF-8 encoding.

All written requests must use the HTTP POST method, and all read requests must use the HTTP GET method. Data is transferred in the JSON format and UTF-8 encoding.



## Authentication
Vansah expects the authentication credentials to be provided via standard HTTP basic authentication for the API. 

**Workspace Project API Token:** Vansah supports API keys. API keys can be generated in Vansah under Settings. Multiple API keys can be generated and revoked at any time under your settings.




## List of the main Endpoints for Automation Testing:

- **add_test_run**: POST API creates a test run Identifier which is then used with the other testing methods: 1) add_test_log 2) remove_test_run

- **add_test_log**: POST API adds a new test log linked to the project_identifier and the testcase_key. 

- **remove_test_run**: POST API will delete the test log created from add_test_run

- **update_test_log**: POST API will perform any updates required using the test log identifier which is returned from add_test_log & add_quick_test

- **remove_test_log**: POST API will will delete a test log identifier created from add_test_log & add_quick_test

- **add_quick_test**: POST API adds a new test log sequence linked to the test log created by add_test_log endpoint. By calling this endpoint, you will create a new log entry in Vansah with the respective **overal** result. (0 = N/A, 1= FAIL, 2= PASS, 3 = Not Tested). Quick_test is useful for test cases which there are no steps in the test script, where only the overal result is important. 

- **test_script**: Returns the test script for a given case_key. 


You may refer to the APIs documentation here: http://apidoc.vansah.com/


![image](https://user-images.githubusercontent.com/30623282/112987724-c3c90400-91ae-11eb-9274-6a6f3dd25186.png)



------------
## Adding Vansah Java API-Binding to a Selenium project
--------

1) - Download Vansah API-Binding here: https://github.com/testpointcorp/Vansah-API-Binding-Java.git


For questions, suggestions, or other requests, please reach out to us through our support channels:

https://community.vansah.com/

-- 

© 2021 Vansah®. All rights reserved, See license.md for details.

https://www.vansah.com/

![TP](https://user-images.githubusercontent.com/30623282/112829492-f0145000-90dc-11eb-902d-7d1b4a3d0e22.png)
