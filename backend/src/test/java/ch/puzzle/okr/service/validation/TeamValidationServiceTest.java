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

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
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
    Team teamWithoutName;
    Team teamWithoutNameWithId;

    @BeforeEach
    void setUp() {
        team1 = Team.Builder.builder().withId(1L).withName("Team 1").build();
        team2 = Team.Builder.builder().withId(2L).withName("Team 2").build();
        teamWithIdNull = Team.Builder.builder().withId(null).withName("Team null").build();
        teamWithoutName = Team.Builder.builder().build();
        teamWithoutNameWithId = Team.Builder.builder().withId(1L).build();

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

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(team1));
        verify(validator, times(1)).throwExceptionWhenIdIsNotNull(team1.getId());
        assertEquals("Model Team cannot have id while create. Found id 1", exception.getReason());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(null);
        assertEquals("Given model Team is null", exception.getReason());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNameIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(teamWithoutName));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(teamWithoutName);
        verify(validator, times(1)).throwExceptionWhenIdIsNotNull(teamWithoutName.getId());
        verify(validator, times(1)).validate(teamWithoutName);
        assertThat(exception.getReason().strip())
                .contains("Missing attribute name when saving team. Attribute name can not be null when saving team.");
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(teamWithIdNull.getId(), teamWithIdNull));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(teamWithIdNull);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(teamWithIdNull.getId());
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, null));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(null);
        assertEquals("Given model Team is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNameIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(teamWithoutNameWithId.getId(), teamWithoutNameWithId));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(teamWithoutNameWithId);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(teamWithoutNameWithId.getId());
        verify(validator, times(1)).validate(teamWithoutNameWithId);
        assertThat(Objects.requireNonNull(exception.getReason()).strip())
                .contains("Missing attribute name when saving team. Attribute name can not be null when saving team.");
    }
}
