package ch.puzzle.okr.models.evaluation;

import ch.puzzle.okr.models.overview.OverviewId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;

import java.io.Serializable;


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
}
