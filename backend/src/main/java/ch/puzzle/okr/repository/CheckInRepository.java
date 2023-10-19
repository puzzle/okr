package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.checkin.CheckIn;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CheckInRepository extends CrudRepository<CheckIn, Long> {
    List<CheckIn> findCheckInsByKeyResultIdOrderByCreatedOnDesc(Long keyResultId);

    CheckIn findFirstByKeyResultIdOrderByCreatedOnDesc(Long keyResultId);
}
