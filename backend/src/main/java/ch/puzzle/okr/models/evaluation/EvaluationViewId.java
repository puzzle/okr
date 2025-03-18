package ch.puzzle.okr.models.evaluation;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class EvaluationViewId implements Serializable {
    private Long  teamId;
    private Long  quarterId;

    public EvaluationViewId(Long teamId, Long quarterId) {
        this.teamId = teamId;
        this.quarterId = quarterId;
    }

    public EvaluationViewId() {
    }

    private EvaluationViewId(Builder builder) {
        teamId = builder.teamId;
        quarterId = builder.quarterId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public static final class Builder {
        private Long teamId;
        private Long quarterId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withTeamId(Long val) {
            teamId = val;
            return this;
        }

        public Builder withQuarterId(Long val) {
            quarterId = val;
            return this;
        }

        public EvaluationViewId build() {
            return new EvaluationViewId(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EvaluationViewId that)) {
            return false;
        }
        return Objects.equals(getTeamId(), that.getTeamId()) && Objects.equals(getQuarterId(), that.getQuarterId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeamId(), getQuarterId());
    }
}
