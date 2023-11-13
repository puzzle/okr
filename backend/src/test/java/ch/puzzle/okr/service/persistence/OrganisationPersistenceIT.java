package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.repository.OrganisationRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringIntegrationTest
class OrganisationPersistenceIT {

    @Autowired
    private OrganisationPersistenceService organisationPersistenceService;

    @Spy
    OrganisationRepository repository;

    private Organisation createdOrganisation;

    private Organisation createOrganisation(Long id) {
        return createOrganisation(id, 0);
    }

    private Organisation createOrganisation(Long id, int version) {
        return Organisation.Builder.builder().withId(id).withVersion(version).withOrgName("org_test")
                .withState(OrganisationState.ACTIVE).build();
    }

    @AfterEach
    void tearDown() {
        try {
            if (createdOrganisation != null) {
                organisationPersistenceService.findById(createdOrganisation.getId());
                organisationPersistenceService.deleteById(createdOrganisation.getId());
            }
        } catch (ResponseStatusException ex) {
            // created alignment already deleted
        } finally {
            createdOrganisation = null;
        }
    }

    @Test
    void shouldReturnSingleOrganisationWhenFindingByValidId() {
        Organisation returnedOrganisation = organisationPersistenceService.findById(1L);
        assertEquals(1L, returnedOrganisation.getId());
        assertEquals("org_gl", returnedOrganisation.getOrgName());
    }

    @Test
    void shouldThrowExceptionWhenFindingOrganisationNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organisationPersistenceService.findById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Organisation with id 321 not found", exception.getReason());
    }

    @Test
    void shouldThrowExceptionWhenFindingOrganisationWithIdNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organisationPersistenceService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing identifier for Organisation", exception.getReason());
    }

    @Test
    void shouldReturnTrueIfOrgWithNameAlreadyExists() {
        Organisation org = createOrganisation(null);
        createdOrganisation = organisationPersistenceService.saveIfNotExists(org);
        assertTrue(organisationPersistenceService.findAll().contains(org));
    }

    @Test
    void updateOrganisationShouldSaveOrganisation() {
        createdOrganisation = organisationPersistenceService.saveIfNotExists(createOrganisation(null));
        Organisation updateOrganisation = createOrganisation(createdOrganisation.getId());
        updateOrganisation.setOrgName("Updated organisation");

        Organisation updatedOrganisation = organisationPersistenceService.save(updateOrganisation);

        assertEquals(createdOrganisation.getId(), updatedOrganisation.getId());
        assertEquals(createdOrganisation.getVersion() + 1, updatedOrganisation.getVersion());
        assertEquals(updateOrganisation.getOrgName(), updatedOrganisation.getOrgName());
    }

    @Test
    void shouldNotSaveOrganisationIfAlreadyExists() {
        Organisation existingOrganisation = organisationPersistenceService.findById(1L);
        organisationPersistenceService.saveIfNotExists(existingOrganisation);
        assertTrue(organisationPersistenceService.getRepository()
                .existsOrganisationByOrgName(existingOrganisation.getOrgName()));
        verify(repository, times(0)).save(existingOrganisation);
    }

    @Test
    void shouldSetOrgInactive() {
        Organisation organisation = organisationPersistenceService.findById(5L);
        organisation.setState(OrganisationState.ACTIVE);
        assertEquals(OrganisationState.ACTIVE, organisation.getState());
        organisationPersistenceService
                .updateOrganisationStateToInactive(organisationPersistenceService.findById(5L).getOrgName());
        assertEquals(OrganisationState.INACTIVE, organisationPersistenceService.findById(5L).getState());
    }

    @Test
    void getOrganisationByTeamId() {
        assertEquals(List.of(1L),
                organisationPersistenceService.getOrganisationsByTeamId(5L).stream().map(Organisation::getId).toList());
    }

    @Test
    void getEmptyListIfNoTeamWithIdFound() {
        assertEquals(List.of(), organisationPersistenceService.getOrganisationsByTeamId(90L).stream()
                .map(Organisation::getId).toList());
    }
}
