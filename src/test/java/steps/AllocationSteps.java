package steps;

import cepes.itacademy.sarc.domain.*;
import cepes.itacademy.sarc.repository.*;
import cepes.itacademy.sarc.service.AllocationService;
import io.cucumber.java.en.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AllocationSteps {

    private CollaboratorRepository collaboratorRepo;
    private ResourceRepository resourceRepo;
    private AllocationRepository allocationRepo;

    private AllocationService allocationService;

    private Collaborator currentCollaborator;
    private Resource currentResource;
    private LocalDate allocationDate;
    private Allocation confirmedAllocation;
    private Exception exceptionTriggered;

    @Given("that resources and collaborators are registered in the system")
    public void that_resources_and_collaborators_are_registered_in_the_system() {
        collaboratorRepo = new InMemoryCollaboratorRepository();
        resourceRepo = new InMemoryResourceRepository();
        allocationRepo = new InMemoryAllocationRepository();

        allocationService = new AllocationService(allocationRepo);

        currentCollaborator = null;
        currentResource = null;
        allocationDate = null;
        confirmedAllocation = null;
        exceptionTriggered = null;
    }

    private void resetAttemptResult() {
        confirmedAllocation = null;
        exceptionTriggered = null;
    }

    @Given("that the collaborator {string} wants to allocate the notebook {string}")
    public void that_the_collaborator_wants_to_allocate_the_notebook(String collaboratorId, String resourceId) {
        currentCollaborator = collaboratorRepo.findById(collaboratorId).orElseThrow();
        currentResource = resourceRepo.findById(resourceId).orElseThrow();
    }

    @When("they request the reservation for the date {string}")
    public void they_request_the_reservation_for_the_date(String date) {
        resetAttemptResult();

        allocationDate = LocalDate.parse(date);

        try {
            confirmedAllocation = allocationService.allocateResource(
                    currentCollaborator,
                    currentResource,
                    allocationDate
            );
        } catch (Exception e) {
            exceptionTriggered = e;
        }
    }

    @Then("the system should successfully confirm the allocation")
    public void the_system_should_successfully_confirm_the_allocation() {
        assertNotNull(confirmedAllocation, "A alocação deveria ter sido confirmada.");
        assertNull(exceptionTriggered, "Nenhuma exceção deveria ter sido lançada.");
    }

    @Given("that the collaborator is not valid")
    public void that_the_collaborator_is_not_valid() {
        currentCollaborator = null;
        currentResource = resourceRepo.findById("CEPES1983").orElseThrow();
        allocationDate = LocalDate.now();
    }

    @When("they try to allocate a resource")
    public void they_try_to_allocate_a_resource() {
        resetAttemptResult();

        try {
            confirmedAllocation = allocationService.allocateResource(
                    currentCollaborator,
                    currentResource,
                    allocationDate
            );
        } catch (Exception e) {
            exceptionTriggered = e;
        }
    }

    @Then("the system should inform that it is not possible")
    public void the_system_should_inform_that_it_is_not_possible() {
        assertNull(confirmedAllocation, "A alocação não deveria ter sido confirmada.");
        assertNotNull(exceptionTriggered, "Uma exceção deveria ter sido lançada.");
    }

    @Given("that a valid collaborator has allocated a notebook")
    public void that_a_valid_collaborator_has_allocated_a_notebook() {
        currentCollaborator = collaboratorRepo.findById("10002020").orElseThrow();
        currentResource = resourceRepo.findById("CEPES1983").orElseThrow();
        allocationDate = LocalDate.now();

        allocationService.allocateResource(
                currentCollaborator,
                currentResource,
                allocationDate
        );

        resetAttemptResult();
    }

    @When("they try to allocate another notebook for the same day")
    public void they_try_to_allocate_another_notebook_for_the_same_day() {
        resetAttemptResult();

        Resource anotherNotebook = resourceRepo.findById("CEPES1984").orElseThrow();

        try {
            confirmedAllocation = allocationService.allocateResource(
                    currentCollaborator,
                    anotherNotebook,
                    allocationDate
            );
        } catch (Exception e) {
            exceptionTriggered = e;
        }
    }

    @When("another collaborator try to allocate the same notebook for the same day")
    public void another_collaborator_try_to_allocate_the_same_notebook_for_the_same_day() {
        resetAttemptResult();

        Collaborator anotherCollaborator = collaboratorRepo.findById("10002021").orElseThrow();

        try {
            confirmedAllocation = allocationService.allocateResource(
                    anotherCollaborator,
                    currentResource,
                    allocationDate
            );
        } catch (Exception e) {
            exceptionTriggered = e;
        }
    }

    @When("they try to allocate a room for the same day")
    public void they_try_to_allocate_a_room_for_the_same_day() {
        resetAttemptResult();

        Resource roomResource = resourceRepo.findById("ROOM101").orElseThrow();

        try {
            confirmedAllocation = allocationService.allocateResource(
                    currentCollaborator,
                    roomResource,
                    allocationDate
            );
        } catch (Exception e) {
            exceptionTriggered = e;
        }
    }

    @Given("that a valid collaborator has allocated a lab")
    public void that_a_valid_collaborator_has_allocated_a_lab() {
        currentCollaborator = collaboratorRepo.findById("10002022").orElseThrow();
        currentResource = resourceRepo.findById("LAB202").orElseThrow();
        allocationDate = LocalDate.now();

        allocationService.allocateResource(
                currentCollaborator,
                currentResource,
                allocationDate
        );

        resetAttemptResult();
    }

    @When("they try to allocate a notebook for the same day")
    public void they_try_to_allocate_a_notebook_for_the_same_day() {
        resetAttemptResult();

        Resource notebookResource = resourceRepo.findById("CEPES1984").orElseThrow();

        try {
            confirmedAllocation = allocationService.allocateResource(
                    currentCollaborator,
                    notebookResource,
                    allocationDate
            );
        } catch (Exception e) {
            exceptionTriggered = e;
        }
    }
}