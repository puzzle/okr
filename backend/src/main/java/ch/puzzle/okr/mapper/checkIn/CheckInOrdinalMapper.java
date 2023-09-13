package ch.puzzle.okr.mapper.checkIn;

import ch.puzzle.okr.dto.checkIn.CheckInAbstractDTO;
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
                checkInOrdinal.getInitiatives(), checkInOrdinal.getConfidence(), checkInOrdinal.getKeyResult().getId(),
                checkInOrdinal.getCreatedBy(), checkInOrdinal.getCreatedOn(), checkInOrdinal.getModifiedOn(),
                checkInOrdinal.getCheckInType(), checkInOrdinal.getValue());
    }

    public CheckIn toCheckInOrdinal(CheckInAbstractDTO checkInAbstractDTO) {
        return CheckInOrdinal.Builder.builder().withValue(checkInAbstractDTO.getZone())
                .withId(checkInAbstractDTO.getId()).withChangeInfo(checkInAbstractDTO.getChangeInfo())
                .withInitiatives(checkInAbstractDTO.getInitiatives()).withConfidence(checkInAbstractDTO.getConfidence())
                .withKeyResult(keyResultBusinessService.getKeyResultById(checkInAbstractDTO.getKeyResultId()))
                .withCreatedBy(userBusinessService.getOwnerById(checkInAbstractDTO.getCreatedBy().getId()))
                .withCreatedOn(checkInAbstractDTO.getCreatedOn()).withModifiedOn(checkInAbstractDTO.getModifiedOn())
                .withCheckInType(checkInAbstractDTO.getCheckInType()).build();
    }
}
