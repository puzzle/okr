package ch.puzzle.okr.repository;

import java.util.List;

import ch.puzzle.okr.models.checkin.CheckIn;

import org.springframework.data.repository.CrudRepository;

public interface CheckInRepository extends CrudRepository<CheckIn, Long> {
    List<CheckIn> findCheckInsByKeyResultIdOrderByCreatedOnDesc(Long keyResultId);

    CheckIn findFirstByKeyResultIdOrderByCreatedOnDesc(Long keyResultId);
}
