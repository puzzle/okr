package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.AlignmentSelection;
import ch.puzzle.okr.service.persistence.AlignmentSelectionPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlignmentSelectionBusinessService {

    private final AlignmentSelectionPersistenceService alignmentSelectionPersistenceService;

    public AlignmentSelectionBusinessService(
            AlignmentSelectionPersistenceService alignmentSelectionPersistenceService) {
        this.alignmentSelectionPersistenceService = alignmentSelectionPersistenceService;
    }

    public List<AlignmentSelection> getAlignmentSelectionByQuarterIdAndTeamIdNot(Long quarterId, Long ignoredTeamId) {
        return alignmentSelectionPersistenceService.getAlignmentSelectionByQuarterIdAndTeamIdNot(quarterId,
                ignoredTeamId);
    }
}
