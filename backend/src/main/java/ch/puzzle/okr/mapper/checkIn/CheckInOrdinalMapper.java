package ch.puzzle.okr.mapper.checkIn;

import ch.puzzle.okr.dto.checkIn.CheckInOrdinalDto;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInOrdinal;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.stereotype.Component;

@Component
public class CheckInOrdinalMapper {
    private final KeyResultBusinessService keyResultBusinessService;
    private final UserBusinessService userBusinessService;

    public CheckInOrdinalMapper(KeyResultBusinessService keyResultBusinessService,
            UserBusinessService userBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.userBusinessService = userBusinessService;
    }

    public CheckInOrdinalDto toDto(CheckInOrdinal checkInOrdinal) {
        return new CheckInOrdinalDto(checkInOrdinal.getId(), checkInOrdinal.getChangeInfo(),
                checkInOrdinal.getInitiatives(), checkInOrdinal.getConfidence(), checkInOrdinal.getKeyResult(),
                checkInOrdinal.getCreatedBy(), checkInOrdinal.getCreatedOn(), checkInOrdinal.getModifiedOn(),
                checkInOrdinal.getCheckInType(), checkInOrdinal.getValue());
    }

    public CheckIn toCheckInOrdinal(CheckInOrdinalDto checkInOrdinal) {
        return CheckInOrdinal.Builder.builder().withValue(checkInOrdinal.getValue()).withId(checkInOrdinal.getId())
                .withChangeInfo(checkInOrdinal.getChangeInfo()).withInitiatives(checkInOrdinal.getInitiatives())
                .withConfidence(checkInOrdinal.getConfidence())
                .withKeyResult(keyResultBusinessService.getKeyResultById(checkInOrdinal.getKeyResult().getId()))
                .withCreatedBy(userBusinessService.getOwnerById(checkInOrdinal.getCreatedBy().getId()))
                .withCreatedOn(checkInOrdinal.getCreatedOn()).withModifiedOn(checkInOrdinal.getModifiedOn())
                .withCheckInType(checkInOrdinal.getCheckInType()).build();
    }
}
