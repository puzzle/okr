package ch.puzzle.okr.models.alignment;

import ch.puzzle.okr.models.WriteableInterface;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class AlignmentView implements WriteableInterface {

    @Id
    private Long id;
    private Long alignedObjectiveId;
    private String alignedObjectiveTitle;
    private Long alignedObjectiveTeamId;
    private String alignedObjectiveTeamName;
    private Long alignedObjectiveQuarterId;
    private String alignmentType;
    private Long targetKeyResultId;
    private String targetKeyResultTitle;
    private Long targetKeyResultTeamId;
    private String targetKeyResultTeamName;
    private Long targetObjectiveId;
    private String targetObjectiveTitle;
    private Long targetObjectiveTeamId;
    private String targetObjectiveTeamName;

    private transient boolean writeable;

    public AlignmentView() {
    }

    private AlignmentView(Builder builder) {
        setId(builder.id);
        setAlignedObjectiveId(builder.alignedObjectiveId);
        setAlignedObjectiveTitle(builder.alignedObjectiveTitle);
        setAlignedObjectiveTeamId(builder.alignedObjectiveTeamId);
        setAlignedObjectiveTeamName(builder.alignedObjectiveTeamName);
        setAlignedObjectiveQuarterId(builder.alignedObjectiveQuarterId);
        setAlignmentType(builder.alignmentType);
        setTargetKeyResultId(builder.targetKeyResultId);
        setTargetKeyResultTitle(builder.targetKeyResultTitle);
        setTargetKeyResultTeamId(builder.targetKeyResultTeamId);
        setTargetKeyResultTeamName(builder.targetKeyResultTeamName);
        setTargetObjectiveId(builder.targetObjectiveId);
        setTargetObjectiveTitle(builder.targetObjectiveTitle);
        setTargetObjectiveTeamId(builder.targetObjectiveTeamId);
        setTargetObjectiveTeamName(builder.targetObjectiveTeamName);
        setWriteable(builder.writeable);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlignedObjectiveId() {
        return alignedObjectiveId;
    }

    public void setAlignedObjectiveId(Long alignedObjectiveId) {
        this.alignedObjectiveId = alignedObjectiveId;
    }

    public String getAlignedObjectiveTitle() {
        return alignedObjectiveTitle;
    }

    public void setAlignedObjectiveTitle(String alignedObjectiveTitle) {
        this.alignedObjectiveTitle = alignedObjectiveTitle;
    }

    public Long getAlignedObjectiveTeamId() {
        return alignedObjectiveTeamId;
    }

    public void setAlignedObjectiveTeamId(Long alignedObjectiveTeamId) {
        this.alignedObjectiveTeamId = alignedObjectiveTeamId;
    }

    public String getAlignedObjectiveTeamName() {
        return alignedObjectiveTeamName;
    }

    public void setAlignedObjectiveTeamName(String alignedObjectiveTeamName) {
        this.alignedObjectiveTeamName = alignedObjectiveTeamName;
    }

    public Long getAlignedObjectiveQuarterId() {
        return alignedObjectiveQuarterId;
    }

    public void setAlignedObjectiveQuarterId(Long alignedObjectiveQuarterId) {
        this.alignedObjectiveQuarterId = alignedObjectiveQuarterId;
    }

    public String getAlignmentType() {
        return alignmentType;
    }

    public void setAlignmentType(String alignmentType) {
        this.alignmentType = alignmentType;
    }

    public Long getTargetKeyResultId() {
        return targetKeyResultId;
    }

    public void setTargetKeyResultId(Long targetKeyResultId) {
        this.targetKeyResultId = targetKeyResultId;
    }

    public String getTargetKeyResultTitle() {
        return targetKeyResultTitle;
    }

    public void setTargetKeyResultTitle(String targetKeyResultTitle) {
        this.targetKeyResultTitle = targetKeyResultTitle;
    }

    public Long getTargetKeyResultTeamId() {
        return targetKeyResultTeamId;
    }

    public void setTargetKeyResultTeamId(Long targetKeyResultTeamId) {
        this.targetKeyResultTeamId = targetKeyResultTeamId;
    }

    public String getTargetKeyResultTeamName() {
        return targetKeyResultTeamName;
    }

    public void setTargetKeyResultTeamName(String targetKeyResultTeamName) {
        this.targetKeyResultTeamName = targetKeyResultTeamName;
    }

    public Long getTargetObjectiveId() {
        return targetObjectiveId;
    }

    public void setTargetObjectiveId(Long targetObjectiveId) {
        this.targetObjectiveId = targetObjectiveId;
    }

    public String getTargetObjectiveTitle() {
        return targetObjectiveTitle;
    }

    public void setTargetObjectiveTitle(String targetObjectiveTitle) {
        this.targetObjectiveTitle = targetObjectiveTitle;
    }

    public Long getTargetObjectiveTeamId() {
        return targetObjectiveTeamId;
    }

    public void setTargetObjectiveTeamId(Long targetObjectiveTeamId) {
        this.targetObjectiveTeamId = targetObjectiveTeamId;
    }

    public String getTargetObjectiveTeamName() {
        return targetObjectiveTeamName;
    }

    public void setTargetObjectiveTeamName(String targetObjectiveTeamName) {
        this.targetObjectiveTeamName = targetObjectiveTeamName;
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
        return "AlignmentView{" + "id=" + id + ", alignedObjectiveId=" + alignedObjectiveId
                + ", alignedObjectiveTitle='" + alignedObjectiveTitle + '\'' + ", alignedObjectiveTeamId="
                + alignedObjectiveTeamId + ", alignedObjectiveTeamName='" + alignedObjectiveTeamName + '\''
                + ", alignedObjectiveQuarterId=" + alignedObjectiveQuarterId + ", alignmentType='" + alignmentType
                + '\'' + ", targetKeyResultId=" + targetKeyResultId + ", targetKeyResultTitle='" + targetKeyResultTitle
                + '\'' + ", targetKeyResultTeamId=" + targetKeyResultTeamId + ", targetKeyResultTeamName='"
                + targetKeyResultTeamName + '\'' + ", targetObjectiveId=" + targetObjectiveId
                + ", targetObjectiveTitle='" + targetObjectiveTitle + '\'' + ", targetObjectiveTeamId="
                + targetObjectiveTeamId + ", targetObjectiveTeamName='" + targetObjectiveTeamName + '\''
                + ", writeable=" + writeable + '}';
    }

    public static final class Builder {
        private Long id;
        private Long alignedObjectiveId;
        private String alignedObjectiveTitle;
        private Long alignedObjectiveTeamId;
        private String alignedObjectiveTeamName;
        private Long alignedObjectiveQuarterId;
        private String alignmentType;
        private Long targetKeyResultId;
        private String targetKeyResultTitle;
        private Long targetKeyResultTeamId;
        private String targetKeyResultTeamName;
        private Long targetObjectiveId;
        private String targetObjectiveTitle;
        private Long targetObjectiveTeamId;
        private String targetObjectiveTeamName;
        private boolean writeable;

        public Builder() {
        }

        public Builder withId(Long val) {
            id = val;
            return this;
        }

        public Builder withAlignedObjectiveId(Long val) {
            alignedObjectiveId = val;
            return this;
        }

        public Builder withAlignedObjectiveTitle(String val) {
            alignedObjectiveTitle = val;
            return this;
        }

        public Builder withAlignedObjectiveTeamId(Long val) {
            alignedObjectiveTeamId = val;
            return this;
        }

        public Builder withAlignedObjectiveTeamName(String val) {
            alignedObjectiveTeamName = val;
            return this;
        }

        public Builder withAlignedObjectiveQuarterId(Long val) {
            alignedObjectiveQuarterId = val;
            return this;
        }

        public Builder withAlignmentType(String val) {
            alignmentType = val;
            return this;
        }

        public Builder withTargetKeyResultId(Long val) {
            targetKeyResultId = val;
            return this;
        }

        public Builder withTargetKeyResultTitle(String val) {
            targetKeyResultTitle = val;
            return this;
        }

        public Builder withTargetKeyResultTeamId(Long val) {
            targetKeyResultTeamId = val;
            return this;
        }

        public Builder withTargetKeyResultTeamName(String val) {
            targetKeyResultTeamName = val;
            return this;
        }

        public Builder withTargetObjectiveId(Long val) {
            targetObjectiveId = val;
            return this;
        }

        public Builder withTargetObjectiveTitle(String val) {
            targetObjectiveTitle = val;
            return this;
        }

        public Builder withTargetObjectiveTeamId(Long val) {
            targetObjectiveTeamId = val;
            return this;
        }

        public Builder withTargetObjectiveTeamName(String val) {
            targetObjectiveTeamName = val;
            return this;
        }

        public Builder withWriteable(boolean val) {
            writeable = val;
            return this;
        }

        public AlignmentView build() {
            return new AlignmentView(this);
        }
    }
}
