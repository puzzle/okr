package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class MeasureMapper {
    KeyResultRepository keyResultRepository;
    UserRepository userRepository;

    public MeasureDto toDto(Measure measure) {
        return new MeasureDto(measure.getId(), measure.getKeyResult().getId(), measure.getValue(), measure.getChangeInfo(),
                measure.getInitiatives(), measure.getCreatedBy().getId(), measure.getCreatedOn());
    }

    public Measure toMeasure(MeasureDto measureDto) {
        Long keyResultId = measureDto.getKeyResultId();
        Long userId = measureDto.getCreatedById();
        KeyResult keyresult = keyResultRepository.findById(keyResultId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Keyresult with id %d not found", keyResultId))
        );
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", userId))
        );
        return Measure.Builder.builder()
                .withId(measureDto.getId())
                .withKeyResult(keyresult)
                .withValue(measureDto.getValue())
                .withChangeInfo(measureDto.getChangeInfo())
                .withInitiatives(measureDto.getInitiatives())
                .withCreatedBy(user)
                .withCreatedOn(measureDto.getCreatedOn())
                .build();
    }
}
