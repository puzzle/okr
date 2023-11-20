package ch.puzzle.okr.dto;

import java.util.List;

public record TeamDto(Long id, String name, List<OrganisationDto> organisations) {
}
