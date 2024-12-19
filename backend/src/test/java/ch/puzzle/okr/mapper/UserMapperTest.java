package ch.puzzle.okr.mapper;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.okr.dto.NewUserDto;
import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    private static final long USER_TEAM_ID = 100L;
    private static final long ID = 0L;
    private static final int VERSION = 1;
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String EMAIL = "a@b.ch";
    private static final boolean IS_OKR_CHAMPION = true;

    private UserMapper userMapper;

    @InjectMocks
    private TeamMapper teamMapper;

    private final User user = User.Builder
            .builder() //
            .withId(ID) //
            .withVersion(VERSION) //
            .withFirstname(FIRSTNAME) //
            .withLastname(LASTNAME) //
            .withEmail(EMAIL) //
            .withUserTeamList(List
                    .of(UserTeam.Builder
                            .builder() //
                            .withId(USER_TEAM_ID) //
                            .withTeam(Team.Builder.builder().build()) //
                            .build())) //
            .withOkrChampion(IS_OKR_CHAMPION) //
            .build();

    private final NewUserDto userDto = new NewUserDto(FIRSTNAME, LASTNAME, EMAIL);

    @BeforeEach
    void setup() {
        userMapper = new UserMapper(teamMapper);
    }

    @DisplayName("toDto() without TeamList throws NullPointerException")
    @Test
    void toDtoWithoutTeamListThrowsException() {
        // arrange
        User user = User.Builder
                .builder() //
                .withId(ID) //
                .withVersion(VERSION) //
                .withFirstname(FIRSTNAME) //
                .withLastname(LASTNAME) //
                .withEmail(EMAIL) //
                .withOkrChampion(IS_OKR_CHAMPION) //
                .build();

        // act + assert
        assertThrows(NullPointerException.class, () -> userMapper.toDto(user));
    }

    @DisplayName("toDto() should map User to Dto")
    @Test
    void toDtoShouldMapUserToDto() {
        // act
        UserDto userDto = userMapper.toDto(user);

        // assert
        assertNotNull(userDto);
        assertUserDto(user, userDto);
    }

    @DisplayName("toDtos() should map list of Users to Dtos")
    @Test
    void toDtosShouldMapListOfUsersToDtos() {
        // act
        List<UserDto> userDtos = userMapper.toDtos(List.of(user));

        // assert
        assertEquals(1, userDtos.size());
        assertUserDto(user, userDtos.get(0));
    }

    private void assertUserDto(User expected, UserDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getFirstname(), actual.firstname());
        assertEquals(expected.isOkrChampion(), actual.isOkrChampion());
        assertEquals(expected.getEmail(), actual.email());

        assertEquals(1, actual.userTeamList().size());
        assertEquals(expected.getUserTeamList().get(0).getId(), actual.userTeamList().get(0).id());
    }

    @DisplayName("toUser() should map UserDto to User")
    @Test
    void toUserShouldMapUserDtoToUser() {
        // act
        User user = userMapper.toUser(userDto);

        // assert
        assertNotNull(user);
        assertUser(userDto, user);
    }

    @DisplayName("toUserList() should map List of UserDtos to List of Users")
    @Test
    void toUserListShouldMapUserDtoListToUserList() {
        // act
        List<User> users = userMapper.toUserList(List.of(userDto));

        // assert
        assertEquals(1, users.size());
        assertUser(userDto, users.get(0));
    }

    private void assertUser(NewUserDto expected, User actual) {
        assertEquals(expected.firstname(), actual.getFirstname());
        assertEquals(expected.lastname(), actual.getLastname());
        assertEquals(expected.email(), actual.getEmail());
    }

}
