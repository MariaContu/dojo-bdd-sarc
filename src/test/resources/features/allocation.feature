# language: en
Feature: Resource Allocation
  As an IT Academy collaborator
  I want to be able to reserve notebooks, rooms, and labs
  So that students can learn

  Background:
    Given that resources and collaborators are registered in the system

#cobrido
@positive
  Scenario: Successful allocation of an available resource
    Given that the collaborator "10002020" wants to allocate the notebook "CEPES1983"
    When they request the reservation for the date "2026-06-12"
    Then the system should successfully confirm the allocation
#cobrido
@negative
  Scenario: Failed allocation of an invalid collaborator
    Given that the collaborator is not valid
    When they try to allocate a resource
    Then the system should inform that it is not possible
#cobrido
@negative
  Scenario: Failed allocation of two available resources
   Given that a valid collaborator has allocated a notebook
   When they try to allocate another notebook for the same day
   Then the system should inform that it is not possible
#coobrido
@negative
  Scenario: Failed allocation of the same resource
   Given that a valid collaborator has allocated a notebook
   When another collaborator try to allocate the same notebook for the same day
   Then the system should inform that it is not possible
#cobrido
@positive
  Scenario: Successful allocation of two different available resources
    Given that a valid collaborator has allocated a notebook
    When they try to allocate a room for the same day
    Then the system should successfully confirm the allocation
#cobrido
@negative
  Scenario: Failed allocation of a lab and a notebook
   Given that a valid collaborator has allocated a lab
   When they try to allocate a notebook for the same day
   Then the system should inform that it is not possible

@negative
  Scenario: Failed allocation of a lab and a room
    Given that a valid collaborator has allocated a lab
    When they try to allocate a room for the same day 
    Then the system should inform that it is not possible