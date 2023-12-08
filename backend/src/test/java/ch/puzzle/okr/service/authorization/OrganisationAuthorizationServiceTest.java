package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.userWithoutWriteAllRole;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
