package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.CheckInTestHelpers.checkInMetric;
import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class CheckInAuthorizationServiceTest {
    @InjectMocks
    private CheckInAuthorizationService checkInAuthorizationService;
    @Mock
    CheckInBusinessService checkInBusinessService;
    @Mock
    AuthorizationService authorizationService;

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Test
    void getEntityByIdShouldReturnCheckInWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(checkInBusinessService.getEntityById(id)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.getEntityById(id);
        assertEquals(checkInMetric, checkIn);
    }

    @Test
    void getEntityByIdShouldReturnCheckInWritableWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.isWriteable(checkInMetric, authorizationUser)).thenReturn(true);
        when(checkInBusinessService.getEntityById(id)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.getEntityById(id);
        assertTrue(checkIn.isWriteable());
    }

    @Test
    void getEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleReadByCheckInId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInAuthorizationService.getEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void createEntityShouldReturnCreatedCheckInWhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(checkInBusinessService.createEntity(checkInMetric, authorizationUser)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.createEntity(checkInMetric);
        assertEquals(checkInMetric, checkIn);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(checkInMetric, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInAuthorizationService.createEntity(checkInMetric));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntityShouldReturnUpdatedCheckInWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(checkInBusinessService.updateEntity(id, checkInMetric, authorizationUser)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.updateEntity(id, checkInMetric);
        assertEquals(checkInMetric, checkIn);
    }

    @Test
    void updateEntityShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(checkInMetric, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInAuthorizationService.updateEntity(id, checkInMetric));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void deleteEntityByIdShouldPassThroughWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        checkInAuthorizationService.deleteEntityById(id);
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByCheckInId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInAuthorizationService.deleteEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }
}
