package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.UserTeam;

import java.util.List;

public record UserDto(Long id, int version, String firstname, String lastname, String email, List<UserTeam> userTeamList, boolean isWriteable) {
}
