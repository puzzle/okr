package ch.puzzle.okr.dto.overview;

public record OverviewKeyResultOrdinalDto(Long id, String title, String keyResultType, String commitZone,
        String targetZone, String stretchZone, OverviewLastCheckInOrdinalDto lastCheckIn)
        implements OverviewKeyResultDto {
}
