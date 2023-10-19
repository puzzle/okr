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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.CheckInTestHelpers.checkInMetric;
import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private final Jwt token = defaultJwtToken();
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Test
    void getCheckInById_ShouldReturnCheckIn_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(checkInBusinessService.getCheckInById(id)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.getCheckInById(id, token);
        assertEquals(checkInMetric, checkIn);
    }

    @Test
    void getCheckInById_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleReadByCheckInId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInAuthorizationService.getCheckInById(id, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void createCheckIn_ShouldReturnCreatedCheckIn_WhenAuthorized() {
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(checkInBusinessService.createCheckIn(checkInMetric, authorizationUser)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.createCheckIn(checkInMetric, token);
        assertEquals(checkInMetric, checkIn);
    }

    @Test
    void createCheckIn_ShouldThrowException_WhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(checkInMetric, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInAuthorizationService.createCheckIn(checkInMetric, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateCheckIn_ShouldReturnUpdatedCheckIn_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(checkInBusinessService.updateCheckIn(id, checkInMetric)).thenReturn(checkInMetric);

        CheckIn checkIn = checkInAuthorizationService.updateCheckIn(id, checkInMetric, token);
        assertEquals(checkInMetric, checkIn);
    }

    @Test
    void updateCheckIn_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(checkInMetric, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInAuthorizationService.updateCheckIn(id, checkInMetric, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void deleteCheckInById_ShouldPassThrough_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);

        checkInAuthorizationService.deleteCheckInById(id, token);
    }

    @Test
    void deleteCheckInById_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByCheckInId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> checkInAuthorizationService.deleteCheckInById(id, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }
}
