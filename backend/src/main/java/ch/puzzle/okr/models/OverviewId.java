package ch.puzzle.okr.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OverviewId implements Serializable {

    private Long teamId;
    private Long objectiveId;
    private Long keyResultId;
    private Long measureId;

    public OverviewId() {
    }

    public OverviewId(Long teamId, Long objectiveId, Long keyResultId, Long measureId) {
        this.teamId = teamId;
        this.objectiveId = objectiveId;
        this.keyResultId = keyResultId;
        this.measureId = measureId;
    }

    private OverviewId(Builder builder) {
        this(builder.teamId, builder.objectiveId, builder.keyResultId, builder.measureId);
    }

    public static OverviewId of(Long teamId, Long objectiveId, Long keyResultId, Long measureId) {
        return new OverviewId(teamId, objectiveId, keyResultId, measureId);
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getObjectiveId() {
        return objectiveId;
    }

    public Long getKeyResultId() {
        return keyResultId;
    }

    public Long getMeasureId() {
        return measureId;
    }

    @Override
    public String toString() {
        return "OverviewId{" + "teamId=" + teamId + ", objectiveId=" + objectiveId + ", keyResultId=" + keyResultId
                + ", measureId=" + measureId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OverviewId that = (OverviewId) o;
        return Objects.equals(teamId, that.teamId) && Objects.equals(objectiveId, that.objectiveId)
                && Objects.equals(keyResultId, that.keyResultId) && Objects.equals(measureId, that.measureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectiveId, keyResultId, measureId);
    }

    public static final class Builder {
        private Long teamId;
        private Long objectiveId;
        private Long keyResultId;
        private Long measureId;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withTeamId(Long teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder withObjectiveId(Long objectiveId) {
            this.objectiveId = objectiveId;
            return this;
        }

        public Builder withKeyResultId(Long keyResultId) {
            this.keyResultId = keyResultId;
            return this;
        }

        public Builder withMeasureId(Long measureId) {
            this.measureId = measureId;
            return this;
        }

        public OverviewId build() {
            return new OverviewId(this);
        }
    }
}
