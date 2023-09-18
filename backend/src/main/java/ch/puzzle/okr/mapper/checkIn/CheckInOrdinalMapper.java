package ch.puzzle.okr.mapper.checkIn;

import ch.puzzle.okr.dto.checkIn.CheckInOrdinalDto;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInOrdinal;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.security.oauth2.jwt.Jwt;
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
                checkInOrdinal.getCreatedOn(), checkInOrdinal.getModifiedOn(), checkInOrdinal.getValue());
    }

    public CheckIn toCheckInOrdinal(CheckInOrdinalDto checkInOrdinalDto, Jwt jwt) {
        return CheckInOrdinal.Builder.builder().withValue(checkInOrdinalDto.zone()).withId(checkInOrdinalDto.id())
                .withChangeInfo(checkInOrdinalDto.changeInfo()).withInitiatives(checkInOrdinalDto.initiatives())
                .withConfidence(checkInOrdinalDto.confidence())
                .withKeyResult(keyResultBusinessService.getKeyResultById(checkInOrdinalDto.keyResultId()))
                .withCreatedBy(userBusinessService.getUserByAuthorisationToken(jwt))
                .withCheckInType(
                        keyResultBusinessService.getKeyResultById(checkInOrdinalDto.keyResultId()).getKeyResultType())
                .withCreatedOn(checkInOrdinalDto.createdOn()).withModifiedOn(checkInOrdinalDto.modifiedOn()).build();
    }
}
