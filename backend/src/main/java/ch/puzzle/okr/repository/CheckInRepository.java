package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.checkin.CheckIn;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckInRepository extends CrudRepository<CheckIn, Long> {
    List<CheckIn> findCheckInsByKeyResultIdOrderByCreatedOnDesc(@Param("key_result_id") Long keyResultId);

    CheckIn findFirstByKeyResultIdOrderByCreatedOnDesc(@Param("key_result") Long keyResultId);
}
