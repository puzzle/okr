package ch.puzzle.okr.dto.keyresult;

import java.time.LocalDateTime;

public record KeyResultLastCheckInDto(Long id, Double value, Integer confidence, LocalDateTime createdOn,
        String comment) {
}
