package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeamMapperTest {

    private static final long ID = 0L;
    private static final int VERSION = 1;
    private static final String NAME = "my team name";
    public static final boolean IS_WRITEABLE = true;

    @InjectMocks
    private TeamMapper teamMapper;

    @DisplayName("Should map team to dto when calling toDto()")
    @Test
    void shouldMapTeamToDto() {
        // arrange
        Team team = Team.Builder.builder() //
                .withId(ID) //
                .withVersion(VERSION) //
                .withName(NAME) //
                .build();
        team.setWriteable(IS_WRITEABLE);

        // act
        TeamDto teamDto = teamMapper.toDto(team);

        // assert
        assertNotNull(teamDto);
        assertTeamDto(team, teamDto);
    }

    private static void assertTeamDto(Team expected, TeamDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getName(), actual.name());
        assertEquals(expected.isWriteable(), actual.isWriteable());
    }

    @DisplayName("Should map dto to team when calling toTeam()")
    @Test
    void shouldMapDtoToTeam() {
        // arrange
        TeamDto teamDto = new TeamDto(ID, VERSION, NAME, IS_WRITEABLE);

        // act
        Team team = teamMapper.toTeam(teamDto);

        // assert
        assertNotNull(team);
        assertTeam(teamDto, team);
    }

    private static void assertTeam(TeamDto expected, Team actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.version(), actual.getVersion());
        assertEquals(expected.name(), actual.getName());
        assertFalse(actual.isWriteable());
    }

}
