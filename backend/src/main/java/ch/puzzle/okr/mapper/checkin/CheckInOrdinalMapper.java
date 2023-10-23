package ch.puzzle.okr.mapper.checkin;

import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.stereotype.Component;

@Component
public class CheckInOrdinalMapper {
    private final KeyResultBusinessService keyResultBusinessService;

    public CheckInOrdinalMapper(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    public CheckInOrdinalDto toDto(CheckInOrdinal checkInOrdinal) {
        return new CheckInOrdinalDto(checkInOrdinal.getId(), checkInOrdinal.getChangeInfo(),
                checkInOrdinal.getInitiatives(), checkInOrdinal.getConfidence(), checkInOrdinal.getKeyResult().getId(),
                checkInOrdinal.getCreatedOn(), checkInOrdinal.getModifiedOn(), checkInOrdinal.getZone());
    }

    public CheckIn toCheckInOrdinal(CheckInOrdinalDto checkInOrdinalDto) {
        return CheckInOrdinal.Builder.builder().withZone(checkInOrdinalDto.value()).withId(checkInOrdinalDto.id())
                .withChangeInfo(checkInOrdinalDto.changeInfo()).withInitiatives(checkInOrdinalDto.initiatives())
                .withConfidence(checkInOrdinalDto.confidence())
                .withKeyResult(keyResultBusinessService.getEntityById(checkInOrdinalDto.keyResultId())).build();
    }
}
