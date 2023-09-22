package ch.puzzle.okr.dto.overview;

public interface OverviewKeyResultDto {

    Long id();

    String title();

    String keyResultType();

    OverviewLastCheckInDto lastCheckIn();
}
