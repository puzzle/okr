package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.OrganisationState;
import ch.puzzle.okr.repository.OrganisationRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        } catch (OkrResponseStatusException ex) {
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
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> organisationPersistenceService.findById(321L));

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("MODEL_WITH_ID_NOT_FOUND", List.of("Organisation", "321")));

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void shouldThrowExceptionWhenFindingOrganisationWithIdNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> organisationPersistenceService.findById(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Organisation")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
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

    @Test
    void getActiveOrganisations() {
        List<Organisation> organisations = organisationPersistenceService.getActiveOrganisations();
        organisations.forEach(organisation -> assertNotSame(OrganisationState.INACTIVE, organisation.getState()));
    }
}
