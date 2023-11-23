package ch.puzzle.okr.dto.overview;

public record OverviewTeamDto(Long id, int version, String name, boolean writable, boolean hasInActiveOrganisations) {
}
