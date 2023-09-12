package ch.puzzle.okr.dto.checkIn;

import ch.puzzle.okr.models.User;

import java.time.LocalDateTime;

public class CheckInOrdinalDto extends CheckInDto {
    private String value;

    public CheckInOrdinalDto(Long id, String changeInfo, String initiatives, Integer confidence, Long keyResultId,
            User createdBy, LocalDateTime createdOn, LocalDateTime modifiedOn, String checkInType, String value) {
        super(id, changeInfo, initiatives, confidence, keyResultId, createdBy, createdOn, modifiedOn, checkInType);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
