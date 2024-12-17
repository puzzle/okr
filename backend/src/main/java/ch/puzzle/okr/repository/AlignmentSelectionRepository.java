package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.models.alignment.AlignmentSelectionId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlignmentSelectionRepository extends ReadOnlyRepository<AlignmentSelection, AlignmentSelectionId> {

    @Query(value = "from AlignmentSelection where quarterId = :quarterId and teamId != :ignoredTeamId")
    List<AlignmentSelection> getAlignmentSelectionByQuarterIdAndTeamIdNot(@Param("quarterId") Long quarterId,
            @Param("ignoredTeamId") Long ignoredTeamId);
}
