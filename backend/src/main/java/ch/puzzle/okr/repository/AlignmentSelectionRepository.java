package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.alignment.AlignmentSelection;
import ch.puzzle.okr.models.alignment.AlignmentSelectionId;
import ch.puzzle.okr.models.alignment.AlignmentView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlignmentSelectionRepository extends ReadOnlyRepository<AlignmentSelection, AlignmentSelectionId> {

    @Query(value = "from AlignmentSelection where quarterId = :quarter_id and teamId != :ignoredTeamId")
    List<AlignmentSelection> getAlignmentSelectionByQuarterIdAndTeamIdNot(@Param("quarter_id") Long quarterId,
            @Param("ignoredTeamId") Long ignoredTeamId);

    @Query(value = "from AlignmentView where alignedObjectiveQuarterId= :quarter_id and alignedObjectiveTeamId in :team_ids")
    List<AlignmentView> getAlignmentViewByQuarterId(@Param("quarter_id") Long quarterId,
            @Param("team_ids") List<Long> teamIds);
}
