package ch.puzzle.okr.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.CacheService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.service.validation.UserValidationService;
import ch.puzzle.okr.test.TestHelper;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        User userAlice = User.Builder
                .builder() //
                .withId(2L) //
                .withFirstName("Alice") //
                .withLastName("Wunderland") //
                .withEmail("wunderland@puzzle.ch") //
                .build();

        User userBob = User.Builder
                .builder() //
                .withId(9L) //
                .withFirstName("Bob") //
                .withLastName("Baumeister") //
                .withEmail("baumeister@puzzle.ch") //
                .build();

        userList = Arrays.asList(userAlice, userBob);
    }

    @DisplayName("Should return correct list of all users on getAllUsers()")
    @Test
    void shouldReturnAllUsersCorrectly() throws ResponseStatusException {
        Mockito.when(userPersistenceService.findAll()).thenReturn(userList);

        List<User> userList = userBusinessService.getAllUsers();

        Assertions.assertThat(userList).hasSize(2);
        Assertions.assertThat(userList.getFirst().getId()).isEqualTo(2);
        Assertions.assertThat(userList.getFirst().getFirstName()).isEqualTo("Alice");
        Assertions.assertThat(userList.get(0).getLastName()).isEqualTo("Wunderland");
        Assertions.assertThat(userList.get(0).getEmail()).isEqualTo("wunderland@puzzle.ch");
        Assertions.assertThat(userList.get(1).getId()).isEqualTo(9);
        Assertions.assertThat(userList.get(1).getFirstName()).isEqualTo("Bob");
        Assertions.assertThat(userList.get(1).getLastName()).isEqualTo("Baumeister");
        Assertions.assertThat(userList.get(1).getEmail()).isEqualTo("baumeister@puzzle.ch");
    }

    @DisplayName("Should return empty list of users on getAllUsers() when there are no users")
    @Test
    void shouldReturnEmptyListOfUsers() throws ResponseStatusException {
        List<User> userList = userBusinessService.getAllUsers();

        Assertions.assertThat(userList.size()).isZero();
    }

    @DisplayName("Should return correct user on getUserById()")
    @Test
    void shouldReturnSingleUserWhenUserFoundUsingGetUserById() {
        User owner = User.Builder
                .builder()
                .withId(1L)
                .withFirstName("Bob")
                .withLastName("Kaufmann")
                .withEmail("kaufmann@puzzle.ch")
                .build();
        Mockito.when(userPersistenceService.findById(any())).thenReturn(owner);

        User returnedUser = userBusinessService.getUserById(1L);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstName());
        assertEquals("Kaufmann", returnedUser.getLastName());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @DisplayName("Should return user on getOrCreateUser() when user already exists")
    @Test
    void shouldReturnSingleUserWhenUserFoundUsingGetOrCreateUser() {
        User newUser = User.Builder
                .builder()
                .withId(1L)
                .withFirstName("Bob")
                .withLastName("Kaufmann")
                .withEmail("kaufmann@puzzle.ch")
                .build();
        Mockito.when(userPersistenceService.getOrCreateUser(any())).thenReturn(newUser);

        User returnedUser = userBusinessService.getOrCreateUser(newUser);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstName());
        assertEquals("Kaufmann", returnedUser.getLastName());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @DisplayName("Should create new user on getOrCreateUser() when user does not already exist")
    @Test
    void shouldReturnSavedUserWhenUserNotFoundUsingGetOrCreateUser() {
        User newUser = User.Builder
                .builder()
                .withId(1L)
                .withFirstName("Bob")
                .withLastName("Kaufmann")
                .withEmail("kaufmann@puzzle.ch")
                .build();
        Mockito.when(userPersistenceService.getOrCreateUser(newUser)).thenReturn(newUser);

        User returnedUser = userBusinessService.getOrCreateUser(newUser);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstName());
        assertEquals("Kaufmann", returnedUser.getLastName());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @DisplayName("Should throw exception on getOrCreateUser() when user is invalid")
    @Test
    void shouldThrowResponseStatusExceptionWhenInvalidUserUsingGetOrCreateUser() {
        User newUser = User.Builder
                .builder()
                .withId(1L)
                .withFirstName("Bob")
                .withLastName("Kaufmann")
                .withEmail("kaufmann@puzzle.ch")
                .build();
        Mockito
                .doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id"))
                .when(validationService)
                .validateOnGetOrCreate(newUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> userBusinessService.getOrCreateUser(newUser));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Not allowed to give an id", exception.getReason());
    }

    @DisplayName("Should set user to okr champion on setIsOkrChampion()")
    @Test
    void shouldSetOkrChampionCorrectly() {
        var user = TestHelper.defaultUser(1L);
        user.setOkrChampion(false);

        userBusinessService.setIsOkrChampion(user, true);

        verify(userPersistenceService, times(1)).save(user);
        assertTrue(user.isOkrChampion());
    }

    @DisplayName("Should throw exception on setIsOkrChampion() when the last okr champion is removed")
    @Test
    void shouldThrowExceptionIfLastOkrChampionIsRemovedUsingSetIsOkrChampion() {
        var user = TestHelper.defaultUser(1L);
        var user2 = TestHelper.defaultUser(2L);
        user.setOkrChampion(true);
        when(userPersistenceService.findAllOkrChampions()).thenReturn(List.of(user, user2));

        assertThrows(OkrResponseStatusException.class, () -> userBusinessService.setIsOkrChampion(user, false));
    }

    @DisplayName("Should not throw exception on setIsOkrChampion() when removing the second to last okr champion")
    @Test
    void shouldNotThrowExceptionIfPenultimateOkrChampionIsRemovedUsingSetOkrChampion() {
        var user = TestHelper.defaultUser(1L);
        var user2 = TestHelper.defaultUser(2L);
        user.setOkrChampion(true);
        user2.setOkrChampion(true);
        when(userPersistenceService.findAllOkrChampions()).thenReturn(List.of(user, user2));

        userBusinessService.setIsOkrChampion(user, false);

        verify(userPersistenceService, times(1)).save(user);
        assertFalse(user.isOkrChampion());
    }

    @DisplayName("Should delete user on deleteEntityById()")
    @Test
    void shouldDeleteUser() {
        userBusinessService.deleteEntityById(23L);

        verify(userPersistenceService, times(1)).deleteById(23L);
    }
}
