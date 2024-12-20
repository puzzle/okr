package ch.puzzle.okr.service.authorization;

import static ch.puzzle.okr.test.KeyResultTestHelpers.metricKeyResult;
import static ch.puzzle.okr.test.KeyResultTestHelpers.ordinalKeyResult;
import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ObjectiveAuthorizationServiceTest {
    @InjectMocks
    private ObjectiveAuthorizationService objectiveAuthorizationService;
    @Mock
    ObjectiveBusinessService objectiveBusinessService;
    @Mock
    AuthorizationService authorizationService;
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    private final Objective newObjective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();

    @Test
    void createEntityShouldReturnObjectiveWhenAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.createEntity(newObjective, authorizationUser)).thenReturn(newObjective);

        Objective objective = objectiveAuthorizationService.createEntity(newObjective);
        assertEquals(newObjective, objective);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdate(newObjective, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> objectiveAuthorizationService
                                                                 .createEntity(newObjective));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getEntityByIdShouldReturnObjectiveWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        Objective objective = objectiveAuthorizationService.getEntityById(id);
        assertEquals(newObjective, objective);
    }

    @Test
    void getEntityByIdShouldReturnObjectiveWritableWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.hasRoleWriteForTeam(newObjective, authorizationUser)).thenReturn(true);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        Objective objective = objectiveAuthorizationService.getEntityById(id);
        assertTrue(objective.isWriteable());
    }

    @Test
    void getEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleReadByObjectiveId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> objectiveAuthorizationService.getEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntityShouldReturnUpdatedObjectiveWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.updateEntity(id, newObjective, authorizationUser)).thenReturn(newObjective);

        Objective Objective = objectiveAuthorizationService.updateEntity(id, newObjective);
        assertEquals(newObjective, Objective);
    }

    @Test
    void updateEntityShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdate(newObjective, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> objectiveAuthorizationService
                                                                 .updateEntity(id, newObjective));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void isImUsedShouldReturnTrueWhenQuarterChanged() {
        when(objectiveBusinessService.isImUsed(newObjective)).thenReturn(true);

        assertTrue(objectiveAuthorizationService.isImUsed(newObjective));
    }

    @Test
    void deleteEntityByIdShouldPassThroughWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);

        objectiveAuthorizationService.deleteEntityById(id);
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleDeleteByObjectiveId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> objectiveAuthorizationService.deleteEntityById(id));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("duplicateEntity() should throw exception when not authorized")
    @Test
    void duplicateEntityShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        Long idExistingObjective = 13L;
        String reason = "junit test reason";
        Objective objective = Objective.Builder.builder().build();

        List<KeyResult> keyResults = new ArrayList<>();
        keyResults.add(metricKeyResult);

        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdate(objective, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> objectiveAuthorizationService
                                                                 .duplicateEntity(idExistingObjective,
                                                                                  objective,
                                                                                  keyResults));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("duplicateEntity() should return duplicated Objective when authorized")
    @Test
    void duplicateEntityShouldReturnDuplicatedObjectiveWhenAuthorized() {
        // arrange
        Long idExistingObjective = 13L;

        Objective newObjectiveWithoutKeyResults = Objective.Builder
                .builder() //
                .withTitle("Objective without KeyResults")
                .build();

        Objective newObjectiveWithKeyResults = Objective.Builder
                .builder() //
                .withId(42L)
                .withTitle("Objective with Id and KeyResults")
                .build();

        List<KeyResult> keyResults = new ArrayList<>();
        keyResults.add(metricKeyResult);

        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService
                .duplicateObjective(idExistingObjective, newObjectiveWithoutKeyResults, authorizationUser, keyResults))
                .thenReturn(newObjectiveWithKeyResults);

        // act
        Objective objective = objectiveAuthorizationService
                .duplicateEntity(idExistingObjective, newObjectiveWithoutKeyResults, keyResults);

        // assert
        assertEquals(newObjectiveWithKeyResults, objective);
    }

    @Test
    void getAllKeyResultsByObjectiveIdShouldReturnCorrectKeyResults() {
        Long id = 13L;
        when(objectiveBusinessService.getAllKeyResultsByObjective(id))
                .thenReturn(List.of(metricKeyResult, ordinalKeyResult));

        List<KeyResult> keyResults = objectiveAuthorizationService.getAllKeyResultsByObjective(id);
        assertEquals(keyResults, List.of(metricKeyResult, ordinalKeyResult));
    }
}
