package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Team;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceTest {
    @Spy
    private ValidationService validationService;

    @Test
    void validateOnSave_ShouldBeSuccessfulWhenValidTeam() {
        Team team = Team.Builder.builder().withId(null).withName("testTeam").build();
        validationService.validateOnSave(team);
        verify(validationService, times(1)).validateOnSave(team);
    }

    @Test
    void validateOnSave_ShouldThrowExceptionWhenIdIsNotNull() {
        Team team = Team.Builder.builder().withId(5L).withName("testTeam").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason().contains("Not allowed to give an id"));
    }

    @Test
    void validateOnSave_ShouldThrowExceptionWhenNameIsNull() {
        Team team = Team.Builder.builder().withId(null).withName(null).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason().contains("Missing attribute name when saving team."));
        assertTrue(exception.getReason().contains("Attribute name can not be null when saving team."));
    }

    @Test
    void validateOnSave_ShouldThrowExceptionWhenNameIsToShort() {
        Team team = Team.Builder.builder().withId(null).withName("1").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason()
                .contains("Attribute name must have size between 2 and 250 characters when saving team."));
    }

    @Test
    void validateOnSave_ShouldThrowExceptionWhenNameIsToLong() {
        Team team = Team.Builder.builder().withId(null).withName(StringUtils.repeat('1', 251)).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason()
                .contains("Attribute name must have size between 2 and 250 characters when saving team."));
    }

    @Test
    void validateOnSave_ShouldThrowExceptionWhenNameIsBlank() {
        Team team = Team.Builder.builder().withId(null).withName("    ").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason().contains("Missing attribute name when saving team"));
    }

    @Test
    void validateOnUpdate_ShouldBeSuccessfulWhenValidTeam() {
        Team team = Team.Builder.builder().withId(5L).withName("testTeam").build();
        validationService.validateOnUpdate(team);
        verify(validationService, times(1)).validateOnUpdate(team);
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenIdIsNull() {
        Team team = Team.Builder.builder().withId(null).withName("testTeam").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnUpdate(team));
        assertTrue(exception.getReason().contains("Missing attribute team id"));
    }
}
