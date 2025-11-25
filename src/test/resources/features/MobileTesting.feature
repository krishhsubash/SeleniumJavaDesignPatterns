@mobile @appium
Feature: Mobile Testing with Appium
  As a mobile test automation engineer
  I want to test mobile applications using Appium
  So that I can ensure mobile app quality across Android and iOS

  Background:
    Given I am using mobile testing framework

  @android @smoke
  Scenario: Android app basic navigation
    Given I am using "android" device
    When I launch the app
    And I tap on element "id=com.example.app:id/loginButton"
    Then element "id=com.example.app:id/loginForm" should be visible on mobile

  @android @gestures
  Scenario: Android swipe gestures
    Given I am using "android" device
    When I launch the app
    And I swipe from 500,1000 to 500,200
    Then element "id=bottomElement" should be visible on mobile

  @android @gestures
  Scenario: Swipe directions on Android
    Given I am using "android" device
    When I launch the app
    And I swipe "up" on element "id=scrollView"
    Then element "id=hiddenElement" should be visible on mobile
    When I swipe "down" on element "id=scrollView"
    Then element "id=topElement" should be visible on mobile

  @android @keyboard
  Scenario: Hide keyboard on Android
    Given I am using "android" device
    When I launch the app
    And I tap on element "id=usernameField"
    And I enter "testuser" in element "id=usernameField"
    And I hide the keyboard
    Then element "id=loginButton" should be visible on mobile

  @android @scroll
  Scenario: Scroll to element on Android
    Given I am using "android" device
    When I launch the app
    And I scroll to element "id=termsAndConditions"
    Then element "id=termsAndConditions" should be visible on mobile

  @ios @smoke
  Scenario: iOS app basic navigation
    Given I am using "ios" device
    When I launch the app
    And I tap on element "id=loginButton"
    Then element "id=loginForm" should be visible on mobile

  @ios @gestures
  Scenario: iOS swipe gestures
    Given I am using "ios" device
    When I launch the app
    And I swipe from 500,1000 to 500,200
    Then element "accessibility=bottomElement" should be visible on mobile

  @android @hybrid
  Scenario: Android hybrid app context switching
    Given I am using "android" device
    When I launch the app
    And I tap on element "id=webViewButton"
    And I switch to WEBVIEW
    Then current context should be "WEBVIEW"
    When I tap on element "css=.web-element"
    And I switch to NATIVE_APP
    Then current context should be "NATIVE_APP"

  @ios @hybrid
  Scenario: iOS hybrid app context switching
    Given I am using "ios" device
    When I launch the app
    And I tap on element "id=webViewButton"
    And I switch to webview
    Then WEBVIEW context should be available
    When I click on element "css=#submitButton"
    And I switch to native
    Then current context should be "NATIVE_APP"

  @android @app-management
  Scenario: Install and launch Android app
    Given I am using "android" device
    When I install app "/path/to/app.apk"
    And I launch the app
    Then app should be installed
    And element "id=mainScreen" should be visible on mobile

  @android @app-management
  Scenario: Remove Android app
    Given I am using "android" device
    When I remove app "com.example.app"
    Then app should be installed

  @android @regression
  Scenario: Android login flow
    Given I am using "android" device
    When I launch the app
    And I tap on element "id=usernameField"
    And I enter "testuser@example.com" in element "id=usernameField"
    And I hide keyboard
    And I tap on element "id=passwordField"
    And I enter "password123" in element "id=passwordField"
    And I hide keyboard
    And I tap on element "id=loginButton"
    Then element "id=dashboard" should be visible on mobile
    And element "id=welcomeMessage" text should be "Welcome, testuser!" on mobile

  @ios @regression
  Scenario: iOS login flow
    Given I am using "ios" device
    When I launch the app
    And I tap on element "accessibility=usernameField"
    And I enter "testuser@example.com" in element "accessibility=usernameField"
    And I tap on element "accessibility=passwordField"
    And I enter "password123" in element "accessibility=passwordField"
    And I tap on element "accessibility=loginButton"
    Then element "accessibility=dashboard" should be visible on mobile

  @android @coordinates
  Scenario: Tap at specific coordinates on Android
    Given I am using "android" device
    When I launch the app
    And I tap at coordinates 500,800
    Then element "id=tappedArea" should be visible on mobile

  @android @multicontext
  Scenario: Multiple context switches on Android
    Given I am using "android" device
    When I launch the app
    And I should see 1 contexts available
    And I tap on element "id=openWebView"
    And I should see 2 contexts available
    When I switch to "WEBVIEW" context
    Then current context should be "WEBVIEW"
    When I switch to "NATIVE_APP" context
    Then current context should be "NATIVE_APP"

  @android @validation
  Scenario: Validate element visibility on Android
    Given I am using "android" device
    When I launch the app
    Then element "id=loginButton" should be visible on mobile
    And element "id=hiddenElement" should not be visible on mobile

  @ios @validation
  Scenario: Validate element text on iOS
    Given I am using "ios" device
    When I launch the app
    Then element "accessibility=appTitle" text should be "My App" on mobile
    And element "accessibility=versionLabel" should be visible on mobile

  @android @performance
  Scenario: Android app performance test
    Given I am using "android" device
    When I launch the app
    Then element "id=splashScreen" should be visible on mobile
    And I wait for 2 seconds
    Then element "id=mainScreen" should be visible on mobile

  @android @ios @crossplatform
  Scenario Outline: Cross-platform element validation
    Given I am using "<platform>" device
    When I launch the app
    Then element "<locator>" should be visible on mobile

    Examples:
      | platform | locator                      |
      | android  | id=mainScreen               |
      | ios      | accessibility=mainScreen    |

  @android @longpress
  Scenario: Long press gesture on Android
    Given I am using "android" device
    When I launch the app
    And I long press on element "id=menuButton"
    Then element "id=contextMenu" should be visible on mobile

  @android @scrolling
  Scenario: Scroll and find element on Android
    Given I am using "android" device
    When I launch the app
    And I scroll to element "id=privacyPolicy"
    And I tap on element "id=privacyPolicy"
    Then element "id=privacyPolicyPage" should be visible on mobile
