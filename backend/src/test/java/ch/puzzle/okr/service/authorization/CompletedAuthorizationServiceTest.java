package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.CompletedBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class CompletedAuthorizationServiceTest {
    @InjectMocks
    private CompletedAuthorizationService completedAuthorizationService;
    @Mock
    CompletedBusinessService completedBusinessService;
    @Mock
    AuthorizationService authorizationService;
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    private final Long objectiveId = 12L;
    private final Completed newCompleted = Completed.Builder.builder().withId(5L)
            .withObjective(Objective.Builder.builder().withId(objectiveId).withTitle("Completed 1").build()).build();

    @Test
    void createCompletedShouldReturnObjectiveWhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(completedBusinessService.createCompleted(newCompleted)).thenReturn(newCompleted);

        Completed completed = completedAuthorizationService.createCompleted(newCompleted);
        assertEquals(newCompleted, completed);
    }

    @Test
    void createCompletedShouldThrowExceptionWhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdateByObjectiveId(objectiveId, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> completedAuthorizationService.createCompleted(newCompleted));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void deleteCompletedByObjectiveIdShouldPassThroughWhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        completedAuthorizationService.deleteCompletedByObjectiveId(13L);
    }

    @Test
    void deleteCompletedByObjectiveIdShouldThrowExceptionWhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByObjectiveId(objectiveId, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> completedAuthorizationService.deleteCompletedByObjectiveId(objectiveId));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }
}
