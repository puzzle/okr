package ch.puzzle.okr.dto.keyresult;

import ch.puzzle.okr.models.checkin.Zone;

import java.time.LocalDateTime;

public record KeyResultLastCheckInOrdinalDto(Long id, Zone value, Integer confidence, LocalDateTime createdOn,
        String changeInfo, String initiatives) implements KeyResultLastCheckIn {
}
