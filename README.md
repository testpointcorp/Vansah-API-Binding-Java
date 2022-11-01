<div align="center">
  <img src="https://vansah.com/wp-content/uploads/2022/06/512x512.png" style="width:300px"/>
</div>
<h1 align="center" style="background-color:cornflowerblue !important;
    color: white !important;">
  Java API-Binder
</h1>

<p align="center">
   Vansah's API can be used to integrate Vansah with various tools, automation and development frameworks including third-party applications.
   This Java Binder is used for Java web automation projects. Example tools would be Selenium with (Java), Cucumber. 
</p>
<br />

---------
## Introduction

Vansah's API can be used to integrate Vansah with various tools, automation and development frameworks including third-party applications. The main usage for the API is to allow our customers to integrate their automated tests and submit test results to Vansah Test Management for Jira. For a full detail of what other tasks you can perform using the API the list can be obtained from this documentation. For more information on API token required for connecting to Jira see: https://community.vansah.com/posts/how-to-generate-a-vansah-api-token-from-jira

## What's New
New Node API's has been integrated which is making it more secure, reliable and improves performance.

You can check out latest api's from the api doc page
<a href="https://apidoc.vansah.com/#3734dc31-9d7e-4bb9-84e4-f6f60cdbdf3f">Click Here</a>

<span>We currently support below api's</span>

<table>
<tr>
<th>Node API's</th>
<th>Use Case</th>
</tr>
<td> https://prod.vansahnode.app/api/v1/run </td>
<td> To create a Test Run Identifier and even this api can be used to a perform quick test </td>
<tr>
<td> https://prod.vansahnode.app/api/v1/run/{{test_run_identifier}}</td>
<td> To delete a test run for a test Case</td>
</tr>
<tr>
<td> https://prod.vansahnode.app/api/v1/logs</td>
<td> To Add a test log against a test step</td>
</tr>
<tr>
<td> https://prod.vansahnode.app/api/v1/logs/{{testLogIdentifier}}</td>
<td> To update the current/existing test log of a test step</td>
</tr>
<tr>
<td> https://prod.vansahnode.app/api/v1/logs/{{testLogIdentifier}}</td>
<td> To delete a test log of a test step</td>
</tr>
<tr>
<td> https://prod.vansahnode.app/api/v1/testCase/list/testScripts?caseKey={{caseKey}}</td>
<td> To get the test step count of a test case</td>
</tr>
</table>


## Authentication
Vansah current node api's expects vansahConnectToken as Authentication that can be passed in the header of a api request. 

<a href="https://community.vansah.com/posts/how-to-create-a-vansah-api-token-in-jira">Generate your own token from here</a>

**Using Vansah Test Management for Jira - API for Test Automation:** <a href="https://vansahapp.atlassian.net/wiki/spaces/VANSAH/pages/66641/Using+Vansah+API+for+Test+Automation">Using Vansah API for Test Automation</a>

## List of the All new Node Endpoints for Automation Testing:
You may refer to the APIs documentation here: <a href="https://vansahapp.atlassian.net/wiki/spaces/VANSAH/pages/66641/Using+Vansah+API+for+Test+Automation">Click Here</a>


## Adding Vansah Java API-Binding to a Selenium project


Download Vansah API-Binding here: https://github.com/testpointcorp/Vansah-API-Binding-Java.git

For questions, suggestions, or other requests, please reach out to us through our support channels:
https://community.vansah.com/ to raise a ticket: https://vansahapp.atlassian.net/servicedesk/customer/portals

-- 

© 2022 Vansah®. All rights reserved, See license.md for details.

https://www.vansah.com/

