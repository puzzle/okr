package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Alignment;
import ch.puzzle.okr.models.KeyResultAlignment;
import ch.puzzle.okr.models.ObjectiveAlignment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlignmentRepository extends CrudRepository<Alignment, Long> {

    List<Alignment> findByAlignedObjectiveId(Long alignedObjectiveId);

    @Query(value = "from KeyResultAlignment where targetKeyResult.id = :keyResultId")
    List<KeyResultAlignment> findByKeyResultAlignmentId(@Param("keyResultId") Long keyResultId);

    @Query(value = "from ObjectiveAlignment where targetObjective.id  = :objectiveId")
    List<ObjectiveAlignment> findByObjectiveAlignmentId(@Param("objectiveId") Long objectiveId);
}
