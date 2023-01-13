package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MeasureService {
    private final KeyResultRepository keyResultRepository;
    private final MeasureRepository measureRepository;
    private final ProgressService progressService;

    public MeasureService(KeyResultRepository keyResultRepository, MeasureRepository measureRepository,
            ProgressService progressService) {
        this.keyResultRepository = keyResultRepository;
        this.measureRepository = measureRepository;
        this.progressService = progressService;
    }

    public Measure saveMeasure(Measure measure) {
        if (measure.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Measure has already an Id");
        }
        this.checkMeasure(measure);
        Measure createdMeasure = this.measureRepository.save(measure);
        this.progressService.updateObjectiveProgress(createdMeasure.getKeyResult().getObjective().getId());
        return createdMeasure;
    }

    public Measure updateMeasure(Long id, Measure measure) {
        this.measureRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Measure with id %d not found", id)));
        this.checkMeasure(measure);
        Measure createdMeasure = this.measureRepository.save(measure);
        this.progressService.updateObjectiveProgress(createdMeasure.getKeyResult().getObjective().getId());
        return createdMeasure;
    }

    private void checkMeasure(Measure measure) {
        if (measure.getKeyResult() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given keyresult does not exist");
        }
        if (measure.getValue() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given measure does not have a value");
        }
        if (measure.getChangeInfo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given change value is blank");
        }
        if (measure.getCreatedBy() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is null");
        }
        if (measure.getCreatedOn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given creation date is null");
        }
        if (measure.getMeasureDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given measure date is null");
        }
    }

    public KeyResult mapKeyResult(MeasureDto measureDto) {
        Long keyResultId = measureDto.getKeyResultId();
        return keyResultRepository.findById(keyResultId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Keyresult with id %d not found", keyResultId)));
    }

    public List<Measure> getAllMeasures() {
        return (List<Measure>) measureRepository.findAll();
    }

    public Measure getMeasureById(long id) {
        return this.measureRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Measure with id %d not found", id)));
    }

    public void deleteMeasureById(Long measureId) {
        measureRepository.findById(measureId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Measure with measureId %d not found", measureId)));

        measureRepository.deleteById(measureId);
    }
}
