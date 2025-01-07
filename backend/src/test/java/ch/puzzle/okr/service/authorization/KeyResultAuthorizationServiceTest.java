package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultWithActionList;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.test.KeyResultTestHelpers.checkIn1;
import static ch.puzzle.okr.test.KeyResultTestHelpers.metricKeyResult;
import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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

    @DisplayName("Should return the created key-result when authorized")
    @Test
    void shouldReturnKeyResultWhenAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.createEntity(metricKeyResult, authorizationUser)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.createEntity(metricKeyResult);
        assertEquals(metricKeyResult, keyResult);
    }

    @DisplayName("Should throw an exception when the user is not authorized to create key-result")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToCreateKeyResult() {
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdate(metricKeyResult, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService
                                                                 .createEntity(metricKeyResult));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should return a key-result when authorized")
    @Test
    void shouldReturnKeyResultByIdWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);
        assertEquals(metricKeyResult, keyResult);
    }

    @DisplayName("Should return a writable key-result when authorized")
    @Test
    void shouldReturnWritableKeyResultWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.hasRoleWriteForTeam(metricKeyResult, authorizationUser)).thenReturn(true);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);
        assertTrue(keyResult.isWriteable());
    }

    @DisplayName("Should throw an exception when the user is not authorized to get key-result by ID")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToGetKeyResultById() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleReadByKeyResultId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService.getEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should return the updated key-result when authorized")
    @Test
    void shouldReturnUpdatedKeyResultWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.updateEntities(id, metricKeyResult, List.of()))
                .thenReturn(new KeyResultWithActionList(metricKeyResult, List.of()));

        KeyResultWithActionList keyResult = keyResultAuthorizationService
                .updateEntities(id, metricKeyResult, List.of());
        assertEquals(metricKeyResult, keyResult.keyResult());
    }

    @DisplayName("Should throw an exception when the user is not authorized to update key-result")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToUpdateKeyResult() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdate(metricKeyResult, authorizationUser);
        List<Action> emptyActionList = List.of();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService
                                                                 .updateEntities(id, metricKeyResult, emptyActionList));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should throw an exception for unsupported updateEntity method")
    @Test
    void shouldThrowExceptionForUnsupportedUpdateEntityMethod() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService
                                                                 .updateEntity(1L, metricKeyResult));
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("unsupported method in class " + KeyResultAuthorizationService.class.getSimpleName()
                     + ", use updateEntities() instead",
                     exception.getReason());
    }

    @DisplayName("Should successfully delete key-result by ID when authorized")
    @Test
    void shouldDeleteKeyResultByIdWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        assertDoesNotThrow(() -> keyResultAuthorizationService.deleteEntityById(id));
    }

    @DisplayName("Should throw an exception when the user is not authorized to delete key-result by ID")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToDeleteKeyResultById() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleDeleteByKeyResultId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService.deleteEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should return a list of check-ins by key-result when authorized")
    @Test
    void shouldReturnListOfCheckInsByKeyResultWhenAuthorized() {
        long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getAllCheckInsByKeyResult(id)).thenReturn(List.of(checkIn1, checkIn1));

        List<CheckIn> checkIns = keyResultAuthorizationService.getAllCheckInsByKeyResult(id);
        assertThat(List.of(checkIn1, checkIn1)).hasSameElementsAs(checkIns);
    }

    @DisplayName("Should return an empty list if no check-ins are found by key-result")
    @Test
    void shouldReturnEmptyListIfNoCheckInsAreFoundByKeyResult() {
        long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getAllCheckInsByKeyResult(id)).thenReturn(List.of());

        List<CheckIn> checkIns = keyResultAuthorizationService.getAllCheckInsByKeyResult(id);
        assertThat(List.of()).hasSameElementsAs(checkIns);
    }

    @DisplayName("Should return true when the key-result is in use")
    @Test
    void shouldReturnTrueWhenKeyResultIsInUse() {
        Long id = 13L;
        when(keyResultBusinessService.isImUsed(id, metricKeyResult)).thenReturn(true);

        assertTrue(keyResultAuthorizationService.isImUsed(id, metricKeyResult));
    }
}
