package ch.puzzle.okr.service;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @MockBean
    UserRepository userRepository = mock(UserRepository.class);
    @MockBean
    ValidationService validationService = mock(ValidationService.class);
    List<User> userList;
    @InjectMocks
    private UserService userService;

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
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        List<User> userList = userService.getAllUsers();

        Assertions.assertThat(userList).hasSize(2);
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
        List<User> userList = userService.getAllUsers();

        Assertions.assertThat(userList.isEmpty());
    }

    @Test
    void shouldReturnSingleUserWhenFindingOwnerByValidId() {
        Optional<User> owner = Optional.of(User.Builder.builder().withId(1L).withFirstname("Bob")
                .withLastname("Kaufmann").withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build());
        Mockito.when(userRepository.findById(any())).thenReturn(owner);

        User returnedUser = userService.getOwnerById(1L);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstname());
        assertEquals("Kaufmann", returnedUser.getLastname());
        assertEquals("bkaufmann", returnedUser.getUsername());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void shouldThrowReponseStatusExceptionWhenFindingOwnerNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getOwnerById(321L);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Owner with id 321 not found", exception.getReason());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenGetOwnerWithNullId() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getOwnerById(null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing attribute owner id", exception.getReason());
    }

    @Test
    void getOrCreateUser_ShouldReturnSingleUserWhenUserFound() {
        User newUser = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        Mockito.when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.of(newUser));

        User returnedUser = userService.getOrCreateUser(newUser);

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
        Mockito.when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        User returnedUser = userService.getOrCreateUser(newUser);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Bob", returnedUser.getFirstname());
        assertEquals("Kaufmann", returnedUser.getLastname());
        assertEquals("bkaufmann", returnedUser.getUsername());
        assertEquals("kaufmann@puzzle.ch", returnedUser.getEmail());
    }
}