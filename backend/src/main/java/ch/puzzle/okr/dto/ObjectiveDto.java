package ch.puzzle.okr.dto;

import java.time.LocalDateTime;

import ch.puzzle.okr.models.State;

public record ObjectiveDto(
        Long id, int version, String title, Long teamId, Long quarterId, String quarterLabel, String description,
        State state, LocalDateTime createdOn, LocalDateTime modifiedOn, boolean writeable
) {
}
