package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;
import ch.puzzle.okr.service.persistence.QuarterPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.BACK_LOG_QUARTER_LABEL;

@Service
public class QuarterValidationService
        extends ValidationBase<Quarter, Long, QuarterRepository, QuarterPersistenceService> {

    public QuarterValidationService(QuarterPersistenceService quarterPersistenceService) {
        super(quarterPersistenceService);
    }

    @Override
    public void validateOnCreate(Quarter model) {
        throw new IllegalCallerException("This method must not be called");
    }

    @Override
    public void validateOnUpdate(Long id, Quarter model) {
        throw new IllegalCallerException("This method must not be called because there is no update of quarters");
    }

    public static void throwExceptionWhenStartEndDateQuarterIsNull(Quarter model) {
        if (!model.getLabel().equals(BACK_LOG_QUARTER_LABEL)) {
            if (model.getStartDate() == null) {
                throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                        List.of("StartDate", model.getLabel()));
            } else if (model.getEndDate() == null) {
                throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                        List.of("EndDate", model.getLabel()));
            }

        }
    }

    public void validateOnGeneration(Quarter quarter) {
        if (quarter.getStartDate() == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                    List.of("StartDate", quarter.getLabel()));
        } else if (quarter.getEndDate() == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                    List.of("EndDate", quarter.getLabel()));
        }
    }
}
