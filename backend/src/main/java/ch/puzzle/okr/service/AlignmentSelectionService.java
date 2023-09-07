package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.mapper.AlignmentSelectionMapper;
import ch.puzzle.okr.service.business.AlignmentSelectionBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlignmentSelectionService {
    private final AlignmentSelectionMapper alignmentSelectionMapper;
    private final AlignmentSelectionBusinessService alignmentSelectionBusinessService;

    public AlignmentSelectionService(AlignmentSelectionMapper alignmentSelectionMapper,
            AlignmentSelectionBusinessService alignmentSelectionBusinessService) {
        this.alignmentSelectionMapper = alignmentSelectionMapper;
        this.alignmentSelectionBusinessService = alignmentSelectionBusinessService;
    }

    public List<AlignmentObjectiveDto> getAlignmentSelectionByQuarterIdAndTeamIdNot(Long quarterFilter,
            Long ignoredTeamId) {
        return alignmentSelectionMapper.toDto(alignmentSelectionBusinessService
                .getAlignmentSelectionByQuarterIdAndTeamIdNot(quarterFilter, ignoredTeamId));
    }
}
