package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.repository.OrganisationRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringIntegrationTest
public class OrganisationPersistenceIT {

    @Autowired
    private OrganisationPersistenceService organisationPersistenceService;

    @Spy
    OrganisationRepository repository;

    Organisation org = Organisation.Builder.builder().withOrgName("org_test").withState(OrganisationState.ACTIVE)
            .build();
    @Autowired
    private OrganisationRepository organisationRepository;

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
        organisationPersistenceService.saveIfNotExists(org);
        assertTrue(organisationPersistenceService.findAll().contains(org));
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

}
