package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MeasureService {
    KeyResultRepository keyResultRepository;
    UserRepository userRepository;
    MeasureRepository measureRepository;

    public Measure saveMeasure(Measure measure) {
        if (measure.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Measure has already an Id");
        }
        this.checkMeasure(measure);
        return measureRepository.save(measure);
    }

    public Measure updateMeasure(Long id, Measure measure) {
        this.measureRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Measure with this id wasn't found"));
        this.checkMeasure(measure);
        return this.measureRepository.save(measure);
    }

    private void checkMeasure(Measure measure) {
        if(measure.getKeyResult() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given keyresult does not exist");
        }
        if(measure.getValue() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given measure does not have a value");
        }
        if(measure.getChangeInfo().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given change value is blank");
        }
        if(measure.getCreatedBy() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is null");
        }
        if(measure.getCreatedOn() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given creation date is null");
        }
    }

    public KeyResult mapKeyResult(MeasureDto measureDto) {
        Long keyResultId = measureDto.getKeyResultId();
        return keyResultRepository.findById(keyResultId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Keyresult with id %d not found", keyResultId))
        );
    }

    public User mapUser(MeasureDto measureDto) {
        Long userId = measureDto.getCreatedById();
        return userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", userId))
        );
    }

    public List<Measure> getAllMeasures() {
        return (List<Measure>) measureRepository.findAll();
    }


    public MeasureService(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }
}
