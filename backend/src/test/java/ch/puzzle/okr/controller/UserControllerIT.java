package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.mapper.UserMapper;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.authorization.AuthorizationService;
import ch.puzzle.okr.service.authorization.UserAuthorizationService;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ch.puzzle.okr.controller.ActionControllerIT.SUCCESSFUL_UPDATE_BODY;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerIT {
    private static final String FIRSTNAME_1 = "Alice";
    private static final String LASTNAME_1 = "Wunderland";
    private static final String EMAIL_1 = "wunderland@puzzle.ch";
    private static final String FIRSTNAME_2 = "Bob";
    private static final String LASTNAME_2 = "Baumeister";
    private static final String EMAIL_2 = "baumeister@puzzle.ch";
    static User userAlice = User.Builder.builder().withId(2L).withFirstname(FIRSTNAME_1).withLastname(LASTNAME_1)
            .withEmail(EMAIL_1).build();
    static User userBob = User.Builder.builder().withId(9L).withFirstname(FIRSTNAME_2).withLastname(LASTNAME_2)
            .withEmail(EMAIL_2).build();
    static List<User> userList = Arrays.asList(userAlice, userBob);
    static UserDto userAliceDto = new UserDto(2L, 3, FIRSTNAME_1, LASTNAME_1, EMAIL_1, new ArrayList<>(), false);
    static UserDto userBobDto = new UserDto(9L, 4, FIRSTNAME_2, LASTNAME_2, EMAIL_2, new ArrayList<>(), false);
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserAuthorizationService userAuthorizationService;
    @MockBean
    private AuthorizationService authorizationService;
    @MockBean
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(userMapper.toDto(userAlice)).willReturn(userAliceDto);
        BDDMockito.given(userMapper.toDto(userBob)).willReturn(userBobDto);
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        BDDMockito.given(userAuthorizationService.getAllUsers()).willReturn(userList);

        mvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(2))).andExpect(jsonPath("$[0].firstname", Is.is(FIRSTNAME_1)))
                .andExpect(jsonPath("$[0].lastname", Is.is(LASTNAME_1)))
                .andExpect(jsonPath("$[0].email", Is.is(EMAIL_1))).andExpect(jsonPath("$[1].id", Is.is(9)))
                .andExpect(jsonPath("$[1].firstname", Is.is(FIRSTNAME_2)))
                .andExpect(jsonPath("$[1].lastname", Is.is(LASTNAME_2)))
                .andExpect(jsonPath("$[1].email", Is.is(EMAIL_2)));
    }

    @Test
    void shouldGetAllUsersIfNoUserExists() throws Exception {
        BDDMockito.given(userAuthorizationService.getAllUsers()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnCurrentUser() throws Exception {
        BDDMockito.given(authorizationService.updateOrAddAuthorizationUser())
                .willReturn(new AuthorizationUser(userAlice));
        BDDMockito.given(userMapper.toDto(userAlice)).willReturn(userAliceDto);

        mvc.perform(get("/api/v1/users/current").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()) //
                .andExpect(jsonPath("$", Matchers.aMapWithSize(7))) //
                .andExpect(jsonPath("$.id", Is.is(2))) //
                .andExpect(jsonPath("$.version", Is.is(3))) //
                .andExpect(jsonPath("$.firstname", Is.is(FIRSTNAME_1))) //
                .andExpect(jsonPath("$.lastname", Is.is(LASTNAME_1))) //
                .andExpect(jsonPath("$.email", Is.is(EMAIL_1))) //
                .andExpect(jsonPath("$.userTeamList", Matchers.empty())) //
                .andExpect(jsonPath("$.isOkrChampion", Is.is(false)));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        BDDMockito.given(userAuthorizationService.getById(2)).willReturn(userAlice);
        BDDMockito.given(userMapper.toDto(userAlice)).willReturn(userAliceDto);

        mvc.perform(get("/api/v1/users/2").contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(MockMvcResultMatchers.status().isOk()) //
                .andExpect(jsonPath("$", Matchers.aMapWithSize(7))) //
                .andExpect(jsonPath("$.id", Is.is(2))) //
                .andExpect(jsonPath("$.version", Is.is(3))) //
                .andExpect(jsonPath("$.firstname", Is.is(FIRSTNAME_1))) //
                .andExpect(jsonPath("$.lastname", Is.is(LASTNAME_1))) //
                .andExpect(jsonPath("$.email", Is.is(EMAIL_1))) //
                .andExpect(jsonPath("$.userTeamList", Matchers.empty())) //
                .andExpect(jsonPath("$.isOkrChampion", Is.is(false)));
    }

    @Test
    void shouldSetOkrChampion() throws Exception {
        BDDMockito.given(userAuthorizationService.setIsOkrChampion(2, true)).willReturn(userAlice);
        BDDMockito.given(userMapper.toDto(userAlice)).willReturn(userAliceDto);

        mvc.perform(put("/api/v1/users/2/isokrchampion/true") //
                .content(SUCCESSFUL_UPDATE_BODY) //
                .contentType(MediaType.APPLICATION_JSON) //
                .with(SecurityMockMvcRequestPostProcessors.csrf()) //
        ) //
                .andExpect(MockMvcResultMatchers.status().isOk()) //
                .andExpect(jsonPath("$", Matchers.aMapWithSize(7))) //
                .andExpect(jsonPath("$.id", Is.is(2))) //
                .andExpect(jsonPath("$.version", Is.is(3))) //
                .andExpect(jsonPath("$.firstname", Is.is(FIRSTNAME_1))) //
                .andExpect(jsonPath("$.lastname", Is.is(LASTNAME_1))) //
                .andExpect(jsonPath("$.email", Is.is(EMAIL_1))) //
                .andExpect(jsonPath("$.userTeamList", Matchers.empty())) //
                .andExpect(jsonPath("$.isOkrChampion", Is.is(false)));
    }

    @Test
    void shouldCreateUsers() throws Exception {
        BDDMockito.given(userAuthorizationService.createUsers(any())).willReturn(List.of(userAlice));
        BDDMockito.given(userMapper.toDtos(List.of(userAlice))).willReturn(List.of(userAliceDto));

        mvc.perform(post("/api/v1/users/createall") //
                .content(SUCCESSFUL_UPDATE_BODY) //
                .contentType(MediaType.APPLICATION_JSON) //
                .with(SecurityMockMvcRequestPostProcessors.csrf()) //
        ) //
                .andExpect(MockMvcResultMatchers.status().isOk()) //
                .andExpect(jsonPath("$", Matchers.hasSize(1))) //
                .andExpect(jsonPath("$[0].id", Is.is(2))) //
                .andExpect(jsonPath("$[0].version", Is.is(3))) //
                .andExpect(jsonPath("$[0].firstname", Is.is(FIRSTNAME_1))) //
                .andExpect(jsonPath("$[0].lastname", Is.is(LASTNAME_1))) //
                .andExpect(jsonPath("$[0].email", Is.is(EMAIL_1))) //
                .andExpect(jsonPath("$[0].userTeamList", Matchers.empty())) //
                .andExpect(jsonPath("$[0].isOkrChampion", Is.is(false)));
    }

}