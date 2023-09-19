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
        return getCheckInRepository().findCheckInsByKeyResultIdOrderByCreatedOnDesc(keyResultId);
    }

    public CheckIn getLastCheckInOfKeyResult(Long keyResultId) {
        return getCheckInRepository().findFirstByKeyResultIdOrderByCreatedOnDesc(keyResultId);
    }

    public CheckInRepository getCheckInRepository() {
        return (CheckInRepository) repository;
    }
}
