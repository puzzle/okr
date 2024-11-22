package ch.puzzle.okr.test.dto.builder;

import ch.puzzle.okr.dto.CompletedDto;
import ch.puzzle.okr.dto.ObjectiveDto;

public class CompletedDtoBuilder {
    private Long id;
    private ObjectiveDto objective;
    private String comment;

    private CompletedDtoBuilder() {
    }

    public static CompletedDtoBuilder builder() {
        return new CompletedDtoBuilder();
    }

    public CompletedDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompletedDtoBuilder withObjectiveDto(ObjectiveDto objective) {
        this.objective = objective;
        return this;
    }

    public CompletedDtoBuilder withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public CompletedDto build() {
        return new CompletedDto(id, objective, comment);
    }
}
