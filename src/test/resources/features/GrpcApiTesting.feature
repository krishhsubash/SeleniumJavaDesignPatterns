@api @grpc
Feature: gRPC API Testing
  As a QA Engineer
  I want to test gRPC services
  So that I can validate gRPC API functionality

  Background:
    Given I have gRPC server at "localhost" on port 9090

  @smoke @grpc-get
  Scenario: Get user by ID using gRPC
    When I call gRPC method "GetUser" with user ID 1
    Then the gRPC call should be successful
    And the gRPC response should contain user name "John Doe"
    And the gRPC channel should be active

  @grpc-create
  Scenario: Create new user using gRPC
    When I call gRPC CreateUser with name "Jane Smith" and email "jane@example.com"
    Then the gRPC call should be successful
    And the gRPC response should contain user name "Jane Smith"

  @grpc-connection
  Scenario: Verify gRPC channel connectivity
    Then the gRPC channel should be active

  @grpc-secure
  Scenario: Connect to secure gRPC server
    Given I have secure gRPC server at "localhost" on port 9443
    Then the gRPC channel should be active
