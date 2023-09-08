package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.CheckInRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CheckInPersistenceService extends PersistenceBase<Measure, Long> {

    protected CheckInPersistenceService(CheckInRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return null;
    }

    public List<Measure> getMeasuresByKeyResultIdAndMeasureDate(Long keyResultId, Instant measureDate) {
        return ((CheckInRepository) repository).findMeasuresByKeyResultIdAndMeasureDate(keyResultId, measureDate);
    }

    public List<Measure> getMeasuresByKeyResultIdOrderByMeasureDateDesc(Long keyResultId) {
        return ((CheckInRepository) repository).findMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResultId);

    }

    public List<Measure> getLastMeasuresOfKeyresults(Long objectiveId) {
        return ((CheckInRepository) repository).findLastMeasuresOfKeyresults(objectiveId);
    }

    public Measure getFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(Long keyResultId) {
        return ((CheckInRepository) repository).findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResultId);
    }
}
