package cepes.itacademy.sarc.service;

import cepes.itacademy.sarc.domain.*;
import cepes.itacademy.sarc.repository.*;
import cepes.itacademy.sarc.service.AllocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AllocationServiceTest {

    private CollaboratorRepository collaboratorRepo;
    private ResourceRepository resourceRepo;
    private AllocationRepository allocationRepo;
    private AllocationService allocationService;


    @BeforeEach
    void setUp() {
        collaboratorRepo = new InMemoryCollaboratorRepository();
        resourceRepo = new InMemoryResourceRepository();

        allocationRepo = new InMemoryAllocationRepository();

        allocationService = new AllocationService(allocationRepo);
    }

    @Test
    void shouldSuccessfullyAllocateAnAvailableResource() {
        Collaborator collaborator = collaboratorRepo.findById("10002020").orElseThrow();
        Resource notebook = resourceRepo.findById("CEPES1983").orElseThrow();
        LocalDate allocationDate = LocalDate.of(2026, 6, 12);

        Allocation result = allocationService.allocateResource(collaborator, notebook, allocationDate);

        assertNotNull(result, "The allocation should not be null");
        assertEquals(collaborator, result.getCollaborator(), "The collaborator should match Andre Oliveira");
        assertEquals(notebook, result.getResource(), "The resource should match Notebook CEPES1983");
        assertEquals(allocationDate, result.getDate(), "The date should match 2026-06-12");

        assertTrue(allocationRepo.existsByResourceAndDate(notebook, allocationDate),
                "The allocation should be persisted in the repository");
    }

    @Test
    void shouldThrowExceptionWhenAllocatingAnAlreadyAllocatedResource() {
        Collaborator collaborator1 = collaboratorRepo.findById("10002020").orElseThrow();
        Collaborator collaborator2 = collaboratorRepo.findById("10002021").orElseThrow();
        Resource notebook = resourceRepo.findById("CEPES1983").orElseThrow();
        LocalDate allocationDate = LocalDate.of(2026, 6, 12);

        allocationService.allocateResource(collaborator1, notebook, allocationDate);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                allocationService.allocateResource(collaborator2, notebook, allocationDate));

        assertEquals("Resource already allocated for this date.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAllocatingTwoNotebooksToTheSameCollaboratorOnTheSameDate() {
        Collaborator collaborator = collaboratorRepo.findById("10002020").orElseThrow();
        Resource notebook1 = resourceRepo.findById("CEPES1983").orElseThrow();
        Resource notebook2 = resourceRepo.findById("CEPES1984").orElseThrow();
        LocalDate allocationDate = LocalDate.of(2026, 6, 12);

        allocationService.allocateResource(collaborator, notebook1, allocationDate);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                allocationService.allocateResource(collaborator, notebook2, allocationDate));

        assertEquals("Collaborator cannot allocate this resource combination on the same date.", exception.getMessage());
    }

    @Test 
    void shouldThrowExceptionWhenAllocatingTwoProjectorsToTheSameCollaboratorOnTheSameDate() {
        Collaborator collaborator = collaboratorRepo.findById("10002020").orElseThrow();
        Resource projector1 = resourceRepo.findById("CEPES1995").orElseThrow();
        Resource projector2 = resourceRepo.findById("CEPES1983").orElseThrow();
        LocalDate allocationDate = LocalDate.of(2026, 6, 12);

        allocationService.allocateResource(collaborator, projector1, allocationDate);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                allocationService.allocateResource(collaborator, projector2, allocationDate));

        assertEquals("Collaborator cannot allocate this resource combination on the same date.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAllocatingTwoRoomsToTheSameCollaboratorOnTheSameDate() {
        Collaborator collaborator = collaboratorRepo.findById("10002020").orElseThrow();
        Resource room1 = resourceRepo.findById("ROOM101").orElseThrow();
        Resource room2 = resourceRepo.findById("512").orElseThrow();
        LocalDate allocationDate = LocalDate.of(2026, 6, 12);

        allocationService.allocateResource(collaborator, room1, allocationDate);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                allocationService.allocateResource(collaborator, room2, allocationDate));

        assertEquals("Collaborator cannot allocate this resource combination on the same date.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAllocatingTwoLabsToTheSameCollaboratorOnTheSameDate() {
        Collaborator collaborator = collaboratorRepo.findById("10002020").orElseThrow();
        Resource lab1 = resourceRepo.findById("411").orElseThrow();
        Resource lab2 = resourceRepo.findById("312").orElseThrow();
        LocalDate allocationDate = LocalDate.of(2026, 6, 12);

        allocationService.allocateResource(collaborator, lab1, allocationDate);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                allocationService.allocateResource(collaborator, lab2, allocationDate));

        assertEquals("Collaborator cannot allocate this resource combination on the same date.", exception.getMessage());
    }

    @Test 
    void shouldSuccessfullyAllocateDifferentResourceTypesToTheSameCollaboratorOnTheSameDate() {
        Collaborator collaborator = collaboratorRepo.findById("10002020").orElseThrow();
        Resource notebook = resourceRepo.findById("CEPES1983").orElseThrow();
        Resource room = resourceRepo.findById("ROOM101").orElseThrow();
        LocalDate allocationDate = LocalDate.of(2026, 6, 12);

        Allocation allocation1 = allocationService.allocateResource(collaborator, notebook, allocationDate);
        Allocation allocation2 = allocationService.allocateResource(collaborator, room, allocationDate);

        assertNotNull(allocation1, "The first allocation should not be null");
        assertNotNull(allocation2, "The second allocation should not be null");
    }
}