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

    void deleteQuarterById(Long quarterId) {
        repository.deleteById(quarterId);
    }

    public List<Quarter> getMostCurrentQuarters() {
        return ((QuarterRepository) repository).getTop6ByOrderByStartDateDesc();
    }

    public Quarter getCurrentQuarter() {
        return ((QuarterRepository) repository).getActiveQuarter(LocalDate.now());
    }
}
