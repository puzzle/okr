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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.KeyResultTestHelpers.checkIn1;
import static ch.puzzle.okr.KeyResultTestHelpers.metricKeyResult;
import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
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
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Test
    void createEntity_ShouldReturnKeyResult_WhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.createEntity(metricKeyResult, authorizationUser)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.createEntity(metricKeyResult);
        assertEquals(metricKeyResult, keyResult);
    }

    @Test
    void createEntity_ShouldThrowException_WhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(metricKeyResult, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.createEntity(metricKeyResult));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getEntityById_ShouldReturnKeyResult_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);
        assertEquals(metricKeyResult, keyResult);
    }

    @Test
    void getEntityById_ShouldReturnKeyResultWritable_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.isWriteable(metricKeyResult, authorizationUser)).thenReturn(true);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);
        assertTrue(keyResult.isWriteable());
    }

    @Test
    void getEntityById_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleReadByKeyResultId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.getEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntity_ShouldReturnUpdatedKeyResult_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.updateEntity(id, metricKeyResult, authorizationUser)).thenReturn(metricKeyResult);

        KeyResult KeyResult = keyResultAuthorizationService.updateEntity(id, metricKeyResult);
        assertEquals(metricKeyResult, KeyResult);
    }

    @Test
    void updateEntity_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(metricKeyResult, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.updateEntity(id, metricKeyResult));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void deleteEntityById_ShouldPassThrough_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        keyResultAuthorizationService.deleteEntityById(id);
    }

    @Test
    void deleteEntityById_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByKeyResultId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.deleteEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getAllCheckInsByKeyResult_ShouldReturnListOfCheckIns_WhenAuthorized() {
        long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getAllCheckInsByKeyResult(id)).thenReturn(List.of(checkIn1, checkIn1));

        List<CheckIn> checkIns = keyResultAuthorizationService.getAllCheckInsByKeyResult(id);
        assertThat(List.of(checkIn1, checkIn1)).hasSameElementsAs(checkIns);
    }

    @Test
    void isImUsed_ShouldReturnTrue_WhenImUsed() {
        Long id = 13L;
        when(keyResultBusinessService.isImUsed(id, metricKeyResult)).thenReturn(true);

        assertTrue(keyResultAuthorizationService.isImUsed(id, metricKeyResult));
    }
}
