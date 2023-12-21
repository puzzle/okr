package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;

public record UserTeamDto(
        Long id,
        int version,
        User user,
        Team team,
        boolean isAdmin
        ) {}
