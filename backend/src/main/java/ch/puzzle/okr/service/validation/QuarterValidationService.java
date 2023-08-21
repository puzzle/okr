package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Quarter;
import org.springframework.stereotype.Service;

@Service
public class QuarterValidationService extends ValidationBase<Quarter, Long> {

    @Override
    public void validateOnCreate(Quarter model) {

    }

    @Override
    public void validateOnUpdate(Long id, Quarter model) {

    }
}
