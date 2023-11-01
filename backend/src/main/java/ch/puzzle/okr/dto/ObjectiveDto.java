package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.State;

import java.time.LocalDateTime;

public record ObjectiveDto(Long id, int version, String title, Long teamId, Long quarterId, String description,
        State state, LocalDateTime createdOn, LocalDateTime modifiedOn, boolean writeable) {
}
