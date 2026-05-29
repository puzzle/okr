package ch.puzzle.okr.mapper;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.team.Team;
import ch.puzzle.okr.models.team.TeamStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class TeamMapperTest {

    private static final long ID = 0L;
    private static final int VERSION = 1;
    private static final String NAME = "my team name";
    private static final String DESCRIPTION = "my team description that is quite long";
    public static final boolean IS_WRITEABLE = true;
    public static final LocalDate MARKED_AS_ARCHIVED_AT = null;
    public static final TeamStatus STATUS = TeamStatus.ACTIVE;

    @InjectMocks
    private TeamMapper teamMapper;

    @DisplayName("Should map team to dto when calling toDto()")
    @Test
    void shouldMapTeamToDto() {
        // arrange
        Team team = Team.Builder
                .builder() //
                .withId(ID) //
                .withVersion(VERSION) //
                .withName(NAME) //
                .withDescription(DESCRIPTION)
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
        assertEquals(expected.getDescription(), actual.description());
        assertEquals(expected.isWriteable(), actual.isWriteable());
        assertEquals(expected.getMarkedAsArchivedAt(), actual.markedAsArchivedAt());
        assertEquals(expected.getStatus(), actual.status());
    }

    @DisplayName("Should map dto to team when calling toTeam()")
    @Test
    void shouldMapDtoToTeam() {
        // arrange
        TeamDto teamDto = new TeamDto(ID, VERSION, NAME, DESCRIPTION, IS_WRITEABLE, MARKED_AS_ARCHIVED_AT, STATUS);

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
        assertEquals(expected.description(), actual.getDescription());
        assertFalse(actual.isWriteable());
        assertEquals(expected.markedAsArchivedAt(), actual.getMarkedAsArchivedAt());
        assertEquals(expected.status(), actual.getStatus());
    }

}
