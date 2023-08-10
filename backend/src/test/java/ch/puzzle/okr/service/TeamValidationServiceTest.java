package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamValidationServiceTest {
    @Spy
    @InjectMocks
    private TeamValidationService validator;

    @MockBean
    TeamRepository teamRepository = Mockito.mock(TeamRepository.class);

    @Test
    void validateOnGet_ShouldBeSuccessfulWhenValidTeamId() {
        Team team = Team.Builder.builder().withId(1L).withName("Team 1").build();
        Mockito.when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        validator.validateOnGet(1L);
        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).isIdNull(1L);
    }

    @Test
    void validateOnGet_ShouldThrowExceptionIfTeamIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));
        verify(validator, times(1)).isIdNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnSave_ShouldThrowExceptionWhenTeamDoesNotExist() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(1L));

        verify(validator, times(1)).isIdNull(1L);
        verify(validator, times(1)).doesEntityExist(eq(1L), validator.modelName());
        assertEquals("Team with id 1 not found", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldBeSuccessfulWhenTeamIsValid() {
        Team team = Team.Builder.builder().withId(1L).withName("Team 1").build();
        validator.validateOnCreate(team);
        verify(validator, times(1)).isModelNull(team, validator.modelName());
        verify(validator, times(1)).validate(team);
    }

    // @Test
    // void validateOnSave_ShouldThrowExceptionWhenNameIsToShort() {
    // Team team = Team.Builder.builder().withId(null).withName("1").build();
    // ResponseStatusException exception = assertThrows(ResponseStatusException.class,
    // () -> validator.validateOnCreate(team));
    // assertTrue(exception.getReason()
    // .contains("Attribute name must have size between 2 and 250 characters when saving team."));
    // }
    //
    // @Test
    // void validateOnSave_ShouldThrowExceptionWhenNameIsToLong() {
    // Team team = Team.Builder.builder().withId(null).withName(StringUtils.repeat('1', 251)).build();
    // ResponseStatusException exception = assertThrows(ResponseStatusException.class,
    // () -> validator.validateOnCreate(team));
    // assertTrue(exception.getReason()
    // .contains("Attribute name must have size between 2 and 250 characters when saving team."));
    // }
    //
    // @Test
    // void validateOnSave_ShouldThrowExceptionWhenNameIsBlank() {
    // Team team = Team.Builder.builder().withId(null).withName(" ").build();
    // ResponseStatusException exception = assertThrows(ResponseStatusException.class,
    // () -> validator.validateOnCreate(team));
    // assertTrue(exception.getReason().contains("Missing attribute name when saving team"));
    // }
    //
    // @Test
    // void validateOnSave_ShouldBeSuccessfulWhenValidTeam() {
    // Team team = Team.Builder.builder().withId(null).withName("testTeam").build();
    // validator.validateOnCreate(team);
    // verify(validator, times(1)).validateOnCreate(team);
    // }
    //
    // @Test
    // void validateOnSave_ShouldThrowExceptionWhenIdIsNotNull() {
    // Team team = Team.Builder.builder().withId(5L).withName("testTeam").build();
    // ResponseStatusException exception = assertThrows(ResponseStatusException.class,
    // () -> validator.validateOnCreate(team));
    // assertTrue(exception.getReason().contains("Not allowed to give an id"));
    // }
    //
    // @Test
    // void validateOnUpdate_ShouldBeSuccessfulWhenValidTeam() {
    // Team team = Team.Builder.builder().withId(5L).withName("testTeam").build();
    // validator.validateOnUpdate(team);
    // verify(validator, times(1)).validateOnUpdate(team);
    // }
    //
    // @Test
    // void validateOnUpdate_ShouldThrowExceptionWhenIdIsNull() {
    // Team team = Team.Builder.builder().withId(null).withName("testTeam").build();
    // ResponseStatusException exception = assertThrows(ResponseStatusException.class,
    // () -> validator.validateOnUpdate(team));
    // assertTrue(exception.getReason().contains("Missing attribute team id"));
    // }
}
