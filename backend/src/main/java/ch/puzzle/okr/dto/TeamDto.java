package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.UserTeam;

import java.util.List;

public record TeamDto(Long id, int version, String name, List<UserTeam> userTeamList, boolean filterIsActive) {
}
