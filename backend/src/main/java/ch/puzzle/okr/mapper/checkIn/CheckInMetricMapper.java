package ch.puzzle.okr.mapper.checkIn;

import ch.puzzle.okr.dto.checkIn.CheckInMetricDto;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInMetric;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.stereotype.Component;

@Component
public class CheckInMetricMapper {
    private final KeyResultBusinessService keyResultBusinessService;
    private final UserBusinessService userBusinessService;

    public CheckInMetricMapper(KeyResultBusinessService keyResultBusinessService, UserBusinessService userBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.userBusinessService = userBusinessService;
    }

    public CheckInMetricDto toDto(CheckInMetric checkInMetric) {
        return new CheckInMetricDto(checkInMetric.getId(), checkInMetric.getChangeInfo(),
                checkInMetric.getInitiatives(), checkInMetric.getConfidence(), checkInMetric.getKeyResult(), checkInMetric.getCreatedBy(),
                checkInMetric.getCreatedOn(), checkInMetric.getModifiedOn(), checkInMetric.getCheckInType(), checkInMetric.getValue());
    }

    public CheckIn toCheckInMetric(CheckInMetricDto checkInMetricDto) {
        return CheckInMetric.Builder.builder()
                .withValue(checkInMetricDto.getValue())
                .withId(checkInMetricDto.getId())
                .withChangeInfo(checkInMetricDto.getChangeInfo())
                .withInitiatives(checkInMetricDto.getInitiatives())
                .withConfidence(checkInMetricDto.getConfidence())
                .withKeyResult(keyResultBusinessService.getKeyResultById(checkInMetricDto.getKeyResult().getId()))
                .withCreatedBy(userBusinessService.getOwnerById(checkInMetricDto.getCreatedBy().getId()))
                .withCreatedOn(checkInMetricDto.getCreatedOn())
                .withModifiedOn(checkInMetricDto.getModifiedOn())
                .withCheckInType(checkInMetricDto.getCheckInType())
                .build();
    }
}
