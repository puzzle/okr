package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.repository.CheckInRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInPersistenceService extends PersistenceBase<CheckIn, Long> {

    protected CheckInPersistenceService(CheckInRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "CheckIn";
    }

    public List<CheckIn> getCheckInsByKeyResultIdOrderByCheckInDateDesc(Long keyResultId) {
        return ((CheckInRepository) repository).findCheckInsByKeyResultIdOrderByModifiedOnDateDesc(keyResultId);
    }

    public CheckIn getLastCheckInOfKeyresults(Long keyResultId) {
        return ((CheckInRepository) repository).findFirstByKeyResultOrderByCreatedOn(keyResultId);
    }
}
