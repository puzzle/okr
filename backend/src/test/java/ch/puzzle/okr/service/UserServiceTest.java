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
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @MockBean
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    @InjectMocks
    private UserService userService;

    List<User> userList;

    @BeforeEach
    void setUp() {
        User userAlice = User.Builder.builder()
                .withId(2L)
                .withUsername("awunderland")
                .withFirstname("Alice")
                .withLastname("Wunderland")
                .withEmail("wunderland@puzzle.ch")
                .build();

        User userBob = User.Builder.builder()
                .withId(9L)
                .withUsername("bbaumeister")
                .withFirstname("Bob")
                .withLastname("Baumeister")
                .withEmail("baumeister@puzzle.ch")
                .build();

        userList = Arrays.asList(userAlice, userBob);
    }

    @Test
    void shouldReturnAllUsersCorrect() throws ResponseStatusException {
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        List<User> userList = userService.getAllUsers();

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
        List<User> userList = userService.getAllUsers();

        Assertions.assertThat(userList.size()).isEqualTo(0);
    }

}