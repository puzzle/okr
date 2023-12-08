package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(MockitoExtension.class)
class ObjectiveValidationServiceTest {
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);

    Objective objective1;
    Objective objectiveMinimal;
    User user;
    Quarter quarter;
    Team team;
    @Spy
    @InjectMocks
    private ObjectiveValidationService validator;

    private static Stream<Arguments> nameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 251),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "Objective", "2", "250")))),
                arguments(StringUtils.repeat('1', 1),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "Objective", "2", "250")))),

                arguments("",
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "Objective", "2", "250")),
                                new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "Objective")))),

                arguments(" ",
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "Objective", "2", "250")),
                                new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "Objective")))),

                arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "Objective")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "Objective")),
                        new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("title", "Objective")))));
    }

    @BeforeEach
    void setUp() {
        this.user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        this.team = Team.Builder.builder().withId(1L).withName("Team1").build();
        this.quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();

        this.objective1 = Objective.Builder.builder().withId(1L).withTitle("Objective 1").withCreatedBy(user)
                .withTeam(team).withQuarter(quarter).withDescription("This is our description")
                .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(user)
                .withCreatedOn(LocalDateTime.MAX).build();

        this.objectiveMinimal = Objective.Builder.builder().withId(null).withTitle("Objective 2").withCreatedBy(user)
                .withTeam(team).withQuarter(quarter).withState(State.DRAFT).withCreatedOn(LocalDateTime.MAX).build();

        when(objectivePersistenceService.findById(1L)).thenReturn(objective1);
        when(objectivePersistenceService.getModelName()).thenReturn("Objective");
        doThrow(new OkrResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", objectivePersistenceService.getModelName(), 2L)))
                        .when(objectivePersistenceService).findById(2L);
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidObjectiveId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfObjectiveIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenTeamIsValid() {
        validator.validateOnCreate(objectiveMinimal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveMinimal);
        verify(validator, times(1)).validate(objectiveMinimal);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objective1));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenTitleIsInvalid(String title, List<ErrorDto> expectedErrors) {
        Objective objective = Objective.Builder.builder().withId(null).withTitle(title).withCreatedBy(this.user)
                .withTeam(this.team).withQuarter(this.quarter).withDescription("This is our description 2")
                .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withCreatedOn(LocalDateTime.MAX).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objective));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        Objective objectiveInvalid = Objective.Builder.builder().withId(null).withTitle("Title").build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("team", "Objective")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "Objective")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "Objective")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("state", "Objective")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("quarter", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrModifiedByIsSet() {
        Objective objectiveInvalid = Objective.Builder.builder().withId(null)
                .withTitle("ModifiedBy is not null on create").withCreatedBy(user).withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT).withTeam(team).withQuarter(quarter).withModifiedBy(user).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveInvalid));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_SET_FORBIDDEN", List.of("ModifiedBy",
                "User{id=1, version=0, username='bkaufmann', firstname='Bob', lastname='Kaufmann', email='kaufmann@puzzle.ch', writeable=false}")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenObjectiveIsValid() {
        validator.validateOnUpdate(objective1.getId(), objective1);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objective1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(objective1.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(objective1.getId(), objective1.getId());
        verify(validator, times(1)).validate(objective1);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, objectiveMinimal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveMinimal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(7L, objective1));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objective1);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(objective1.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(7L, objective1.getId());
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "7", "1")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenTitleIsInvalid(String title, List<ErrorDto> expectedErrors) {
        Objective objective = Objective.Builder.builder().withId(3L).withTitle(title).withCreatedBy(this.user)
                .withTeam(this.team).withQuarter(this.quarter).withDescription("This is our description 2")
                .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(this.user)
                .withCreatedOn(LocalDateTime.MAX).build();
        when(objectivePersistenceService.findById(objective.getId())).thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, objective));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        Objective objective = Objective.Builder.builder().withId(5L).withTitle("Title").withModifiedBy(user).build();
        when(objectivePersistenceService.findById(objective.getId())).thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(5L, objective));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("quarter", "Objective")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("team", "Objective")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "Objective")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "Objective")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("state", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrModifiedByIsNotSet() {
        Objective objectiveInvalid = Objective.Builder.builder().withId(1L)
                .withTitle("ModifiedBy is not null on create").withCreatedBy(user).withCreatedOn(LocalDateTime.MAX)
                .withState(State.DRAFT).withTeam(team).withQuarter(quarter).withModifiedBy(null).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, objectiveInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_SET", List.of("modifiedBy")));

        assertEquals(INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWheTeamHasChanged() {
        Objective savedObjective = Objective.Builder.builder().withId(1L).withTitle("Team has changed")
                .withCreatedBy(user).withCreatedOn(LocalDateTime.MAX).withState(State.DRAFT).withTeam(team)
                .withQuarter(quarter).withModifiedBy(null).build();
        Objective updatedObjective = Objective.Builder.builder().withId(1L).withTitle("Team has changed")
                .withCreatedBy(user).withCreatedOn(LocalDateTime.MAX).withState(State.DRAFT)
                .withTeam(Team.Builder.builder().withId(2L).withName("other team").build()).withQuarter(quarter)
                .withModifiedBy(user).build();
        when(objectivePersistenceService.findById(savedObjective.getId())).thenReturn(savedObjective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, updatedObjective));
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CANNOT_CHANGE", List.of("Team", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidObjectiveId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfObjectiveIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Objective")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }
}
