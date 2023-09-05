package ch.puzzle.okr.dto.overview;

import java.time.LocalDateTime;

public interface OverviewLastCheckInDto {

    Long id();

    Integer confidence();

    LocalDateTime createdOn();
}
