package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuarterService {

    QuarterRepository quarterRepository;

    public QuarterService(QuarterRepository quarterRepository) {
        this.quarterRepository = quarterRepository;
    }

    public Quarter getQuarterById(Long quarterId) {
        if (quarterId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute quarter id");
        }

        return quarterRepository.findById(quarterId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, (String.format("Quarter with id %d not found", quarterId)))
        );
    }
}
