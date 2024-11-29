package ch.puzzle.okr.service.authorization;

import java.util.List;

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

    @Test
    void createEntityShouldReturnKeyResultWhenAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.createEntity(metricKeyResult, authorizationUser)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.createEntity(metricKeyResult);
        assertEquals(metricKeyResult, keyResult);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                                                                             .hasRoleCreateOrUpdate(metricKeyResult,
                                                                                                    authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService.createEntity(metricKeyResult));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getEntityByIdShouldReturnKeyResultWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);
        assertEquals(metricKeyResult, keyResult);
    }

    @Test
    void getEntityByIdShouldReturnKeyResultWritableWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.hasRoleWriteForTeam(metricKeyResult, authorizationUser)).thenReturn(true);
        when(keyResultBusinessService.getEntityById(id)).thenReturn(metricKeyResult);

        KeyResult keyResult = keyResultAuthorizationService.getEntityById(id);
        assertTrue(keyResult.isWriteable());
    }

    @Test
    void getEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                                                                             .hasRoleReadByKeyResultId(id,
                                                                                                       authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService.getEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntitiesShouldReturnUpdatedKeyResultWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.updateEntities(id, metricKeyResult, List.of())).thenReturn(
                                                                                                 new KeyResultWithActionList(metricKeyResult,
                                                                                                                             List.of()));

        KeyResultWithActionList KeyResult = keyResultAuthorizationService.updateEntities(id,
                                                                                         metricKeyResult,
                                                                                         List.of());
        assertEquals(metricKeyResult, KeyResult.keyResult());
    }

    @Test
    void updateEntitiesShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                                                                             .hasRoleCreateOrUpdate(metricKeyResult,
                                                                                                    authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService.updateEntities(id,
                                                                                                            metricKeyResult,
                                                                                                            List.of()));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntityShouldThrowException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService.updateEntity(1L,
                                                                                                          metricKeyResult));
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("unsupported method in class " + KeyResultAuthorizationService.class.getSimpleName() + ", use updateEntities() instead",
                     exception.getReason());
    }

    @Test
    void deleteEntityByIdShouldPassThroughWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);

        keyResultAuthorizationService.deleteEntityById(id);
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                                                                             .hasRoleDeleteByKeyResultId(id,
                                                                                                         authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> keyResultAuthorizationService.deleteEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getAllCheckInsByKeyResultShouldReturnListOfCheckInsWhenAuthorized() {
        long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getAllCheckInsByKeyResult(id)).thenReturn(List.of(checkIn1, checkIn1));

        List<CheckIn> checkIns = keyResultAuthorizationService.getAllCheckInsByKeyResult(id);
        assertThat(List.of(checkIn1, checkIn1)).hasSameElementsAs(checkIns);
    }

    @DisplayName("getAllCheckInsByKeyResult() should return empty List if no Checkins are found")
    @Test
    void getAllCheckInsByKeyResultShouldReturnEmptyListIfNoCheckinsAreFound() {
        long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(keyResultBusinessService.getAllCheckInsByKeyResult(id)).thenReturn(List.of());

        List<CheckIn> checkIns = keyResultAuthorizationService.getAllCheckInsByKeyResult(id);
        assertThat(List.of()).hasSameElementsAs(checkIns);
    }

    @Test
    void isImUsedShouldReturnTrueWhenImUsed() {
        Long id = 13L;
        when(keyResultBusinessService.isImUsed(id, metricKeyResult)).thenReturn(true);

        assertTrue(keyResultAuthorizationService.isImUsed(id, metricKeyResult));
    }
}
