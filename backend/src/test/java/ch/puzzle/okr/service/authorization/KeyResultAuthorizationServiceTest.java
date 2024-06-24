package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultWithActionList;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.KeyResultTestHelpers.*;
import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
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

    @Test
    void createEntityShouldReturnKeyResultWhenAuthorized() {
        // arrange
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.createEntity(metricKeyResult, authorizationUser)).thenReturn(metricKeyResult);

        // act
        KeyResult keyResult = keyResultAuthorizationService.createEntity(metricKeyResult);

        // assert
        assertEquals(metricKeyResult, keyResult);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(metricKeyResult, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.createEntity(metricKeyResult));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getEntityByIdShouldReturnKeyResultWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(metricKeyResult);

        // act
        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);

        // assert
        assertEquals(metricKeyResult, keyResult);
    }

    @Test
    void getEntityByIdShouldReturnKeyResultWritableWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.isWriteable(metricKeyResult, authorizationUser)).thenReturn(true);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(metricKeyResult);

        // act
        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);

        // assert
        assertTrue(keyResult.isWriteable());
    }

    @Test
    void getEntityByIdShouldReturnKeyResultNotWritableWhenArchived() {
        // arrange
        Long id = 1L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(archivedKeyResult);

        // act
        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);

        // assert
        assertFalse(keyResult.isWriteable());
    }

    @Test
    void getEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleReadByKeyResultId(id, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.getEntityById(id));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntitiesShouldReturnUpdatedKeyResultWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.updateEntities(id, metricKeyResult, List.of()))
                .thenReturn(new KeyResultWithActionList(metricKeyResult, List.of()));

        // act
        KeyResultWithActionList KeyResult = keyResultAuthorizationService.updateEntities(id, metricKeyResult,
                List.of());

        // assert
        assertEquals(metricKeyResult, KeyResult.keyResult());
    }

    @Test
    void updateEntitiesShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(metricKeyResult, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.updateEntities(id, metricKeyResult, List.of()));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntityShouldThrowException() {
        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.updateEntity(1L, metricKeyResult));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("unsupported method in class " + KeyResultAuthorizationService.class.getSimpleName()
                + ", use updateEntities() instead", exception.getReason());
    }

    @Test
    void deleteEntityByIdShouldPassThroughWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        // act
        keyResultAuthorizationService.deleteEntityById(id);
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByKeyResultId(id, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultAuthorizationService.deleteEntityById(id));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getAllCheckInsByKeyResultShouldReturnListOfCheckInsWhenAuthorized() {
        // arrange
        long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getAllCheckInsByKeyResult(id)).thenReturn(List.of(checkIn1, checkIn1));

        // act
        List<CheckIn> checkIns = keyResultAuthorizationService.getAllCheckInsByKeyResult(id);

        // assert
        verify(authorizationService, times(1)).isWriteable(checkIn1, authorizationUser);
        assertThat(List.of(checkIn1, checkIn1)).hasSameElementsAs(checkIns);
    }

    @Test
    void getAllCheckInsByKeyResultShouldReturnListOfCheckInsNotWritableWhenArchived() {
        // arrange
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getAllCheckInsByKeyResult(id)).thenReturn(List.of(archivedCheckin));

        // act
        List<CheckIn> checkIns = keyResultAuthorizationService.getAllCheckInsByKeyResult(id);

        // assert
        verify(authorizationService, times(0)).isWriteable(archivedCheckin, authorizationUser);
        assertThat(List.of(archivedCheckin)).hasSameElementsAs(checkIns);
        assertFalse(checkIns.get(0).isWriteable());
    }

    @Test
    void isImUsedShouldReturnTrueWhenImUsed() {
        // arrange
        Long id = 13L;
        when(keyResultBusinessService.isImUsed(id, metricKeyResult)).thenReturn(true);

        // assert
        assertTrue(keyResultAuthorizationService.isImUsed(id, metricKeyResult));
    }
}
