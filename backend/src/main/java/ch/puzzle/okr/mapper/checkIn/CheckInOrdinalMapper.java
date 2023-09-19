package ch.puzzle.okr.mapper.checkIn;

import ch.puzzle.okr.dto.checkIn.CheckInOrdinalDto;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInOrdinal;
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
        return CheckInOrdinal.Builder.builder().withZone(checkInOrdinalDto.zone()).withId(checkInOrdinalDto.id())
                .withChangeInfo(checkInOrdinalDto.changeInfo()).withInitiatives(checkInOrdinalDto.initiatives())
                .withConfidence(checkInOrdinalDto.confidence())
                .withKeyResult(keyResultBusinessService.getKeyResultById(checkInOrdinalDto.keyResultId()))
                .withCheckInType(
                        keyResultBusinessService.getKeyResultById(checkInOrdinalDto.keyResultId()).getKeyResultType())
                .build();
    }
}
