package ch.puzzle.okr.service.validation;

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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectiveValidationServiceTest {
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);

    Objective objective1;
    Objective objectiveMinimal;
    User user;
    Quarter quarter;
    Team team;

    @BeforeEach
    void setUp() {
        this.user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        this.team = Team.Builder.builder().withId(1L).withName("Team1").build();
        this.quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();

        this.objective1 = Objective.Builder.builder().withId(1L).withTitle("Objective 1").withCreatedBy(user)
                .withTeam(team).withQuarter(quarter).withDescription("This is our description").withProgress(null)
                .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(user)
                .withCreatedOn(LocalDateTime.MAX).build();

        this.objectiveMinimal = Objective.Builder.builder().withId(null).withTitle("Objective 2").withCreatedBy(user)
                .withTeam(team).withQuarter(quarter).withProgress(null).withState(State.DRAFT).withModifiedBy(user)
                .withCreatedOn(LocalDateTime.MAX).build();

        when(objectivePersistenceService.findById(1L)).thenReturn(objective1);
        when(objectivePersistenceService.getModelName()).thenReturn("Objective");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", objectivePersistenceService.getModelName(), 2L)))
                        .when(objectivePersistenceService).findById(2L);
    }

    @Spy
    @InjectMocks
    private ObjectiveValidationService validator;

    private static Stream<Arguments> nameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 251), List.of(
                        "Attribute title must have a max length between 2 and 250 characters when saving objective.")),
                arguments(StringUtils.repeat('1', 1), List.of(
                        "Attribute title must have a max length between 2 and 250 characters when saving objective.")),
                arguments("", List.of(
                        "Missing attribute title when saving objective. Attribute title must have a max length between 2 and 250 characters when saving objective.")),
                arguments(" ", List.of(
                        "Attribute title must have a max length between 2 and 250 characters when saving objective. Missing attribute title when saving objective.")),
                arguments("         ", List.of("Missing attribute title when saving objective.")),
                arguments(null, List.of(
                        "Missing attribute title when saving objective. Attribute title can not be null when saving objective.")));
    }

    @Test
    void validateOnGet_ShouldBeSuccessfulWhenValidObjectiveId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGet_ShouldThrowExceptionIfObjectiveIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldBeSuccessfulWhenTeamIsValid() {
        validator.validateOnCreate(objectiveMinimal);

        verify(validator, times(1)).throwExceptionIfModelIsNull(objectiveMinimal);
        verify(validator, times(1)).validate(objectiveMinimal);
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model Objective is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(objective1));

        assertEquals("Model Objective cannot have id while create. Found id 1", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenTitleIsInvalid(String title, List<String> errors) {
        Objective objective = Objective.Builder.builder().withId(null).withTitle(title).withCreatedBy(this.user)
                .withTeam(this.team).withQuarter(this.quarter).withDescription("This is our description 2")
                .withProgress(null).withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(this.user)
                .withCreatedOn(LocalDateTime.MAX).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(objective));

        assertThat(exception.getReason()).contains(errors);
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenAttrsAreMissing() {
        Objective objective = Objective.Builder.builder().withId(null).withTitle("Title").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(objective));

        String error = "CreatedOn must not be null. CreatedBy must not be null. State must not be null. Quarter must not be null. Team must not be null.";
        assertThat(exception.getReason()).contains(error);
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model Objective is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, objectiveMinimal));

        verify(validator, times(1)).throwExceptionIfModelIsNull(objectiveMinimal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenTitleIsInvalid(String title, List<String> errors) {
        Objective objective = Objective.Builder.builder().withId(3L).withTitle(title).withCreatedBy(this.user)
                .withTeam(this.team).withQuarter(this.quarter).withDescription("This is our description 2")
                .withProgress(null).withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(this.user)
                .withCreatedOn(LocalDateTime.MAX).build();
        when(objectivePersistenceService.findById(5L)).thenReturn(this.objective1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(5L, objective));
        assertThat(exception.getReason()).contains(errors);
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenAttrsAreMissing() {
        Objective objective = Objective.Builder.builder().withId(5L).withTitle("Title").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(5L, objective));

        String error = "State must not be null. CreatedBy must not be null. Team must not be null. CreatedOn must not be null. Quarter must not be null.";
        assertThat(exception.getReason()).contains(error);
    }

    @Test
    void validateOnDelete_ShouldBeSuccessfulWhenValidObjectiveId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDelete_ShouldThrowExceptionIfObjectiveIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

}
