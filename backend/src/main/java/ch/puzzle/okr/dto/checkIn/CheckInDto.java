package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public abstract class CheckInDto {
    Long id;
    String changeInfo;
    String initiatives;
    Integer confidence;
    KeyResult keyResult;
    User createdBy;
    LocalDateTime createdOn;
    LocalDateTime modifiedOn;
    String checkInType;

    public CheckInDto(Long id, String changeInfo, String initiatives, Integer confidence, KeyResult keyResult,
            User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType) {
        this.id = id;
        this.changeInfo = changeInfo;
        this.initiatives = initiatives;
        this.confidence = confidence;
        this.keyResult = keyResult;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.checkInType = checkInType;
    }
}
