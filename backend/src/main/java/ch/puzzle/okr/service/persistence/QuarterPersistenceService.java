package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class QuarterPersistenceService {
    private final QuarterRepository quarterRepository;

    public QuarterPersistenceService(QuarterRepository quarterRepository) {
        this.quarterRepository = quarterRepository;
    }

    public Quarter getQuarterById(Long quarterId) {
        if (quarterId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute quarter id");
        }
        return quarterRepository.findById(quarterId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                (String.format("Quarter with id %d not found", quarterId))));
    }

    public synchronized Quarter getOrCreateQuarter(String label) {
        Optional<Quarter> quarter = quarterRepository.findByLabel(label);
        return quarter.orElseGet(() -> quarterRepository.save(Quarter.Builder.builder().withLabel(label).build()));
    }

    void deleteQuarterById(Long quarterId) {
        quarterRepository.deleteById(quarterId);
    }
}
