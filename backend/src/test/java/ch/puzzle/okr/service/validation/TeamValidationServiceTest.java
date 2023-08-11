package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamValidationServiceTest {
    @MockBean
    TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
    @Spy
    @InjectMocks
    private TeamValidationService validator;

    private static Stream<Arguments> nameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 251),
                        "Attribute name must have size between 2 and 250 characters when saving team."),
                arguments(StringUtils.repeat('1', 1),
                        "Attribute name must have size between 2 and 250 characters when saving team."),
                arguments("",
                        "Attribute name must have size between 2 and 250 characters when saving team. Missing attribute name when saving team."),
                arguments(" ",
                        "Attribute name must have size between 2 and 250 characters when saving team. Missing attribute name when saving team."),
                arguments("         ", "Missing attribute name when saving team."), arguments(null,
                        "Missing attribute name when saving team. Attribute name can not be null when saving team."));
    }

    @Test
    void validateOnGet_ShouldBeSuccessfulWhenValidTeamId() {
        Team team = Team.Builder.builder().withId(1L).withName("Team 1").build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
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
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(1L));

        verify(validator, times(1)).isIdNull(1L);
        assertEquals("Team with id 1 not found", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldBeSuccessfulWhenTeamIsValid() {
        Team team = Team.Builder.builder().withId(1L).withName("Team 1").build();

        validator.validateOnCreate(team);

        verify(validator, times(1)).isModelNull(team);
        verify(validator, times(1)).validate(team);
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model Team is null", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenNameIsInvalid(String name, String error) {
        Team team = Team.Builder.builder().withId(1L).withName(name).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(team));
        assertEquals(exception.getReason(), error);
    }

    //
    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model Team is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenIdIsNull() {
        Team team = Team.Builder.builder().withId(null).withName("Team 1").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, team));
        verify(validator, times(1)).isModelNull(team);
        verify(validator, times(1)).isIdNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenUpdatingTeamDoesNotExists() {
        Team team = Team.Builder.builder().withId(1L).withName("Team 1").build();
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, team));
        verify(validator, times(1)).isModelNull(team);
        verify(validator, times(1)).isIdNull(1L);
        assertEquals("Team with id 1 not found", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenNameIsInvalid(String name, String error) {
        Team team = Team.Builder.builder().withId(1L).withName(name).build();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, team));
        assertEquals(exception.getReason(), error);
    }

    @Test
    void validateOnDelete_ShouldBeSuccessfulWhenValidTeamId() {
        Team team = Team.Builder.builder().withId(1L).withName("Team 1").build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
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
