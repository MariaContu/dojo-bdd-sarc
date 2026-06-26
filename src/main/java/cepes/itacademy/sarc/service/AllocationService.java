package cepes.itacademy.sarc.service;

import cepes.itacademy.sarc.domain.*;
import cepes.itacademy.sarc.repository.AllocationRepository;

import java.time.LocalDate;
import java.util.List;

public class AllocationService {

    private final AllocationRepository repository;

    public AllocationService(AllocationRepository repository) {
        this.repository = repository;
    }

    public Allocation allocateResource(Collaborator collaborator, Resource resource, LocalDate date) {
        validateRequiredFields(collaborator, resource, date);

        validateResourceAvailability(resource, date);
        validateCollaboratorAvailability(collaborator, resource, date);

        Allocation newAllocation = new Allocation(collaborator, resource, date);
        repository.saveAllocation(newAllocation);

        return newAllocation;
    }

    private void validateRequiredFields(Collaborator collaborator, Resource resource, LocalDate date) {
        if (collaborator == null) {
            throw new IllegalArgumentException("Collaborator is required.");
        }

        if (resource == null) {
            throw new IllegalArgumentException("Resource is required.");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date is required.");
        }
    }

    private void validateResourceAvailability(Resource resource, LocalDate date) {
        if (repository.existsByResourceAndDate(resource, date)) {
            throw new IllegalStateException("Resource already allocated for this date.");
        }
    }

    private void validateCollaboratorAvailability(
            Collaborator collaborator,
            Resource newResource,
            LocalDate date
    ) {
        List<Allocation> collaboratorAllocations =
                repository.findByCollaboratorIdAndDate(collaborator, date);

        for (Allocation allocation : collaboratorAllocations) {
            Resource alreadyAllocatedResource = allocation.getResource();

            if (isInvalidCombination(alreadyAllocatedResource, newResource)) {
                throw new IllegalStateException(
                        "Collaborator cannot allocate this resource combination on the same date."
                );
            }
        }
    }

    private boolean isInvalidCombination(Resource alreadyAllocatedResource, Resource newResource) {
        String alreadyType = alreadyAllocatedResource.getClass().getSimpleName();
        String newType = newResource.getClass().getSimpleName();

        boolean twoNotebooks =
                alreadyType.equalsIgnoreCase("notebook")
                        && newType.equalsIgnoreCase("notebook");

        boolean labAndNotebook =
                alreadyType.equalsIgnoreCase("lab")
                        && newType.equalsIgnoreCase("notebook");

        boolean labAndRoom =
                alreadyType.equalsIgnoreCase("lab")
                        && newType.equalsIgnoreCase("room");

        return twoNotebooks || labAndNotebook || labAndRoom;
    }
}