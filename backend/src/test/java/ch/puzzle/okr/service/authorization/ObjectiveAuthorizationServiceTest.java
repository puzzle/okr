package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.dto.AlignmentDto;
import ch.puzzle.okr.dto.AlignmentObjectDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static ch.puzzle.okr.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class ObjectiveAuthorizationServiceTest {
    @InjectMocks
    private ObjectiveAuthorizationService objectiveAuthorizationService;
    @Mock
    ObjectiveBusinessService objectiveBusinessService;
    @Mock
    AuthorizationService authorizationService;

    private static final String JUNIT_TEST_REASON = "junit test reason";

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    private final Objective newObjective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    private static final AlignmentObjectDto alignmentObject1 = new AlignmentObjectDto(3L, "KR Title 1", "keyResult");
    private static final AlignmentObjectDto alignmentObject2 = new AlignmentObjectDto(1L, "Objective Title 1",
            "objective");
    private static final AlignmentDto alignmentPossibilities = new AlignmentDto(1L, TEAM_PUZZLE,
            List.of(alignmentObject1, alignmentObject2));

    @Test
    void createEntityShouldReturnObjectiveWhenAuthorized() {
        // arrange
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.createEntity(newObjective, authorizationUser)).thenReturn(newObjective);

        // act
        Objective objective = objectiveAuthorizationService.createEntity(newObjective);

        // assert
        assertEquals(newObjective, objective);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        String reason = JUNIT_TEST_REASON;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(newObjective, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.createEntity(newObjective));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getEntityByIdShouldReturnObjectiveWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        // act
        Objective objective = objectiveAuthorizationService.getEntityById(id);

        // assert
        assertEquals(newObjective, objective);
    }

    @Test
    void getEntityByIdShouldReturnObjectiveWritableWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.hasRoleWriteForTeam(newObjective, authorizationUser)).thenReturn(true);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        // act
        Objective objective = objectiveAuthorizationService.getEntityById(id);

        // assert
        assertTrue(objective.isWriteable());
    }

    @Test
    void getEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        Long id = 13L;
        String reason = JUNIT_TEST_REASON;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleReadByObjectiveId(id, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.getEntityById(id));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntityShouldReturnUpdatedObjectiveWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.updateEntity(id, newObjective, authorizationUser)).thenReturn(newObjective);

        // act
        Objective Objective = objectiveAuthorizationService.updateEntity(id, newObjective);

        // assert
        assertEquals(newObjective, Objective);
    }

    @Test
    void updateEntityShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        Long id = 13L;
        String reason = JUNIT_TEST_REASON;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(newObjective, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.updateEntity(id, newObjective));

        // assert
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
        // arrange
        Long id = 13L;
        String reason = JUNIT_TEST_REASON;
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByObjectiveId(id, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.deleteEntityById(id));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getAlignmentPossibilitiesShouldReturnListWhenAuthorized() {
        // arrange
        when(objectiveBusinessService.getAlignmentPossibilities(anyLong())).thenReturn(List.of(alignmentPossibilities));

        // act
        List<AlignmentDto> alignmentPossibilities = objectiveAuthorizationService.getAlignmentPossibilities(3L);

        // assert
        assertEquals(TEAM_PUZZLE, alignmentPossibilities.get(0).teamName());
        assertEquals(1, alignmentPossibilities.get(0).teamId());
        assertEquals(3, alignmentPossibilities.get(0).alignmentObjects().get(0).objectId());
        assertEquals("KR Title 1", alignmentPossibilities.get(0).alignmentObjects().get(0).objectTitle());
        assertEquals("keyResult", alignmentPossibilities.get(0).alignmentObjects().get(0).objectType());
        assertEquals(1, alignmentPossibilities.get(0).alignmentObjects().get(1).objectId());
        assertEquals("objective", alignmentPossibilities.get(0).alignmentObjects().get(1).objectType());
    }

    @Test
    void getAlignmentPossibilitiesShouldReturnEmptyListWhenNoAlignments() {
        // arrange
        when(objectiveBusinessService.getAlignmentPossibilities(anyLong())).thenReturn(List.of());

        // act
        List<AlignmentDto> alignmentPossibilities = objectiveAuthorizationService.getAlignmentPossibilities(3L);

        // assert
        assertEquals(0, alignmentPossibilities.size());
    }

    @DisplayName("duplicateEntity() should throw exception when not authorized")
    @Test
    void duplicateEntityShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        Long idExistingObjective = 13L;
        String reason = "junit test reason";
        Objective objective = Objective.Builder.builder().build();

        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(objective, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.duplicateEntity(idExistingObjective, objective));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("duplicateEntity() should return duplicated Objective when authorized")
    @Test
    void duplicateEntityShouldReturnDuplicatedObjectiveWhenAuthorized() {
        // arrange
        Long idExistingObjective = 13L;

        Objective newObjectiveWithoutKeyResults = Objective.Builder.builder() //
                .withTitle("Objective without KeyResults").build();

        Objective newObjectiveWithKeyResults = Objective.Builder.builder() //
                .withId(42L).withTitle("Objective with Id and KeyResults").build();

        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.duplicateObjective(idExistingObjective, newObjectiveWithoutKeyResults,
                authorizationUser)).thenReturn(newObjectiveWithKeyResults);

        // act
        Objective objective = objectiveAuthorizationService.duplicateEntity(idExistingObjective,
                newObjectiveWithoutKeyResults);

        // assert
        assertEquals(newObjectiveWithKeyResults, objective);
    }
}
