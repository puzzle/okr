package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.alignment.AlignmentKeyResultDto;
import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.models.AlignmentSelection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AlignmentSelectionMapper {

    public List<AlignmentObjectiveDto> toDto(List<AlignmentSelection> alignments) {
        List<AlignmentObjectiveDto> alignmentDtos = new ArrayList<>();
        alignments.forEach(alignment -> processObjectives(alignmentDtos, alignment));
        return alignmentDtos;
    }

    private Optional<AlignmentObjectiveDto> getMatchingObjectiveDto(Long objectiveId,
            List<AlignmentObjectiveDto> objectives) {
        return objectives.stream().filter(objectiveDto -> Objects.equals(objectiveId, objectiveDto.id())).findFirst();
    }

    private void processObjectives(List<AlignmentObjectiveDto> objectiveDtos, AlignmentSelection alignment) {
        Optional<AlignmentObjectiveDto> objectiveDto = getMatchingObjectiveDto(
                alignment.getAlignmentSelectionId().getObjectiveId(), objectiveDtos);
        if (objectiveDto.isPresent()) {
            processKeyResults(objectiveDto.get(), alignment);
        } else {
            AlignmentObjectiveDto alignmentObjectiveDto = createObjectiveDto(alignment);
            objectiveDtos.add(alignmentObjectiveDto);
            processKeyResults(alignmentObjectiveDto, alignment);
        }
    }

    private void processKeyResults(AlignmentObjectiveDto objectiveDto, AlignmentSelection alignment) {
        if (isValidId(alignment.getAlignmentSelectionId().getKeyResultId())) {
            objectiveDto.keyResults().add(createKeyResultDto(alignment));
        }
    }

    private AlignmentObjectiveDto createObjectiveDto(AlignmentSelection alignment) {
        return new AlignmentObjectiveDto(alignment.getAlignmentSelectionId().getObjectiveId(),
                alignment.getObjectiveTitle(), new ArrayList<>());
    }

    private AlignmentKeyResultDto createKeyResultDto(AlignmentSelection alignment) {
        return new AlignmentKeyResultDto(alignment.getAlignmentSelectionId().getKeyResultId(),
                alignment.getKeyResultTitle());
    }

    private boolean isValidId(Long id) {
        return id != null && id > -1;
    }
}
