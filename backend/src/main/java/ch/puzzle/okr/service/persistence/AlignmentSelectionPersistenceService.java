package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.repository.AlignmentSelectionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AlignmentSelectionPersistenceService {

    private final AlignmentSelectionRepository alignmentSelectionRepository;

    public AlignmentSelectionPersistenceService(AlignmentSelectionRepository alignmentSelectionRepository) {
        this.alignmentSelectionRepository = alignmentSelectionRepository;
    }

    public List<AlignmentSelection> getAlignmentSelectionByQuarterIdAndTeamIdNot(Long quarterId, Long ignoredTeamId) {
        return alignmentSelectionRepository.getAlignmentSelectionByQuarterIdAndTeamIdNot(quarterId, ignoredTeamId);
    }
}
