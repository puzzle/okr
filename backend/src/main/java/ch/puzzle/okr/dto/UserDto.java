package ch.puzzle.okr.dto;

import java.util.List;

public record UserDto(Long id, int version, String firstname, String lastname, String email,
        List<UserTeamDto> userTeamList, boolean isOkrChampion, boolean isWriteable) {
}
