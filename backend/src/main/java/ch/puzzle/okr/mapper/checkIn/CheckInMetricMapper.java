package ch.puzzle.okr.mapper.checkIn;

import ch.puzzle.okr.dto.checkIn.CheckInMetricDto;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInMetric;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.stereotype.Component;

@Component
public class CheckInMetricMapper {
    private final KeyResultBusinessService keyResultBusinessService;

    public CheckInMetricMapper(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    public CheckInMetricDto toDto(CheckInMetric checkInMetric) {
        return new CheckInMetricDto(checkInMetric.getId(), checkInMetric.getChangeInfo(),
                checkInMetric.getInitiatives(), checkInMetric.getConfidence(), checkInMetric.getKeyResult().getId(),
                checkInMetric.getCreatedOn(), checkInMetric.getModifiedOn(), checkInMetric.getValue());
    }

    public CheckIn toCheckInMetric(CheckInMetricDto checkInMetricDto) {
        return CheckInMetric.Builder.builder().withValue(checkInMetricDto.valueMetric()).withId(checkInMetricDto.id())
                .withChangeInfo(checkInMetricDto.changeInfo()).withInitiatives(checkInMetricDto.initiatives())
                .withConfidence(checkInMetricDto.confidence())
                .withCheckInType(
                        keyResultBusinessService.getKeyResultById(checkInMetricDto.keyResultId()).getKeyResultType())
                .withKeyResult(keyResultBusinessService.getKeyResultById(checkInMetricDto.keyResultId())).build();
    }
}
