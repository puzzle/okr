package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.ArchiveTeamDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.team.Team;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TeamMapper {

    public TeamDto toDto(Team team) {
        return new TeamDto(team.getId(), team.getVersion(), team.getName(), team.getDescription(), team.isWriteable(), team.getMarkedAsArchivedAt(), team.getStatus());
    }

    public Team toTeam(TeamDto teamDto) {
        return Team.Builder
                .builder()
                .withId(teamDto.id())
                .withVersion(teamDto.version())
                .withName(teamDto.name())
                .withDescription(teamDto.description())
                .withMarkedAsArchivedAt(teamDto.markedAsArchivedAt())
                .build();
    }

    public LocalDateTime toMarkedAsArchivedAt(ArchiveTeamDto archiveTeamDto) {
        return archiveTeamDto.markedAsArchivedAt();
    }
}
