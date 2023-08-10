package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
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
    void validateOnSaveTeam_ShouldBeSuccessfulWhenValidTeam() {
        Team team = Team.Builder.builder().withId(null).withName("testTeam").build();
        validationService.validateOnSave(team);
        verify(validationService, times(1)).validateOnSave(team);
    }

    @Test
    void validateOnSaveTeam_ShouldThrowExceptionWhenIdIsNotNull() {
        Team team = Team.Builder.builder().withId(5L).withName("testTeam").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason().contains("Not allowed to give an id"));
    }

    @Test
    void validateOnSaveTeam_ShouldThrowExceptionWhenNameIsNull() {
        Team team = Team.Builder.builder().withId(null).withName(null).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason().contains("Missing attribute name when saving team."));
        assertTrue(exception.getReason().contains("Attribute name can not be null when saving team."));
    }

    @Test
    void validateOnSaveTeam_ShouldThrowExceptionWhenNameIsToShort() {
        Team team = Team.Builder.builder().withId(null).withName("1").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason()
                .contains("Attribute name must have size between 2 and 250 characters when saving team."));
    }

    @Test
    void validateOnSaveTeam_ShouldThrowExceptionWhenNameIsToLong() {
        Team team = Team.Builder.builder().withId(null).withName(StringUtils.repeat('1', 251)).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason()
                .contains("Attribute name must have size between 2 and 250 characters when saving team."));
    }

    @Test
    void validateOnSaveTeam_ShouldThrowExceptionWhenNameIsBlank() {
        Team team = Team.Builder.builder().withId(null).withName("    ").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(team));
        assertTrue(exception.getReason().contains("Missing attribute name when saving team"));
    }

    @Test
    void validateOnUpdateTeam_ShouldBeSuccessfulWhenValidTeam() {
        Team team = Team.Builder.builder().withId(5L).withName("testTeam").build();
        validationService.validateOnUpdate(team);
        verify(validationService, times(1)).validateOnUpdate(team);
    }

    @Test
    void validateOnUpdateTeam_ShouldThrowExceptionWhenIdIsNull() {
        Team team = Team.Builder.builder().withId(null).withName("testTeam").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnUpdate(team));
        assertTrue(exception.getReason().contains("Missing attribute team id"));
    }

    @Test
    void validateOnSaveUser_ShouldBeSuccessfulWhenValidUser() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("firstname")
                .withLastname("lastname").withEmail("email@puzzle.ch").build();
        validationService.validateOnSave(user);
        verify(validationService, times(1)).validateOnSave(user);
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenIdIsNotNull() {
        User user = User.Builder.builder().withId(5L).withUsername("username").withFirstname("firstname")
                .withLastname("lastname").withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason().contains("Not allowed to give an id"));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenUserameIsNull() {
        User user = User.Builder.builder().withId(null).withUsername(null).withFirstname("firstname")
                .withLastname("lastname").withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason().contains("Missing attribute username when saving user."));
        assertTrue(exception.getReason().contains("Attribute username can not be null when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenUsernameIsToShort() {
        User user = User.Builder.builder().withId(null).withUsername("1").withFirstname("firstname")
                .withLastname("lastname").withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason()
                .contains("Attribute username must have size between 2 and 20 characters when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenUsernameameIsToLong() {
        User user = User.Builder.builder().withId(null).withUsername(StringUtils.repeat('1', 21))
                .withFirstname("firstname").withLastname("lastname").withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason()
                .contains("Attribute username must have size between 2 and 20 characters when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenFirstnameIsNull() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname(null)
                .withLastname("lastname").withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason().contains("Missing attribute firstname when saving user."));
        assertTrue(exception.getReason().contains("Attribute firstname can not be null when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenFirstnameIsToShort() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("1")
                .withLastname("lastname").withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason()
                .contains("Attribute firstname must have size between 2 and 50 characters when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenFirstnameIsToLong() {
        User user = User.Builder.builder().withId(null).withUsername("username")
                .withFirstname(StringUtils.repeat('1', 51)).withLastname("lastname").withEmail("email@puzzle.ch")
                .build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason()
                .contains("Attribute firstname must have size between 2 and 50 characters when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenLastnameIsNull() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("firstname")
                .withLastname(null).withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason().contains("Missing attribute lastname when saving user."));
        assertTrue(exception.getReason().contains("Attribute lastname can not be null when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenLastnameIsToShort() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("firstname")
                .withLastname("1").withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason()
                .contains("Attribute lastname must have size between 2 and 50 characters when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenLastnameIsToLong() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("firstname")
                .withLastname(StringUtils.repeat('1', 51)).withEmail("email@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason()
                .contains("Attribute lastname must have size between 2 and 50 characters when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenEmailIsNull() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("firstname")
                .withLastname("lastname").withEmail(null).build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason().contains("Attribute email can not be null when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenEmailIsToShort() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("firstname")
                .withLastname("lastname").withEmail("1").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason()
                .contains("Attribute email must have size between 2 and 250 characters when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenEmailIsToLong() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("firstname")
                .withLastname("lastname").withEmail(StringUtils.repeat('1', 251) + "@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason()
                .contains("Attribute email must have size between 2 and 250 characters when saving user."));
    }

    @Test
    void validateOnSaveUser_ShouldThrowExceptionWhenEmailIsNotValid() {
        User user = User.Builder.builder().withId(null).withUsername("username").withFirstname("firstname")
                .withLastname("lastname").withEmail("@puzzle.ch").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validationService.validateOnSave(user));
        assertTrue(exception.getReason().contains("Attribute email should be valid when saving user."));
    }
}
