package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
    private final Objective archivedObjective = Objective.Builder.builder().withId(12L).withTitle("Objective 12")
            .withArchived(true).build();

    @Test
    void createEntityShouldReturnObjectiveWhenAuthorized() {
        // arrange
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.createEntity(newObjective, authorizationUser)).thenReturn(newObjective);

        // act
        Objective objective = objectiveAuthorizationService.createEntity(newObjective);

        // assert
        assertEquals(newObjective, objective);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
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
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        // act
        Objective objective = objectiveAuthorizationService.getEntityById(id);

        // assert
        assertEquals(newObjective, objective);
    }

    @Test
    void getEntityByIdShouldReturnObjectiveWritable() {
        // arrange
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.isWriteable(newObjective, authorizationUser)).thenReturn(true);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        // act
        Objective objective = objectiveAuthorizationService.getEntityById(id);

        // assert
        verify(authorizationService, times(1)).isWriteable(newObjective, authorizationUser);
        assertTrue(objective.isWriteable());
    }

    @Test
    void getEntityByIdShouldReturnObjectiveNotWritableWhenArchived() {
        // arrange
        Long id = 12L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(archivedObjective);

        // act
        Objective objective = objectiveAuthorizationService.getEntityById(id);

        // assert
        verify(authorizationService, times(0)).isWriteable(archivedObjective, authorizationUser);
        assertFalse(objective.isWriteable());
    }

    @Test
    void getEntityByIdShouldReturnObjectiveWritableWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(authorizationService.isWriteable(newObjective, authorizationUser)).thenReturn(true);
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
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
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
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
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
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
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
        // arrange
        when(objectiveBusinessService.isImUsed(newObjective)).thenReturn(true);

        // assert
        assertTrue(objectiveAuthorizationService.isImUsed(newObjective));
    }

    @Test
    void deleteEntityByIdShouldPassThroughWhenAuthorized() {
        // arrange
        Long id = 13L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        // act
        objectiveAuthorizationService.deleteEntityById(id);
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenNotAuthorized() {
        // arrange
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByObjectiveId(id, authorizationUser);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.deleteEntityById(id));

        // assert
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }
}
