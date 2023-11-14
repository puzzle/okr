package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.TestHelper.*;
import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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

        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals("Not authorized to read organisations", exception.getReason());
        verify(organisationBusinessService, never()).getActiveOrganisations();

    }
}
