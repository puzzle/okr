package ch.puzzle.okr.service.authorization;

import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.CompletedBusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
    private final Completed newCompleted = Completed.Builder
            .builder()
            .withId(5L)
            .withObjective(Objective.Builder.builder().withId(objectiveId).withTitle("Completed 1").build())
            .build();

    @DisplayName("Should return the created object when authorized")
    @Test
    void shouldReturnCreatedCompletedWhenAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        when(completedBusinessService.createCompleted(newCompleted)).thenReturn(newCompleted);

        Completed completed = completedAuthorizationService.createCompleted(newCompleted);
        assertEquals(newCompleted, completed);
    }

    @DisplayName("Should throw an exception when the user is not authorized to create completed object")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToCreateCompleted() {
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleCreateOrUpdateByObjectiveId(objectiveId, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> completedAuthorizationService
                                                                 .createCompleted(newCompleted));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }

    @DisplayName("Should successfully delete completed object by objective ID when authorized")
    @Test
    void shouldDeleteCompletedByObjectiveIdWhenAuthorized() {
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);

        completedAuthorizationService.deleteCompletedByObjectiveId(13L);
    }

    @DisplayName("Should throw an exception when the user is not authorized to delete completed object by objective ID")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedToDeleteCompletedByObjectiveId() {
        String reason = "junit test reason";
        when(authorizationService.updateOrAddAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason))
                .when(authorizationService)
                .hasRoleDeleteByObjectiveId(objectiveId, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> completedAuthorizationService
                                                                 .deleteCompletedByObjectiveId(objectiveId));
        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
    }
}
