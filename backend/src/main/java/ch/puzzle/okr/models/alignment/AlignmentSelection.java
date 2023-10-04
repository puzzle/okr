package ch.puzzle.okr.models.alignment;

import org.hibernate.annotations.Immutable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@Immutable
public class AlignmentSelection {

    @EmbeddedId
    private AlignmentSelectionId alignmentSelectionId;

    private Long teamId;
    private String teamName;
    private String objectiveTitle;
    private Long quarterId;
    private String quarterLabel;
    private String keyResultTitle;

    public AlignmentSelection() {
    }

    private AlignmentSelection(Builder builder) {
        alignmentSelectionId = builder.alignmentSelectionId;
        teamId = builder.teamId;
        teamName = builder.teamName;
        objectiveTitle = builder.objectiveTitle;
        quarterId = builder.quarterId;
        quarterLabel = builder.quarterLabel;
        keyResultTitle = builder.keyResultTitle;
    }

    public AlignmentSelectionId getAlignmentSelectionId() {
        return alignmentSelectionId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getObjectiveTitle() {
        return objectiveTitle;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public String getKeyResultTitle() {
        return keyResultTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AlignmentSelection alignmentSelection = (AlignmentSelection) o;
        return Objects.equals(alignmentSelectionId, alignmentSelection.alignmentSelectionId)
                && Objects.equals(teamId, alignmentSelection.teamId)
                && Objects.equals(objectiveTitle, alignmentSelection.objectiveTitle)
                && Objects.equals(quarterId, alignmentSelection.quarterId)
                && Objects.equals(keyResultTitle, alignmentSelection.keyResultTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alignmentSelectionId, teamId, objectiveTitle, quarterId, keyResultTitle);
    }

    @Override
    public String toString() {
        return "AlignmentSelection{" + "alignmentSelectionId=" + alignmentSelectionId + ", teamId='" + teamId
                + ", teamName='" + teamName + '\'' + ", objectiveTitle='" + objectiveTitle + '\'' + ", quarterId="
                + quarterId + ", quarterLabel='" + quarterLabel + '\'' + ", keyResultTitle='" + keyResultTitle + '\''
                + '}';
    }

    public static final class Builder {
        private AlignmentSelectionId alignmentSelectionId;

        private Long teamId;
        private String teamName;
        private String objectiveTitle;
        private Long quarterId;
        private String quarterLabel;
        private String keyResultTitle;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withAlignmentSelectionId(AlignmentSelectionId alignmentSelectionId) {
            this.alignmentSelectionId = alignmentSelectionId;
            return this;
        }

        public Builder withTeamId(Long teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder withTeamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder withObjectiveTitle(String objectiveTitle) {
            this.objectiveTitle = objectiveTitle;
            return this;
        }

        public Builder withQuarterId(Long quarterId) {
            this.quarterId = quarterId;
            return this;
        }

        public Builder withQuarterLabel(String quarterLabel) {
            this.quarterLabel = quarterLabel;
            return this;
        }

        public Builder withKeyResultTitle(String keyResultTitle) {
            this.keyResultTitle = keyResultTitle;
            return this;
        }

        public AlignmentSelection build() {
            return new AlignmentSelection(this);
        }
    }
}
