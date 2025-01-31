package ch.puzzle.okr.service.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.test.TestHelper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class TeamValidationServiceTest {
    @MockitoBean
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
                                            String
                                                    .format("%s with id %s not found",
                                                            teamPersistenceService.getModelName(),
                                                            2L)))
                .when(teamPersistenceService)
                .findById(2L);
    }

    @Spy
    @InjectMocks
    private TeamValidationService validator;

    @DisplayName("Should be successful on validateOnGet() when team id is valid")
    @Test
    void validateOnGetShouldBeSuccessfulWhenValidTeamId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @DisplayName("Should throw exception on validateOnGet() when team id is null")
    @Test
    void validateOnGetShouldThrowExceptionIfTeamIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be successful on validateOnDelete() when team id is valid")
    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidTeamId() {
        validator.validateOnDelete(1L);

        verify(validator, times(1)).validateOnDelete(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @DisplayName("Should be throw exception on validateOnDelete() when team id is null")
    @Test
    void validateOnDeleteShouldThrowExceptionIfTeamIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnDelete(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be throw exception on validateOnCreate() when team id is not null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(team1));
        verify(validator, times(1)).throwExceptionWhenIdIsNotNull(team1.getId());
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be throw exception on validateOnCreate() when team already exists")
    @Test
    void validateOnCreateShouldThrowExceptionWhenTeamAlreadyExists() {
        BDDMockito.given(teamPersistenceService.findTeamsByName(anyString())).willReturn(List.of(team1));
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(teamWithIdNull));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ALREADY_EXISTS_SAME_NAME", List.of("Team", "Team null")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be throw exception on validateOnCreate() when model is null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(null));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be throw exception on validateOnCreate() when team name is null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNameIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(teamWithoutName));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(teamWithoutName);
        verify(validator, times(1)).throwExceptionWhenIdIsNotNull(teamWithoutName.getId());
        verify(validator, times(1)).validate(teamWithoutName);
        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("name", "Team")),
                    new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("name", "Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be throw exception on validateOnUpdate() when team id is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIdIsNull() {
        Long teamId = teamWithIdNull.getId();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(teamId, teamWithIdNull));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(teamWithIdNull);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(teamWithIdNull.getId());
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be throw exception on validateOnUpdate() when model and id are null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(null, null));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be throw exception on validateOnUpdate() when team name is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNameIsNull() {
        Long teamId = teamWithoutNameWithId.getId();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator
                                                                    .validateOnUpdate(teamId, teamWithoutNameWithId));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(teamWithoutNameWithId);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(teamWithoutNameWithId.getId());
        verify(validator, times(1)).validate(teamWithoutNameWithId);
        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("name", "Team")),
                    new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("name", "Team")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }
}
