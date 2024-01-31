package ch.puzzle.okr.models.alignment;

import ch.puzzle.okr.models.WriteableInterface;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class AlignmentView implements WriteableInterface {

    @Id
    private Long entityId;
    private String title;
    private Long teamId;
    private Long quarterId;
    private String alignmentType;
    private Long alignedObjectiveId;
    private Long targetObjectiveId;
    private Long targetKeyResultId;

    private transient boolean writeable;

    public AlignmentView() {
    }

    private AlignmentView(Builder builder) {
        entityId = builder.entityId;
        title = builder.title;
        teamId = builder.teamId;
        quarterId = builder.quarterId;
        alignmentType = builder.alignmentType;
        alignedObjectiveId = builder.alignedObjectiveId;
        targetObjectiveId = builder.targetObjectiveId;
        targetKeyResultId = builder.targetKeyResultId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public void setQuarterId(Long quarterId) {
        this.quarterId = quarterId;
    }

    public String getAlignmentType() {
        return alignmentType;
    }

    public void setAlignmentType(String alignmentType) {
        this.alignmentType = alignmentType;
    }

    public Long getAlignedObjectiveId() {
        return alignedObjectiveId;
    }

    public void setAlignedObjectiveId(Long alignedObjectiveId) {
        this.alignedObjectiveId = alignedObjectiveId;
    }

    public Long getTargetObjectiveId() {
        return targetObjectiveId;
    }

    public void setTargetObjectiveId(Long targetObjectiveId) {
        this.targetObjectiveId = targetObjectiveId;
    }

    public Long getTargetKeyResultId() {
        return targetKeyResultId;
    }

    public void setTargetKeyResultId(Long targetKeyResultId) {
        this.targetKeyResultId = targetKeyResultId;
    }

    @Override
    public boolean isWriteable() {
        return writeable;
    }

    @Override
    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    @Override
    public String toString() {
        return "AlignmentView{" +
                "entityId=" + entityId +
                ", title='" + title + '\'' +
                ", teamId=" + teamId +
                ", quarterId=" + quarterId +
                ", alignmentType='" + alignmentType + '\'' +
                ", alignedObjectiveId=" + alignedObjectiveId +
                ", targetObjectiveId=" + targetObjectiveId +
                ", targetKeyResultId=" + targetKeyResultId +
                ", writeable=" + writeable +
                '}';
    }

    public static final class Builder {
        private Long entityId;
        private String title;
        private Long teamId;
        private Long quarterId;
        private String alignmentType;
        private Long alignedObjectiveId;
        private Long targetObjectiveId;
        private Long targetKeyResultId;


        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withEntityId(Long entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withTeamId(Long teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder withQuarterId(Long quarterId) {
            this.quarterId = quarterId;
            return this;
        }

        public Builder withAlignmentType(String alignmentType) {
            this.alignmentType = alignmentType;
            return this;
        }

        public Builder withAlignedObjective(Long alignedObjectiveId) {
            this.alignedObjectiveId = alignedObjectiveId;
            return this;
        }

        public Builder withTargetObjectiveId(Long targetObjectiveId) {
            this.targetObjectiveId = targetObjectiveId;
            return this;
        }

        public Builder withTargetKeyResultId(Long targetKeyResultId) {
            this.targetKeyResultId = targetKeyResultId;
            return this;
        }

        public AlignmentView build() {
            return new AlignmentView(this);
        }
    }
}
