package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.NewUserDto;
import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    private static final long USER_TEAM_ID = 100L;
    private static final long ID = 0L;
    private static final int VERSION = 1;
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String EMAIL = "a@b.ch";
    private static final boolean IS_OKR_CHAMPION = true;

    private UserMapper userMapper;

    @InjectMocks
    private TeamMapper teamMapper;

    private final User user = User.Builder.builder() //
            .withId(ID) //
            .withVersion(VERSION) //
            .withFirstName(FIRST_NAME) //
            .withLastName(LAST_NAME) //
            .withEmail(EMAIL) //
            .withUserTeamList(List.of(UserTeam.Builder.builder() //
                    .withId(USER_TEAM_ID) //
                    .withTeam(Team.Builder.builder().build()) //
                    .build())) //
            .isOkrChampion(IS_OKR_CHAMPION) //
            .build();

    private final NewUserDto userDto = new NewUserDto(FIRST_NAME, LAST_NAME, EMAIL);

    @BeforeEach
    void setup() {
        userMapper = new UserMapper(teamMapper);
    }

    @DisplayName("Should throw NullPointerException when toDto() is called without teamList")
    @Test
    void shouldThrowExceptionWhenToDtoIsCalledWithoutTeamList() {
        // arrange
        User user = User.Builder.builder() //
                .withId(ID) //
                .withVersion(VERSION) //
                .withFirstName(FIRST_NAME) //
                .withLastName(LAST_NAME) //
                .withEmail(EMAIL) //
                .isOkrChampion(IS_OKR_CHAMPION) //
                .build();

        // act + assert
        assertThrows(NullPointerException.class, () -> userMapper.toDto(user));
    }

    @DisplayName("Should map User to Dto when toDto() is called")
    @Test
    void shouldMapUserToDtoWhenToDtoIsCalled() {
        // act
        UserDto userDto = userMapper.toDto(user);

        // assert
        assertNotNull(userDto);
        assertUserDto(user, userDto);
    }

    @DisplayName("Should map list of users to dtos when toDtos() is called")
    @Test
    void shouldMapListOfUsersToDtosWhenToDtosIsCalled() {
        // act
        List<UserDto> userDtos = userMapper.toDtos(List.of(user));

        // assert
        assertEquals(1, userDtos.size());
        assertUserDto(user, userDtos.get(0));
    }

    private void assertUserDto(User expected, UserDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getFirstName(), actual.firstName());
        assertEquals(expected.isOkrChampion(), actual.isOkrChampion());
        assertEquals(expected.getEmail(), actual.email());

        assertEquals(1, actual.userTeamList().size());
        assertEquals(expected.getUserTeamList().get(0).getId(), actual.userTeamList().get(0).id());
    }

    @DisplayName("Should map user-dto to user when toUser() is called")
    @Test
    void shouldMapUserDtoToUserWhenToUserIsCalled() {
        // act
        User user = userMapper.toUser(userDto);

        // assert
        assertNotNull(user);
        assertUser(userDto, user);
    }

    @DisplayName("Should map list of user-dtos to list of users when toUserList() is called")
    @Test
    void shouldMapUserDtoListToUserListWhenToUserListIsCalled() {
        // act
        List<User> users = userMapper.toUserList(List.of(userDto));

        // assert
        assertEquals(1, users.size());
        assertUser(userDto, users.get(0));
    }

    private void assertUser(NewUserDto expected, User actual) {
        assertEquals(expected.firstName(), actual.getFirstName());
        assertEquals(expected.lastName(), actual.getLastName());
        assertEquals(expected.email(), actual.getEmail());
    }

}
