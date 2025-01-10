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

    @DisplayName("Should return the created objective when authorized")
    @Test
    void shouldReturnCreatedObjectiveWhenAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.createEntity(newObjective, authorizationUser)).thenReturn(newObjective);

        Objective objective = objectiveAuthorizationService.createEntity(newObjective);
        assertEquals(newObjective, objective);
    }

    @DisplayName("Should throw an exception when the user is not authorized to create an objective")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToCreateObjective() {
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

    @DisplayName("Should return an objective when authorized")
    @Test
    void shouldReturnObjectiveByIdWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        Objective objective = objectiveAuthorizationService.getEntityById(id);
        assertEquals(newObjective, objective);
    }

    @DisplayName("Should return a writable objective when authorized")
    @Test
    void shouldReturnWritableObjectiveByIdWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.hasRoleWriteForTeam(newObjective, authorizationUser)).thenReturn(true);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        Objective objective = objectiveAuthorizationService.getEntityById(id);
        assertTrue(objective.isWriteable());
    }

    @DisplayName("Should throw an exception when the user is not authorized to get an objective by ID")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToGetObjectiveById() {
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

    @DisplayName("Should return the updated objective when authorized")
    @Test
    void shouldReturnUpdatedObjectiveWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.updateEntity(id, newObjective, authorizationUser)).thenReturn(newObjective);

        Objective objective = objectiveAuthorizationService.updateEntity(id, newObjective);
        assertEquals(newObjective, objective);
    }

    @DisplayName("Should throw an exception when the user is not authorized to update an objective")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToUpdateObjective() {
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

    @DisplayName("Should return true when the quarter has changed for an objective")
    @Test
    void shouldReturnTrueWhenQuarterHasChangedForObjective() {
        when(objectiveBusinessService.isImUsed(newObjective)).thenReturn(true);

        assertTrue(objectiveAuthorizationService.isImUsed(newObjective));
    }

    @DisplayName("Should successfully delete an objective by ID when authorized")
    @Test
    void shouldDeleteObjectiveByIdWhenAuthorized() {
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        assertDoesNotThrow(() -> objectiveAuthorizationService.deleteEntityById(id));
    }

    @DisplayName("Should throw an exception when the user is not authorized to delete an objective by ID")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToDeleteObjectiveById() {
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

    @DisplayName("Should throw an exception when not authorized to duplicate an objective")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToDuplicateObjective() {
        Long idExistingObjective = 13L;
        String reason = "junit test reason";
        Objective objective = Objective.Builder.builder().build();

        List<KeyResult> keyResults = new ArrayList<>();
        keyResults.add(metricKeyResult);

        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdate(objective, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> objectiveAuthorizationService
                                                                 .duplicateEntity(idExistingObjective,
                                                                                  objective,
                                                                                  keyResults));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should return duplicated objective when authorized")
    @Test
    void shouldReturnDuplicatedObjectiveWhenAuthorized() {
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

        Objective objective = objectiveAuthorizationService
                .duplicateEntity(idExistingObjective, newObjectiveWithoutKeyResults, keyResults);

        assertEquals(newObjectiveWithKeyResults, objective);
    }

    @DisplayName("Should return all corresponding key-results for an objective by ID")
    @Test
    void shouldReturnAllKeyResultsForObjectiveById() {
        Long id = 13L;
        when(objectiveBusinessService.getAllKeyResultsByObjective(id))
                .thenReturn(List.of(metricKeyResult, ordinalKeyResult));

        List<KeyResult> keyResults = objectiveAuthorizationService.getAllKeyResultsByObjective(id);
        assertEquals(keyResults, List.of(metricKeyResult, ordinalKeyResult));
    }
}
