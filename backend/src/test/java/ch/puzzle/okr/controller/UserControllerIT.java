package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.mapper.UserMapper;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.business.UserBusinessService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerIT {
    private static final String USERNAME_1 = "awunderland";
    private static final String FIRSTNAME_1 = "Alice";
    private static final String LASTNAME_1 = "Wunderland";
    private static final String EMAIL_1 = "wunderland@puzzle.ch";
    private static final String USERNAME_2 = "bbaumeister";
    private static final String FIRSTNAME_2 = "Bob";
    private static final String LASTNAME_2 = "Baumeister";
    private static final String EMAIL_2 = "baumeister@puzzle.ch";
    static User userAlice = User.Builder.builder().withId(2L).withUsername(USERNAME_1).withFirstname(FIRSTNAME_1)
            .withLastname(LASTNAME_1).withEmail(EMAIL_1).build();
    static User userBob = User.Builder.builder().withId(9L).withUsername(USERNAME_2).withFirstname(FIRSTNAME_2)
            .withLastname(LASTNAME_2).withEmail(EMAIL_2).build();
    static List<User> userList = Arrays.asList(userAlice, userBob);
    static UserDto userAliceDto = new UserDto(2L, USERNAME_1, FIRSTNAME_1, LASTNAME_1, EMAIL_1);
    static UserDto userBobDto = new UserDto(9L, USERNAME_2, FIRSTNAME_2, LASTNAME_2, EMAIL_2);
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserBusinessService userBusinessService;
    @MockBean
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(userMapper.toDto(userAlice)).willReturn(userAliceDto);
        BDDMockito.given(userMapper.toDto(userBob)).willReturn(userBobDto);
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        BDDMockito.given(userBusinessService.getAllUsers()).willReturn(userList);

        mvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(2))).andExpect(jsonPath("$[0].username", Is.is(USERNAME_1)))
                .andExpect(jsonPath("$[0].firstname", Is.is(FIRSTNAME_1)))
                .andExpect(jsonPath("$[0].lastname", Is.is(LASTNAME_1)))
                .andExpect(jsonPath("$[0].email", Is.is(EMAIL_1))).andExpect(jsonPath("$[1].id", Is.is(9)))
                .andExpect(jsonPath("$[1].username", Is.is(USERNAME_2)))
                .andExpect(jsonPath("$[1].firstname", Is.is(FIRSTNAME_2)))
                .andExpect(jsonPath("$[1].lastname", Is.is(LASTNAME_2)))
                .andExpect(jsonPath("$[1].email", Is.is(EMAIL_2)));
    }

    @Test
    void shouldGetAllUsersIfNoUserExists() throws Exception {
        BDDMockito.given(userBusinessService.getAllUsers()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }
}