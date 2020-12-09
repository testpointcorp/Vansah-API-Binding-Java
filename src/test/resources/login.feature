@Login
Feature: The user should be able to login  and the screen must request for a username and password


@Regression
Scenario Outline: The user should be able to login with a valid 
    Given <TestCaseID> I open the page
      When I click on Login after keyin <UserName> and <Password>
      Then I should be redirected to the landing page 
      Then Update results in vansha
    
      
Examples:
|TestCaseID||UserName||Password|
|257     ||sample||sample123|

 
