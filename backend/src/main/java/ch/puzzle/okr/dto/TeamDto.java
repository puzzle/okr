package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.team.TeamStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TeamDto(Long id,
                      int version,
                      String name,
                      String description,
                      boolean isWriteable,
                      LocalDateTime markedAsArchivedAt,
                      @JsonProperty(access = JsonProperty.Access.READ_ONLY)
                      TeamStatus status) {
}
