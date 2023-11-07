package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.OrganisationState;

import java.util.List;

public record OrganisationDto(Long id, int version, String orgName, List<TeamDto> teams, OrganisationState state) {
}
