package ch.puzzle.okr.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OverviewId implements Serializable {

    private Long teamId;
    private Long objectiveId;
    private Long keyResultId;
    private Long checkInId;

    public OverviewId() {
    }

    public OverviewId(Long teamId, Long objectiveId, Long keyResultId, Long checkInId) {
        this.teamId = teamId;
        this.objectiveId = objectiveId;
        this.keyResultId = keyResultId;
        this.checkInId = checkInId;
    }

    private OverviewId(Builder builder) {
        this(builder.teamId, builder.objectiveId, builder.keyResultId, builder.checkInId);
    }

    public static OverviewId of(Long teamId, Long objectiveId, Long keyResultId, Long checkInId) {
        return new OverviewId(teamId, objectiveId, keyResultId, checkInId);
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

    public Long getCheckInId() {
        return checkInId;
    }

    @Override
    public String toString() {
        return "OverviewId{" + "teamId=" + teamId + ", objectiveId=" + objectiveId + ", keyResultId=" + keyResultId
                + ", checkInId=" + checkInId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OverviewId that = (OverviewId) o;
        return Objects.equals(teamId, that.teamId) && Objects.equals(objectiveId, that.objectiveId)
                && Objects.equals(keyResultId, that.keyResultId) && Objects.equals(checkInId, that.checkInId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectiveId, keyResultId, checkInId);
    }

    public static final class Builder {
        private Long teamId;
        private Long objectiveId;
        private Long keyResultId;
        private Long checkInId;

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

        public Builder withCheckInId(Long checkInId) {
            this.checkInId = checkInId;
            return this;
        }

        public OverviewId build() {
            return new OverviewId(this);
        }
    }
}
