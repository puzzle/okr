package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuarterPersistenceService extends PersistenceBase<Quarter, Long, QuarterRepository> {

    protected QuarterPersistenceService(QuarterRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Quarter";
    }

    public List<Quarter> getMostCurrentQuarters() {
        return getRepository().getTop6ByOrderByStartDateDesc();
    }

    public Quarter getCurrentQuarter() {
        return getRepository().getActiveQuarter(LocalDate.now());
    }
}
