package ch.puzzle.okr.dto.keyresult;

import java.time.LocalDateTime;

import ch.puzzle.okr.models.checkin.Zone;

public record KeyResultLastCheckInOrdinalDto(
        Long id, int version, Zone value, Integer confidence, LocalDateTime createdOn, String changeInfo,
        String initiatives
) implements KeyResultLastCheckIn {
}
