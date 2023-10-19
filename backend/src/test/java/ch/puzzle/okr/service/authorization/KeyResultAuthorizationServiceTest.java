package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.KeyResultTestHelpers.checkIn1;
import static ch.puzzle.okr.KeyResultTestHelpers.metricKeyResult;
import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class KeyResultAuthorizationServiceTest {
    @InjectMocks
    private KeyResultAuthorizationService keyResultAuthorizationService;
    @Mock
    KeyResultBusinessService keyResultBusinessService;
    @Mock
    AuthorizationService authorizationService;
    private final Jwt token = defaultJwtToken();
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Test
    void createKeyResult_ShouldReturnKeyResult_WhenAuthorized() {
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(keyResultBusinessService.createKeyResult(metricKeyResult, authorizationUser)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.createKeyResult(metricKeyResult, token);
        assertEquals(metricKeyResult, keyResult);
    }

    @Test
    void createKeyResult_ShouldThrowException_WhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(metricKeyResult, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.createKeyResult(metricKeyResult, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getKeyResultById_ShouldReturnKeyResult_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(keyResultBusinessService.getKeyResultById(id)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.getKeyResultById(id, token);
        assertEquals(metricKeyResult, keyResult);
    }

    @Test
    void getKeyResultById_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleReadByKeyResultId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.getKeyResultById(id, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateKeyResult_ShouldReturnUpdatedKeyResult_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(keyResultBusinessService.updateKeyResult(id, metricKeyResult)).thenReturn(metricKeyResult);

        KeyResult KeyResult = keyResultAuthorizationService.updateKeyResult(id, metricKeyResult, token);
        assertEquals(metricKeyResult, KeyResult);
    }

    @Test
    void updateKeyResult_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(metricKeyResult, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.updateKeyResult(id, metricKeyResult, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void deleteKeyResult_ShouldPassThrough_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);

        keyResultAuthorizationService.deleteKeyResultById(id, token);
    }

    @Test
    void deleteKeyResult_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByKeyResultId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.deleteKeyResultById(id, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getAllCheckInsByKeyResult_ShouldReturnListOfCheckIns_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(keyResultBusinessService.getAllCheckInsByKeyResult(id)).thenReturn(List.of(checkIn1, checkIn1));

        List<CheckIn> checkIns = keyResultAuthorizationService.getAllCheckInsByKeyResult(id, token);
        assertThat(List.of(checkIn1, checkIn1)).hasSameElementsAs(checkIns);
    }

    @Test
    void isImUsed_ShouldReturnTrue_WhenImUsed() {
        Long id = 13L;
        when(keyResultBusinessService.isImUsed(id, metricKeyResult)).thenReturn(true);

        assertTrue(keyResultAuthorizationService.isImUsed(id, metricKeyResult));
    }
}
