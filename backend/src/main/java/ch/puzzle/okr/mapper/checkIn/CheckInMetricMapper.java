package ch.puzzle.okr.mapper.checkIn;

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

    public CheckIn toCheckInMetric(CheckInMetricDto checkInMetricDto, Jwt jwt) {
        return CheckInMetric.Builder.builder().withValue(checkInMetricDto.valueMetric()).withId(checkInMetricDto.id())
                .withChangeInfo(checkInMetricDto.changeInfo()).withInitiatives(checkInMetricDto.initiatives())
                .withConfidence(checkInMetricDto.confidence())
                .withKeyResult(keyResultBusinessService.getKeyResultById(checkInMetricDto.keyResultId()))
                .withCreatedBy(userBusinessService.getUserByAuthorisationToken(jwt))
                .withCreatedOn(checkInMetricDto.createdOn()).withModifiedOn(checkInMetricDto.modifiedOn())
                .withCheckInType(checkInMetricDto.checkInType()).build();
    }
}
