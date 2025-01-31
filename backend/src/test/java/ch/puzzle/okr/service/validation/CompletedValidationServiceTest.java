package ch.puzzle.okr.service.validation;

import static ch.puzzle.okr.test.AssertionHelper.assertOkrResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.persistence.CompletedPersistenceService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CompletedValidationServiceTest {
    @MockitoBean
    CompletedPersistenceService completedPersistenceService = Mockito.mock(CompletedPersistenceService.class);

    Completed validCompleted;

    User user;
    Quarter quarter;
    Team team;
    Objective objective;

    @BeforeEach
    void setUp() {
        this.user = User.Builder
                .builder()
                .withId(1L)
                .withFirstName("Bob")
                .withLastName("Kaufmann")
                .withEmail("kaufmann@puzzle.ch")
                .build();
        this.team = Team.Builder.builder().withId(1L).withName("Team1").build();
        this.quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();

        this.objective = Objective.Builder
                .builder()
                .withId(1L)
                .withTitle("Objective 1")
                .withCreatedBy(user)
                .withTeam(team)
                .withQuarter(quarter)
                .withDescription("This is our description")
                .withModifiedOn(LocalDateTime.MAX)
                .withState(State.DRAFT)
                .withModifiedBy(user)
                .withCreatedOn(LocalDateTime.MAX)
                .build();

        this.validCompleted = Completed.Builder
                .builder()
                .withObjective(this.objective)
                .withComment("Valid Comment")
                .build();

        when(completedPersistenceService.getCompletedByObjectiveId(1L)).thenReturn(this.validCompleted);
        when(completedPersistenceService.getModelName()).thenReturn("Completed");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                            String
                                                    .format("%s with id %s not found",
                                                            completedPersistenceService.getModelName(),
                                                            2L)))
                .when(completedPersistenceService)
                .getCompletedByObjectiveId(2L);
    }

    @Spy
    @InjectMocks
    private CompletedValidationService validator;

    private static Stream<Arguments> nameValidationArguments() {
        return Stream
                .of(arguments(StringUtils.repeat('1', 5000),
                              List
                                      .of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("comment", "Completed", "0", "4096")))));
    }

    @DisplayName("Should be successful on validateOnCreate() when model is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenCompletedIsValid() {
        validator.validateOnCreate(this.validCompleted);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(this.validCompleted);
        verify(validator, times(1)).validate(this.validCompleted);
    }

    @DisplayName("Should throw exception on validateOnCreate() when model is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenModelIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("Completed")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        // arrange
        Completed completed = Completed.Builder
                .builder() //
                .withId(300L) //
                .withObjective(this.objective) //
                .withComment("Not valid") //
                .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(completed));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Completed")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @ParameterizedTest(name = "should throw exception on validateOnCreate() when model has invalid comment {0}")
    @MethodSource("nameValidationArguments")
    void shouldThrowExceptionOnValidateOnCreateWhenCommentIsInvalid(String comment, List<ErrorDto> expectedErrors) {
        // arrange
        Completed completed = Completed.Builder
                .builder() //
                .withObjective(this.objective) //
                .withComment(comment) //
                .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(completed));

        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnCreate() when attributes are missing")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenAttrsAreMissing() {
        // arrange
        Completed completedInvalid = Completed.Builder
                .builder() //
                .withId(null) //
                .withComment("Valid comment") //
                .withObjective(null) //
                .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(completedInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("objective", "Completed")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnUpdate()")
    @Test
    void shouldThrowOnValidateOnUpdateException() {
        // arrange
        Long id = 1L;
        Completed completed = Completed.Builder.builder().build();

        // act + assert
        assertThrows(IllegalCallerException.class, () -> validator.validateOnUpdate(id, completed));
    }

}
