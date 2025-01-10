package ch.puzzle.okr.service.authorization;

import static ch.puzzle.okr.test.CheckInTestHelpers.checkInMetric;
import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CheckInAuthorizationServiceTest {
    @InjectMocks
    private CheckInAuthorizationService checkInAuthorizationService;
    @Mock
    CheckInBusinessService checkInBusinessService;
    @Mock
    AuthorizationService authorizationService;

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @DisplayName("Should return a check-in when authorized")
    @Test
    void shouldReturnCheckInWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(checkInBusinessService.getEntityById(id)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.getEntityById(id);
        assertEquals(checkInMetric, checkIn);
    }

    @DisplayName("Should return a writeable check-in when authorized")
    @Test
    void shouldReturnCheckInWritableWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.hasRoleWriteForTeam(checkInMetric, authorizationUser)).thenReturn(true);
        when(checkInBusinessService.getEntityById(id)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.getEntityById(id);
        assertTrue(checkIn.isWriteable());
    }

    @DisplayName("Should throw an exception when the user is not authorized")
    @Test
    void shouldThrowExceptionWhenUserNotAuthorizedForEntityById() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleReadByCheckInId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> checkInAuthorizationService.getEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should return a created check-in when authorized")
    @Test
    void shouldReturnCreatedCheckInWhenAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(checkInBusinessService.createEntity(checkInMetric, authorizationUser)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.createEntity(checkInMetric);
        assertEquals(checkInMetric, checkIn);
    }

    @DisplayName("Should throw an exception when the user is not authorized to create entity")
    @Test
    void shouldThrowExceptionWhenUserNotAuthorizedToCreateEntity() {
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdate(checkInMetric, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> checkInAuthorizationService.createEntity(checkInMetric));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should return an updated check-in when authorized")
    @Test
    void shouldReturnUpdatedCheckInWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(checkInBusinessService.updateEntity(id, checkInMetric, authorizationUser)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.updateEntity(id, checkInMetric);
        assertEquals(checkInMetric, checkIn);
    }

    @DisplayName("Should throw an exception when the user is not authorized to update entity")
    @Test
    void shouldThrowExceptionWhenUserNotAuthorizedToUpdateEntity() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdate(checkInMetric, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> checkInAuthorizationService
                                                                 .updateEntity(id, checkInMetric));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should successfully delete entity by ID when authorized")
    @Test
    void shouldDeleteEntityByIdWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);

        assertDoesNotThrow(() -> checkInAuthorizationService.deleteEntityById(id));
    }

    @DisplayName("Should throw an exception when the user is not authorized to delete entity by ID")
    @Test
    void shouldThrowExceptionWhenUserNotAuthorizedToDeleteEntityById() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleDeleteByCheckInId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> checkInAuthorizationService.deleteEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }
}
