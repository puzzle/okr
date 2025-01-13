package ch.puzzle.okr.mapper.checkin;

import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.stereotype.Component;

@Component
public class CheckInMetricMapper {
    private final KeyResultBusinessService keyResultBusinessService;

    public CheckInMetricMapper(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    public CheckInMetricDto toDto(CheckInMetric checkInMetric) {
        return new CheckInMetricDto(checkInMetric.getId(), //
                                    checkInMetric.getVersion(), //
                                    checkInMetric.getChangeInfo(), //
                                    checkInMetric.getInitiatives(), //
                                    checkInMetric.getConfidence(), //
                                    checkInMetric.getKeyResult().getId(), //
                                    checkInMetric.getCreatedBy().getFullName(), //
                                    checkInMetric.getCreatedOn(), //
                                    checkInMetric.getModifiedOn(), //
                                    checkInMetric.getValue(), //
                                    checkInMetric.isWriteable());
    }

    public CheckIn toCheckInMetric(CheckInMetricDto checkInMetricDto) {
        return CheckInMetric.Builder
                .builder() //
                .withValue(checkInMetricDto.value()) //
                .withId(checkInMetricDto.id()) //
                .withVersion(checkInMetricDto.version()) //
                .withChangeInfo(checkInMetricDto.changeInfo()) //
                .withInitiatives(checkInMetricDto.initiatives()) //
                .withConfidence(checkInMetricDto.confidence()) //
                .withCreatedOn(checkInMetricDto.createdOn()) //
                .withModifiedOn(checkInMetricDto.modifiedOn()) //
                .withKeyResult(keyResultBusinessService.getEntityById(checkInMetricDto.keyResultId())) //
                .build();
    }
}
