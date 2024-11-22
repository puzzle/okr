package ch.puzzle.okr.test.dto.builder;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.models.State;

import java.time.LocalDateTime;

public class ObjectiveDtoBuilder {
    private Long id;
    private int version;
    private String title;
    private Long teamId;
    private Long quarterId;
    private String quarterLabel;
    private String description;
    private State state;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private boolean writeable;

    private ObjectiveDtoBuilder() {
    }

    public static ObjectiveDtoBuilder builder() {
        return new ObjectiveDtoBuilder();
    }

    public ObjectiveDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ObjectiveDtoBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public ObjectiveDtoBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ObjectiveDtoBuilder withTeamId(Long teamId) {
        this.teamId = teamId;
        return this;
    }

    public ObjectiveDtoBuilder withQuarterId(Long quarterId) {
        this.quarterId = quarterId;
        return this;
    }

    public ObjectiveDtoBuilder withQuarterLabel(String quarterLabel) {
        this.quarterLabel = quarterLabel;
        return this;
    }

    public ObjectiveDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ObjectiveDtoBuilder withState(State state) {
        this.state = state;
        return this;
    }

    public ObjectiveDtoBuilder withCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public ObjectiveDtoBuilder withModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public ObjectiveDtoBuilder withWriteable(boolean writeable) {
        this.writeable = writeable;
        return this;
    }

    public ObjectiveDto build() {
        return new ObjectiveDto(id, version, title, teamId, quarterId, quarterLabel, description, state, createdOn,
                modifiedOn, writeable);
    }

}
