package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuarterPersistenceService extends PersistenceBase<Quarter, Long> {

    protected QuarterPersistenceService(QuarterRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Quarter";
    }

    public List<Quarter> getMostCurrentQuarters() {
        return getQuarterRepository().getTop6ByOrderByStartDateDesc();
    }

    public Quarter getCurrentQuarter() {
        return getQuarterRepository().getActiveQuarter(LocalDate.now());
    }

    private QuarterRepository getQuarterRepository() {
        return (QuarterRepository) repository;
    }
}
