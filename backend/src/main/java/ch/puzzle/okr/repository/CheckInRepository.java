package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.checkin.CheckIn;

import java.util.List;

public interface CheckInRepository extends DeleteRepository<CheckIn, Long> {
    List<CheckIn> findCheckInsByKeyResultIdOrderByCreatedOnDesc(Long keyResultId);

    CheckIn findFirstByKeyResultIdOrderByCreatedOnDesc(Long keyResultId);
}
