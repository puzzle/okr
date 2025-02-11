package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.checkin.CheckIn;
import java.util.List;

public interface CheckInRepository extends DeleteRepository<CheckIn, Long> {
    List<CheckIn> findCheckInsByKeyResultIdAndIsDeletedFalseOrderByCreatedOnDesc(Long keyResultId);

    CheckIn findFirstByKeyResultIdAndIsDeletedFalseOrderByCreatedOnDesc(Long keyResultId);
}
