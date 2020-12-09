@Login1
Feature: The user should be able to login screen must request for a username and password

@TC003

Scenario: The sum of a list of numbers should be calculated
    Given a list of numbers
      | 17   |
      | 42   |
      | 4711 |
    When I summarize them
    Then should I get 4770
    
    
@TC004

Scenario: The sum of a list of numbers should be calculated
    Given a list of numbers
      | 18   |
      | 42   |
      | 4711 |
    When I summarize them
    Then should I get 4770