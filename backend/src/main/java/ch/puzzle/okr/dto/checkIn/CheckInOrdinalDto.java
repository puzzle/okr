package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public class CheckInOrdinalDto extends CheckInDto {
    private String zone;

    public CheckInOrdinalDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
            User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, String zone) {
        super(id, changeInfo, initiatives, confidence, keyResultId, createdBy, createdOn, modifiedOn, checkInType);
        this.zone = zone;
    }

    public String getValue() {
        return zone;
    }

    public void setValue(String value) {
        this.zone = value;
    }
}
