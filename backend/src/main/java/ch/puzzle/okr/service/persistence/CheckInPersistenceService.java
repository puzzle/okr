package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.CHECK_IN;

import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.repository.CheckInRepository;
import ch.puzzle.okr.service.persistence.customCrud.SoftDelete;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CheckInPersistenceService extends PersistenceBase<CheckIn, Long, CheckInRepository> {

    protected CheckInPersistenceService(CheckInRepository repository) {
        super(repository, new SoftDelete<>());
    }

    @Override
    public String getModelName() {
        return CHECK_IN;
    }

    public List<CheckIn> getCheckInsByKeyResultIdOrderByCheckInDateDesc(Long keyResultId) {
        return getRepository().findCheckInsByKeyResultIdAndIsDeletedFalseOrderByCreatedOnDesc(keyResultId);
    }

    public CheckIn getLastCheckInOfKeyResult(Long keyResultId) {
        return getRepository().findFirstByKeyResultIdAndIsDeletedFalseOrderByCreatedOnDesc(keyResultId);
    }
}
