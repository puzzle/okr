package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.MeasureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class MeasurePersistenceService {
    private final MeasureRepository measureRepository;

    public MeasurePersistenceService(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }

    public Measure saveMeasure(Measure measure) {
        if (measure.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Measure has already an Id");
        }
        return measureRepository.save(measure);
    }

    public Measure updateMeasure(Long id, Measure measure) {
        measureRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Measure with id %d not found", id)));
        return measureRepository.save(measure);
    }

    public List<Measure> getAllMeasures() {
        return (List<Measure>) measureRepository.findAll();
    }

    public Measure getMeasureById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute measure id");
        }
        return measureRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Measure with id %d not found", id)));
    }

    public List<MeasureRepository.MeasureValue> getMeasuresByKeyResultId(Long keyResultId) {
        return measureRepository.findMeasuresByKeyResultId(keyResultId);
    }

    public List<Measure> getMeasuresByKeyResultIdAndMeasureDate(Long keyResultId, Instant measureDate) {
        return measureRepository.findMeasuresByKeyResultIdAndMeasureDate(keyResultId, measureDate);
    }

    public List<Measure> getMeasuresByKeyResultIdOrderByMeasureDateDesc(Long keyResultId) {
        return measureRepository.findMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResultId);

    }

    public List<Measure> getLastMeasuresOfKeyresults(Long objectiveId) {
        return measureRepository.findLastMeasuresOfKeyresults(objectiveId);
    }

    public Measure getFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(Long keyResultId) {
        return measureRepository.findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResultId);
    }

    public void deleteMeasureById(Long measureId) {
        measureRepository.findById(measureId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Measure with measureId %d not found", measureId)));
        measureRepository.deleteById(measureId);
    }
}
