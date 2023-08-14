package ch.puzzle.okr.service.validation;

import static org.assertj.core.api.Assertions.assertThat;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistance.TeamPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
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

    private static Stream<Arguments> nameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 251),
                        List.of("Attribute name must have size between 2 and 250 characters when saving team.")),
                arguments(StringUtils.repeat('1', 1),
                        List.of("Attribute name must have size between 2 and 250 characters when saving team.")),
                arguments("",
                        List.of("Attribute name must have size between 2 and 250 characters when saving team.",
                                "Missing attribute name when saving team.")),
                arguments(" ",
                        List.of("Attribute name must have size between 2 and 250 characters when saving team.",
                                "Missing attribute name when saving team.")),
                arguments("         ", List.of("Missing attribute name when saving team.")),
                arguments(null, List.of("Missing attribute name when saving team.",
                        "Attribute name can not be null when saving team.")));
    }

    @Test
    void validateOnGet_ShouldBeSuccessfulWhenValidTeamId() {
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
                () -> validator.validateOnGet(2L));

        verify(validator, times(1)).isIdNull(2L);
        assertEquals("Team with id 2 not found", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldBeSuccessfulWhenTeamIsValid() {
        validator.validateOnCreate(teamWithIdNull);

        verify(validator, times(1)).isModelNull(teamWithIdNull);
        verify(validator, times(1)).validate(teamWithIdNull);
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model Team is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(team1));

        assertEquals("Model Team cannot have id while create. Found id 1", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenNameIsInvalid(String name, List<String> errors) {
        Team team = Team.Builder.builder().withId(null).withName(name).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(team));

        assertThat(exception.getReason()).contains(errors);
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model Team is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, teamWithIdNull));

        verify(validator, times(1)).isModelNull(teamWithIdNull);
        verify(validator, times(1)).isIdNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenUpdatingTeamDoesNotExists() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, team2));

        verify(validator, times(1)).isModelNull(team2);
        verify(validator, times(1)).isIdNull(2L);
        assertEquals("Team with id 2 not found", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenNameIsInvalid(String name, List<String> errors) {
        Team team = Team.Builder.builder().withId(5L).withName(name).build();
        when(teamPersistenceService.findById(5L)).thenReturn(team);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(5L, team));
        assertThat(exception.getReason()).contains(errors);
    }

    @Test
    void validateOnDelete_ShouldBeSuccessfulWhenValidTeamId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).isIdNull(1L);
    }

    @Test
    void validateOnDelete_ShouldThrowExceptionIfTeamIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).isIdNull(null);
        assertEquals("Id is null", exception.getReason());
    }
}
