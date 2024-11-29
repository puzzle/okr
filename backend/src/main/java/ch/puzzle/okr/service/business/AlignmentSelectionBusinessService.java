package ch.puzzle.okr.service.business;

import java.util.List;

import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.service.persistence.AlignmentSelectionPersistenceService;

import org.springframework.stereotype.Service;

@Service
public class AlignmentSelectionBusinessService {

    private final AlignmentSelectionPersistenceService alignmentSelectionPersistenceService;

    public AlignmentSelectionBusinessService(AlignmentSelectionPersistenceService alignmentSelectionPersistenceService) {
        this.alignmentSelectionPersistenceService = alignmentSelectionPersistenceService;
    }

    public List<AlignmentSelection> getAlignmentSelectionByQuarterIdAndTeamIdNot(Long quarterId, Long ignoredTeamId) {
        return alignmentSelectionPersistenceService.getAlignmentSelectionByQuarterIdAndTeamIdNot(quarterId,
                                                                                                 ignoredTeamId);
    }
}
