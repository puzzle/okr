package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.persistence.CompletedPersistenceService;
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
class CompletedValidationServiceTest {
    @MockBean
    CompletedPersistenceService completedPersistenceService = Mockito.mock(CompletedPersistenceService.class);

    Completed validCompleted;

    User user;
    Quarter quarter;
    Team team;
    Objective objective;

    @BeforeEach
    void setUp() {
        this.user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        this.team = Team.Builder.builder().withId(1L).withName("Team1").build();
        this.quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();

        this.objective = Objective.Builder.builder().withId(1L).withTitle("Objective 1").withCreatedBy(user)
                .withTeam(team).withQuarter(quarter).withDescription("This is our description")
                .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(user)
                .withCreatedOn(LocalDateTime.MAX).build();

        this.validCompleted = Completed.Builder.builder().withObjective(this.objective).withComment("Valid Comment")
                .build();

        when(completedPersistenceService.getCompletedByObjectiveId(1L)).thenReturn(this.validCompleted);
        when(completedPersistenceService.getModelName()).thenReturn("Completed");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", completedPersistenceService.getModelName(), 2L)))
                        .when(completedPersistenceService).getCompletedByObjectiveId(2L);
    }

    @Spy
    @InjectMocks
    private CompletedValidationService validator;

    private static Stream<Arguments> nameValidationArguments() {
        return Stream.of(arguments(StringUtils.repeat('1', 5000),
                List.of("Attribute comment has a max length of 4096 characters when completing an objective")));
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenCompletedIsValid() {
        validator.validateOnCreate(this.validCompleted);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(this.validCompleted);
        verify(validator, times(1)).validate(this.validCompleted);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model Completed is null", exception.getReason());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        Completed completed = Completed.Builder.builder().withId(300L).withObjective(this.objective)
                .withComment("Not valid").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(completed));

        assertEquals("Model Completed cannot have id while create. Found id 300", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenCommentIsInvalid(String comment, List<String> errors) {
        Completed completed = Completed.Builder.builder().withObjective(this.objective).withComment(comment).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(completed));

        String[] exceptionParts = exception.getReason().split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        Completed completedInvalid = Completed.Builder.builder().withId(null).withComment("Valid comment")
                .withObjective(null).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(completedInvalid));

        assertThat(exception.getReason().strip()).contains("Objective must not be null.");
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidCompletedId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfCompletedIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

}
