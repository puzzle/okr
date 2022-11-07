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

@Service
public class MeasureService {
    KeyResultRepository keyResultRepository;
    UserRepository userRepository;
    MeasureRepository measureRepository;

    public Measure saveMeasure(Measure measure){
        return measureRepository.save(measure);
    }

    public KeyResult mapKeyResult(MeasureDto measureDto){
        Long keyResultId = measureDto.getKeyResultId();
        return keyResultRepository.findById(keyResultId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Keyresult with id %d not found", keyResultId))
        );
    }
    public User mapUser(MeasureDto measureDto){
        Long userId = measureDto.getCreatedById();
        return userRepository.findById(userId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", userId))
        );
    }

    public MeasureService(MeasureRepository measureRepository){this.measureRepository = measureRepository;}
}
