package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.OrganisationState;

public record OrganisationDto(Long id, int version, String orgName, OrganisationState state) {
}
