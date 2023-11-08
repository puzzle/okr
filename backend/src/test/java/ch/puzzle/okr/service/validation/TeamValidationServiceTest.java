package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamValidationServiceTest {
    @MockBean
    TeamPersistenceService teamPersistenceService = Mockito.mock(TeamPersistenceService.class);

    Team team1;
    Team team2;
    Team teamWithIdNull;

    @BeforeEach
    void setUp() {
        team1 = Team.Builder.builder().withId(1L).withName("Team 1").build();
        team2 = Team.Builder.builder().withId(2L).withName("Team 2").build();
        teamWithIdNull = Team.Builder.builder().withId(null).withName("Team null").build();

        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        when(teamPersistenceService.getModelName()).thenReturn("Team");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", teamPersistenceService.getModelName(), 2L)))
                        .when(teamPersistenceService).findById(2L);
    }

    @Spy
    @InjectMocks
    private TeamValidationService validator;

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidTeamId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfTeamIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowIllegalCallerException() {
        assertThrows(IllegalCallerException.class, () -> validator.validateOnUpdate(null, teamWithIdNull));
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidTeamId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfTeamIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnGetActiveObjectivesShouldBeSuccessfulWhenValidTeamId() {
        validator.validateOnGetActiveObjectives(team1);

        verify(validator, times(1)).validateOnGetActiveObjectives(team1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
        verify(validator, times(1)).throwExceptionWhenModelIsNull(team1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(team1.getId());
        verify(validator, times(1)).doesEntityExist(team1.getId());
    }

    @Test
    void validateOnGetActiveObjectivesShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGetActiveObjectives(teamWithIdNull));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(teamWithIdNull);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }
}
