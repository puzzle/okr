package ch.puzzle.okr.dto.keyresult;

import java.time.LocalDateTime;

public record KeyResultLastCheckInOrdinalDto(Long id, String zone, Integer confidence, LocalDateTime createdOn,
        String changeInfo, String initiatives) implements KeyResultLastCheckIn {
}
