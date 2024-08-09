package ch.puzzle.okr.mapper;

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
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String EMAIL = "a@b.ch";
    private static final boolean IS_OKR_CHAMPION = true;

    private UserMapper userMapper;

    @InjectMocks
    private TeamMapper teamMapper;

    @BeforeEach
    void setup() {
        userMapper = new UserMapper(teamMapper);
    }

    @DisplayName("toDto() without TeamList throws NullPointerException")
    @Test
    void toDtoWithoutTeamListThrowsException() {
        // arrange
        User user = User.Builder.builder() //
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
        // arrange
        UserTeam userTeam = UserTeam.Builder.builder() //
                .withId(USER_TEAM_ID) //
                .withTeam(Team.Builder.builder().build()) //
                .build();
        User user = User.Builder.builder() //
                .withId(ID) //
                .withVersion(VERSION) //
                .withFirstname(FIRSTNAME) //
                .withLastname(LASTNAME) //
                .withEmail(EMAIL) //
                .withUserTeamList(List.of(userTeam)) //
                .withOkrChampion(IS_OKR_CHAMPION) //
                .build();

        // act
        UserDto userDto = userMapper.toDto(user);

        // assert
        assertNotNull(userDto);
        assertUserDto(user, userDto);
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
}
