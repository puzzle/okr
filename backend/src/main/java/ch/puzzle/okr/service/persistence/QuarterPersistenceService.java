package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.QUARTER;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class QuarterPersistenceService extends PersistenceBase<Quarter, Long, QuarterRepository> {

    protected QuarterPersistenceService(QuarterRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return QUARTER;
    }

    public List<Quarter> getMostCurrentQuarters() {
        return getRepository().findTop6ByOrderByStartDateDescWithoutNullStartDate();
    }

    public Quarter findByLabel(String label) {
        return getRepository().findByLabel(label);
    }

    public Quarter getCurrentQuarter() {
        return getRepository().getActiveQuarter(LocalDate.now());
    }
}
