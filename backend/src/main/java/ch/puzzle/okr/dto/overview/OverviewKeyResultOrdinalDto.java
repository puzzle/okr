package ch.puzzle.okr.dto.overview;

public record OverviewKeyResultOrdinalDto(Long id, String title, String keyResultType, String commitZone,
        String targetZone, String strechGoal, OverviewLastCheckInOrdinalDto lastCheckIn)
        implements OverviewKeyResultDto {
}
