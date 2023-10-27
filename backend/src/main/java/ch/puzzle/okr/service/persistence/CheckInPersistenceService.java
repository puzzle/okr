package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.repository.CheckInRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInPersistenceService extends PersistenceBase<CheckIn, Long, CheckInRepository> {

    protected CheckInPersistenceService(CheckInRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "CheckIn";
    }

    public List<CheckIn> getCheckInsByKeyResultIdOrderByCheckInDateDesc(Long keyResultId) {
        return getRepository().findCheckInsByKeyResultIdOrderByCreatedOnDesc(keyResultId);
    }

    public CheckIn getLastCheckInOfKeyResult(Long keyResultId) {
        return getRepository().findFirstByKeyResultIdOrderByCreatedOnDesc(keyResultId);
    }
}
