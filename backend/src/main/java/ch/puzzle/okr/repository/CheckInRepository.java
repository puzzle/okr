package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.checkin.CheckIn;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CheckInRepository extends DeleteRepository<CheckIn, Long> {
    List<CheckIn> findCheckInsByKeyResultIdOrderByCreatedOnDesc(Long keyResultId);

    CheckIn findFirstByKeyResultIdOrderByCreatedOnDesc(Long keyResultId);
}
