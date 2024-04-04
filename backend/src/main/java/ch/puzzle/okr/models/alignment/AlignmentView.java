package ch.puzzle.okr.models.alignment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

import java.util.Objects;

@Entity
@Immutable
public class AlignmentView {

    @Id
    private Long uniqueId;
    private Long id;
    private String title;
    private Long teamId;
    private String teamName;
    private Long quarterId;
    private String state;
    private String objectType;
    private String connectionItem;
    private Long refId;
    private String refType;

    public AlignmentView() {
    }

    private AlignmentView(Builder builder) {
        setUniqueId(builder.uniqueId);
        setId(builder.id);
        setTitle(builder.title);
        setTeamId(builder.teamId);
        setTeamName(builder.teamName);
        setQuarterId(builder.quarterId);
        setState(builder.state);
        setObjectType(builder.objectType);
        setConnectionItem(builder.connectionItem);
        setRefId(builder.refId);
        setRefType(builder.refType);
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public void setQuarterId(Long quarterId) {
        this.quarterId = quarterId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getConnectionItem() {
        return connectionItem;
    }

    public void setConnectionItem(String connectionItem) {
        this.connectionItem = connectionItem;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AlignmentView that = (AlignmentView) o;
        return Objects.equals(uniqueId, that.uniqueId) && Objects.equals(id, that.id)
                && Objects.equals(title, that.title) && Objects.equals(teamId, that.teamId)
                && Objects.equals(teamName, that.teamName) && Objects.equals(quarterId, that.quarterId)
                && Objects.equals(state, that.state) && Objects.equals(objectType, that.objectType)
                && Objects.equals(connectionItem, that.connectionItem) && Objects.equals(refId, that.refId)
                && Objects.equals(refType, that.refType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId, id, title, teamId, teamName, quarterId, state, objectType, connectionItem, refId,
                refType);
    }

    @Override
    public String toString() {
        return "AlignmentView{" + "uniqueId=" + uniqueId + ", id=" + id + ", title='" + title + '\'' + ", teamId="
                + teamId + ", teamName='" + teamName + '\'' + ", quarterId=" + quarterId + ", state='" + state + '\''
                + ", objectType='" + objectType + '\'' + ", connectionItem='" + connectionItem + '\'' + ", refId="
                + refId + ", refType='" + refType + '\'' + '}';
    }

    public static final class Builder {
        private Long uniqueId;
        private Long id;
        private String title;
        private Long teamId;
        private String teamName;
        private Long quarterId;
        private String state;
        private String objectType;
        private String connectionItem;
        private Long refId;
        private String refType;

        public Builder() {
        }

        public Builder withUniqueId(Long val) {
            uniqueId = val;
            return this;
        }

        public Builder withId(Long val) {
            id = val;
            return this;
        }

        public Builder withTitle(String val) {
            title = val;
            return this;
        }

        public Builder withTeamId(Long val) {
            teamId = val;
            return this;
        }

        public Builder withTeamName(String val) {
            teamName = val;
            return this;
        }

        public Builder withQuarterId(Long val) {
            quarterId = val;
            return this;
        }

        public Builder withState(String val) {
            state = val;
            return this;
        }

        public Builder withObjectType(String val) {
            objectType = val;
            return this;
        }

        public Builder withConnectionItem(String val) {
            connectionItem = val;
            return this;
        }

        public Builder withRefId(Long val) {
            refId = val;
            return this;
        }

        public Builder withRefType(String val) {
            refType = val;
            return this;
        }

        public AlignmentView build() {
            return new AlignmentView(this);
        }
    }
}
