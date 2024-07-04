package ch.puzzle.okr.service.business;

import ch.puzzle.okr.test.TestHelper;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.CacheService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.service.validation.UserValidationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBusinessServiceTest {
    @Mock
    UserPersistenceService userPersistenceService;
    @Mock
    UserValidationService validationService;
    @Mock
    CacheService cacheService;
    List<User> userList;
    @InjectMocks
    private UserBusinessService userBusinessService;

    @BeforeEach
    void setUp() {
        User userAlice = User.Builder.builder().withId(2L).withFirstname("Alice").withLastname("Wunderland")
                .withEmail("wunderland@puzzle.ch").build();

        User userBob = User.Builder.builder().withId(9L).withFirstname("Bob").withLastname("Baumeister")
                .withEmail("baumeister@puzzle.ch").build();

        userList = Arrays.asList(userAlice, userBob);
    }

    @Test
    void shouldReturnAllUsersCorrect() throws ResponseStatusException {
        Mockito.when(userPersistenceService.findAll()).thenReturn(userList);

        List<User> userList = userBusinessService.getAllUsers();

        Assertions.assertThat(userList.size()).isEqualTo(2);
        Assertions.assertThat(userList.get(0).getId()).isEqualTo(2);
        Assertions.assertThat(userList.get(0).getFirstname()).isEqualTo("Alice");
        Assertions.assertThat(userList.get(0).getLastname()).isEqualTo("Wunderland");
        Assertions.assertThat(userList.get(0).getEmail()).isEqualTo("wunderland@puzzle.ch");
        Assertions.assertThat(userList.get(1).getId()).isEqualTo(9);
        Assertions.assertThat(userList.get(1).getFirstname()).isEqualTo("Bob");
        Assertions.assertThat(userList.get(1).getLastname()).isEqualTo("Baumeister");
        Assertions.assertThat(userList.get(1).getEmail()).isEqualTo("baumeister@puzzle.ch");
    }

    @Test
    void shouldReturnEmptyUsers() throws ResponseStatusException {
        List<User> userList = userBusinessService.getAllUsers();

        Assertions.assertThat(userList.size()).isEqualTo(0);
    }

    @Test
    void shouldReturnSingleUserWhenFindingOwnerByValidId() {
        User owner = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withEmail("kaufmann@puzzle.ch").build();
        Mockito.when(userPersistenceService.findById(any())).thenReturn(owner);

        User returnedUser = userBusinessService.getUserById(1L);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstname());
        assertEquals("Kaufmann", returnedUser.getLastname());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void getOrCreateUserShouldReturnSingleUserWhenUserFound() {
        User newUser = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withEmail("kaufmann@puzzle.ch").build();
        Mockito.when(userPersistenceService.getOrCreateUser(any())).thenReturn(newUser);

        User returnedUser = userBusinessService.getOrCreateUser(newUser);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstname());
        assertEquals("Kaufmann", returnedUser.getLastname());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void getOrCreateUserShouldReturnSavedUserWhenUserNotFound() {
        User newUser = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withEmail("kaufmann@puzzle.ch").build();
        Mockito.when(userPersistenceService.getOrCreateUser(newUser)).thenReturn(newUser);

        User returnedUser = userBusinessService.getOrCreateUser(newUser);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstname());
        assertEquals("Kaufmann", returnedUser.getLastname());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void getOrCreateUserShouldThrowResponseStatusExceptionWhenInvalidUser() {
        User newUser = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withEmail("kaufmann@puzzle.ch").build();
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id"))
                .when(validationService).validateOnGetOrCreate(newUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userBusinessService.getOrCreateUser(newUser);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Not allowed to give an id", exception.getReason());
    }

    @Test
    void setOkrChampion_shouldSetOkrChampionCorrectly() {
        var user = TestHelper.defaultUser(1L);
        user.setOkrChampion(false);

        userBusinessService.setIsOkrChampion(user, true);

        verify(userPersistenceService, times(1)).save(user);
        assertTrue(user.isOkrChampion());
    }

    @Test
    void setOkrChampion_shouldThrowExceptionIfLastOkrChampIsRemoved() {
        var user = TestHelper.defaultUser(1L);
        var user2 = TestHelper.defaultUser(2L);
        user.setOkrChampion(true);
        when(userPersistenceService.findAllOkrChampions()).thenReturn(List.of(user, user2));

        assertThrows(OkrResponseStatusException.class, () -> userBusinessService.setIsOkrChampion(user, false));
    }

    @Test
    void setOkrChampion_shouldNotThrowExceptionIfSecondLastOkrChampIsRemoved() {
        var user = TestHelper.defaultUser(1L);
        var user2 = TestHelper.defaultUser(2L);
        user.setOkrChampion(true);
        user2.setOkrChampion(true);
        when(userPersistenceService.findAllOkrChampions()).thenReturn(List.of(user, user2));

        userBusinessService.setIsOkrChampion(user, false);

        verify(userPersistenceService, times(1)).save(user);
        assertFalse(user.isOkrChampion());
    }
}
