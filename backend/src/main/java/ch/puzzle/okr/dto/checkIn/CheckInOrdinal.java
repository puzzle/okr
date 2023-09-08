package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkIn.Zone;

import java.time.LocalDateTime;

public class CheckInOrdinal extends CheckInDto{
    Zone value;
    public CheckInOrdinal(Long id, String changeInfo, String initiatives, Integer confidence, KeyResult keyResult, User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, Zone value) {
        super(id, changeInfo, initiatives, confidence, keyResult, createdBy, createdOn, modifiedOn, checkInType);
        this.value = value;
    }
}
