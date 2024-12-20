package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.repository.AlignmentRepository;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class AlignmentValidationService
        extends
            ValidationBase<Alignment, Long, AlignmentRepository, AlignmentPersistenceService> {

    AlignmentValidationService(AlignmentPersistenceService persistenceService) {
        super(persistenceService);
    }

    @Override
    public void validateOnCreate(Alignment model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void validateOnUpdate(Long id, Alignment model) {
        throwExceptionWhenIdIsNull(id);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        validate(model);
    }
}
