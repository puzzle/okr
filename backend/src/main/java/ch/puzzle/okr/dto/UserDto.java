package ch.puzzle.okr.dto;

import java.util.List;

public record UserDto(Long id, int version, String firstName, String lastName, String email,
        List<UserTeamDto> userTeamList, boolean isOkrChampion) {
}
