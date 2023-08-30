package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QuarterPersistenceService extends PersistenceBase<Quarter, Long> {

    protected QuarterPersistenceService(QuarterRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Quarter";
    }

    public synchronized Quarter getOrCreateQuarter(String label) {
        Optional<Quarter> quarter = ((QuarterRepository) repository).findByLabel(label);
        return quarter.orElseGet(() -> repository.save(Quarter.Builder.builder().withLabel(label).build()));
    }

    void deleteQuarterById(Long quarterId) {
        repository.deleteById(quarterId);
    }

    public Optional<Quarter> getByLabel(String label) {
        return ((QuarterRepository) repository).findByLabel(label);
    }

    public List<Quarter> getMostCurrentQuarters() {
        return ((QuarterRepository) repository).getTop6ByOrderByStartDateDesc();
    }

    public Quarter getCurrentQuarter() {
        return ((QuarterRepository) repository).getActiveQuarter(LocalDate.now());
    }
}
