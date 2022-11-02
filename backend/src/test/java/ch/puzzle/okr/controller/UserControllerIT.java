package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.mapper.UserMapper;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.UserService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    static User userAlice = User.Builder.builder().withId(2L).withUsername("awunderland").withFirstname("Alice").withLastname("Wunderland").withEmail("wunderland@puzzle.ch").build();
    static User userBob = User.Builder.builder().withId(9L).withUsername("bbaumeister").withFirstname("Bob").withLastname("Baumeister").withEmail("baumeister@puzzle.ch").build();
    static List<User> userList = Arrays.asList(userAlice, userBob);

    static UserDto userAliceDto = new UserDto(2L, "awunderland", "Alice", "Wunderland", "wunderland@puzzle.ch");
    static UserDto userBobDto = new UserDto(9L, "bbaumeister", "Bob", "Baumeister", "baumeister@puzzle.ch");

    @BeforeEach
    void setUp() {
        // setup user mapper
        BDDMockito.given(userMapper.toDto(userAlice)).willReturn(userAliceDto);
        BDDMockito.given(userMapper.toDto(userBob)).willReturn(userBobDto);
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        BDDMockito.given(userService.getAllUsers()).willReturn(userList);

        mvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(2)))
                .andExpect(jsonPath("$[0].username", Is.is("awunderland")))
                .andExpect(jsonPath("$[0].firstname", Is.is("Alice")))
                .andExpect(jsonPath("$[0].lastname", Is.is("Wunderland")))
                .andExpect(jsonPath("$[0].email", Is.is("wunderland@puzzle.ch")))
                .andExpect(jsonPath("$[1].id", Is.is(9)))
                .andExpect(jsonPath("$[1].username", Is.is("bbaumeister")))
                .andExpect(jsonPath("$[1].firstname", Is.is("Bob")))
                .andExpect(jsonPath("$[1].lastname", Is.is("Baumeister")))
                .andExpect(jsonPath("$[1].email", Is.is("baumeister@puzzle.ch")))
        ;
    }

    @Test
    void shouldGetAllUsersIfNoUserExists() throws Exception {
        BDDMockito.given(userService.getAllUsers()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)))
        ;
    }
}