package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.alignment.AlignmentView;
import ch.puzzle.okr.repository.AlignmentViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.ALIGNMENT_VIEW;

@Service
public class AlignmentViewPersistenceService extends PersistenceBase<AlignmentView, Long, AlignmentViewRepository> {

    protected AlignmentViewPersistenceService(AlignmentViewRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return ALIGNMENT_VIEW;
    }

    public List<AlignmentView> getAlignmentViewListByQuarterId(Long quarterId) {
        return getRepository().getAlignmentViewByQuarterId(quarterId);
    }
}
