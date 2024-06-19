package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.userWithoutWriteAllRole;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class OrganisationAuthorizationServiceTest {

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    private OrganisationAuthorizationService organizationAuthorizationService;
    @Mock
    private OrganisationBusinessService organisationBusinessService;

    @Test
    void getEntityIfAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        organizationAuthorizationService.getEntities();
        verify(organisationBusinessService, times(1)).getActiveOrganisations();
    }

    @Test
    void getErrorIfNotAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationAuthorizationService.getEntities());

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_READ", exception.getReason());
        verify(organisationBusinessService, never()).getActiveOrganisations();
    }

    @DisplayName("getEntitiesByTeam() should return entities if authorized")
    @Test
    void getEntitiesByTeamShouldReturnEntitiesIfAuthorized() {
        // arrange
        long teamId = 5L;
        Organisation organisation = Organisation.Builder.builder().withId(23L).build();

        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(organisationBusinessService.getOrganisationsByTeam(anyLong())).thenReturn(List.of(organisation));

        // act
        List<Organisation> organisations = organizationAuthorizationService.getEntitiesByTeam(teamId);

        // assert
        verify(organisationBusinessService, times(1)).getOrganisationsByTeam(teamId);
        assertEquals(1, organisations.size());
        assertEquals(organisation, organisations.get(0));
    }

    @DisplayName("getEntitiesByTeam() throws exception if not authorized")
    @Test
    void getEntitiesByTeamThrowsExceptionIfNotAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(userWithoutWriteAllRole());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> organizationAuthorizationService.getEntitiesByTeam(23L));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals("NOT_AUTHORIZED_TO_READ", exception.getReason());
        verify(organisationBusinessService, never()).getOrganisationsByTeam(anyLong());
    }
}
