package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringIntegrationTest
public class OrganisationPersistenceIT {

    @Autowired
    private OrganisationPersistenceService organisationPersistenceService;

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

}
