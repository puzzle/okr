package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkIn.Zone;
import ch.puzzle.okr.models.keyresult.KeyResult;

import java.time.LocalDateTime;

public class CheckInOrdinalDto extends CheckInDto {
    private Zone value;

    public CheckInOrdinalDto(Long id, String changeInfo, String initiatives, Integer confidence, KeyResult keyResult,
            User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, Zone value) {
        super(id, changeInfo, initiatives, confidence, keyResult, createdBy, createdOn, modifiedOn, checkInType);
        this.value = value;
    }

    public Zone getValue() {
        return value;
    }

    public void setValue(Zone value) {
        this.value = value;
    }
}
