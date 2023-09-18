package ch.puzzle.okr.mapper.checkIn;

import ch.puzzle.okr.dto.checkIn.CheckInAbstractDTO;
import ch.puzzle.okr.dto.checkIn.CheckInMetricDto;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInMetric;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CheckInMetricMapper {
    private final KeyResultBusinessService keyResultBusinessService;
    private final UserBusinessService userBusinessService;

    public CheckInMetricMapper(KeyResultBusinessService keyResultBusinessService,
            UserBusinessService userBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.userBusinessService = userBusinessService;
    }

    public CheckInMetricDto toDto(CheckInMetric checkInMetric) {
        return new CheckInMetricDto(checkInMetric.getId(), checkInMetric.getChangeInfo(),
                checkInMetric.getInitiatives(), checkInMetric.getConfidence(), checkInMetric.getKeyResult().getId(),
                checkInMetric.getCreatedOn(), checkInMetric.getModifiedOn(), checkInMetric.getCheckInType(),
                checkInMetric.getValue());
    }

    public CheckIn toCheckInMetric(CheckInAbstractDTO checkInAbstractDTO, Jwt jwt) {
        return CheckInMetric.Builder.builder().withValue(checkInAbstractDTO.getValue())
                .withId(checkInAbstractDTO.getId()).withChangeInfo(checkInAbstractDTO.getChangeInfo())
                .withInitiatives(checkInAbstractDTO.getInitiatives()).withConfidence(checkInAbstractDTO.getConfidence())
                .withKeyResult(keyResultBusinessService.getKeyResultById(checkInAbstractDTO.getKeyResultId()))
                .withCreatedBy(userBusinessService.getUserByAuthorisationToken(jwt))
                .withCreatedOn(checkInAbstractDTO.getCreatedOn()).withModifiedOn(checkInAbstractDTO.getModifiedOn())
                .withCheckInType(checkInAbstractDTO.getCheckInType()).build();
    }
}
