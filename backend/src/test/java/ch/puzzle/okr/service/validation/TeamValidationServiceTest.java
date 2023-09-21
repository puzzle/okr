package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGet_ShouldThrowExceptionIfTeamIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldThrowIllegalAccessError() {
        assertThrows(IllegalAccessError.class, () -> validator.validateOnCreate(teamWithIdNull));
    }

    @Test
    void validateOnUpdate_ShouldThrowIllegalAccessException() {
        assertThrows(IllegalAccessError.class, () -> validator.validateOnUpdate(null, teamWithIdNull));
    }

    @Test
    void validateOnDelete_ShouldBeSuccessfulWhenValidTeamId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDelete_ShouldThrowExceptionIfTeamIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnGetActiveObjectives_ShouldBeSuccessfulWhenValidTeamId() {
        validator.validateOnGetActiveObjectives(team1);

        verify(validator, times(1)).validateOnGetActiveObjectives(team1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
        verify(validator, times(1)).throwExceptionIfModelIsNull(team1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(team1.getId());
        verify(validator, times(1)).doesEntityExist(team1.getId());
    }

    @Test
    void validateOnGetActiveObjectives_ShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGetActiveObjectives(teamWithIdNull));

        verify(validator, times(1)).throwExceptionIfModelIsNull(teamWithIdNull);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }
}
