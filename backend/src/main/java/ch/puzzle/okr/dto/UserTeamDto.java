package ch.puzzle.okr.dto;

public record UserTeamDto(Long id, int version, TeamDto teamDto, boolean isTeamAdmin) {
}
