package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.repository.CheckInRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CheckInPersistenceService extends PersistenceBase<CheckIn, Long> {

    protected CheckInPersistenceService(CheckInRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return null;
    }

    public List<CheckIn> getMeasuresByKeyResultIdAndMeasureDate(Long keyResultId, Instant measureDate) {
        return ((CheckInRepository) repository).findMeasuresByKeyResultIdAndMeasureDate(keyResultId, measureDate);
    }

    public List<CheckIn> getMeasuresByKeyResultIdOrderByMeasureDateDesc(Long keyResultId) {
        return ((CheckInRepository) repository).findMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResultId);

    }

    public List<CheckIn> getLastMeasuresOfKeyresults(Long objectiveId) {
        return ((CheckInRepository) repository).findLastMeasuresOfKeyresults(objectiveId);
    }

    public CheckIn getFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(Long keyResultId) {
        return ((CheckInRepository) repository).findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResultId);
    }
}
