package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.checkIn.CheckIn;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckInRepository extends CrudRepository<CheckIn, Long> {
    List<CheckIn> findCheckInsByKeyResultIdOrderByCreatedOnDateDesc(@Param("keyResult_id") Long keyResultId);

    CheckIn findFirstByKeyResultIdOrderByCreatedOnDesc(@Param("keyResult_id") Long keyResultId);
}
