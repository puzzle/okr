package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.alignment.AlignmentView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlignmentViewRepository extends CrudRepository<AlignmentView, Long> {

    @Query(value = "SELECT * FROM alignment_view where quarter_id = :quarterId ", nativeQuery = true)
    List<AlignmentView> getAlignmentViewByQuarterId(@Param("quarterId") Long quarterId);
}
