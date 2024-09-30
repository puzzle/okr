package ch.puzzle.okr.dto;

public record UserTeamDto(Long id, int version, TeamDto team, boolean isTeamAdmin) {
}
