package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.State;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ObjectiveDto(Long id, int version, String title, Long teamId, Long quarterId, LocalDate quarterStartDate,
        LocalDate quarterEndDate, String description, State state, LocalDateTime createdOn, LocalDateTime modifiedOn,
        boolean writeable) {
}
