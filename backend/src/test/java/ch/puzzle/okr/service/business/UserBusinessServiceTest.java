package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.service.validation.UserValidationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserBusinessServiceTest {
    @MockBean
    UserPersistenceService userPersistenceService = mock(UserPersistenceService.class);
    @MockBean
    UserValidationService validationService = mock(UserValidationService.class);
    List<User> userList;
    @InjectMocks
    private UserBusinessService userBusinessService;

    @BeforeEach
    void setUp() {
        User userAlice = User.Builder.builder().withId(2L).withUsername("awunderland").withFirstname("Alice")
                .withLastname("Wunderland").withEmail("wunderland@puzzle.ch").build();

        User userBob = User.Builder.builder().withId(9L).withUsername("bbaumeister").withFirstname("Bob")
                .withLastname("Baumeister").withEmail("baumeister@puzzle.ch").build();

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
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        Mockito.when(userPersistenceService.findById(any())).thenReturn(owner);

        User returnedUser = userBusinessService.getOwnerById(1L);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstname());
        assertEquals("Kaufmann", returnedUser.getLastname());
        assertEquals("bkaufmann", returnedUser.getUsername());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void getOrCreateUser_ShouldReturnSingleUserWhenUserFound() {
        User newUser = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        Mockito.when(userPersistenceService.getOrCreateUser(any())).thenReturn(newUser);

        User returnedUser = userBusinessService.getOrCreateUser(newUser);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstname());
        assertEquals("Kaufmann", returnedUser.getLastname());
        assertEquals("bkaufmann", returnedUser.getUsername());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void getOrCreateUser_ShouldReturnSavedUserWhenUserNotFound() {
        User newUser = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        Mockito.when(userPersistenceService.getOrCreateUser(newUser)).thenReturn(newUser);

        User returnedUser = userBusinessService.getOrCreateUser(newUser);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstname());
        assertEquals("Kaufmann", returnedUser.getLastname());
        assertEquals("bkaufmann", returnedUser.getUsername());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void getOrCreateUser_ShouldThrowResponseStatusExceptionWhenInvalidUser() {
        User newUser = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id"))
                .when(validationService).validateOnCreate(newUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userBusinessService.getOrCreateUser(newUser);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Not allowed to give an id", exception.getReason());
    }
}
