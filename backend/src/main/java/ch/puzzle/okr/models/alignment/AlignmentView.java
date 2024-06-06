package ch.puzzle.okr.models.alignment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

import java.util.Objects;

@Entity
@Immutable
public class AlignmentView {

    @Id
    private String uniqueId;
    private Long id;
    private String title;
    private Long teamId;
    private String teamName;
    private Long quarterId;
    private String state;
    private String objectType;
    private String connectionRole;
    private Long counterpartId;
    private String counterpartType;

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
        setConnectionRole(builder.connectionRole);
        setCounterpartId(builder.counterpartId);
        setCounterpartType(builder.counterpartType);
    }

    public void setUniqueId(String uniqueId) {
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

    public String getConnectionRole() {
        return connectionRole;
    }

    public void setConnectionRole(String connectionItem) {
        this.connectionRole = connectionItem;
    }

    public Long getCounterpartId() {
        return counterpartId;
    }

    public void setCounterpartId(Long refId) {
        this.counterpartId = refId;
    }

    public String getCounterpartType() {
        return counterpartType;
    }

    public void setCounterpartType(String refType) {
        this.counterpartType = refType;
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
                && Objects.equals(connectionRole, that.connectionRole)
                && Objects.equals(counterpartId, that.counterpartId)
                && Objects.equals(counterpartType, that.counterpartType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId, id, title, teamId, teamName, quarterId, state, objectType, connectionRole,
                counterpartId, counterpartType);
    }

    @Override
    public String toString() {
        return "AlignmentView{" + "uniqueId='" + uniqueId + '\'' + ", id=" + id + ", title='" + title + '\''
                + ", teamId=" + teamId + ", teamName='" + teamName + '\'' + ", quarterId=" + quarterId + ", state='"
                + state + '\'' + ", objectType='" + objectType + '\'' + ", connectionItem='" + connectionRole + '\''
                + ", refId=" + counterpartId + ", refType='" + counterpartType + '\'' + '}';
    }

    public static final class Builder {
        private String uniqueId;
        private Long id;
        private String title;
        private Long teamId;
        private String teamName;
        private Long quarterId;
        private String state;
        private String objectType;
        private String connectionRole;
        private Long counterpartId;
        private String counterpartType;

        private Builder() {
        }

        public static AlignmentView.Builder builder() {
            return new AlignmentView.Builder();
        }

        public Builder withUniqueId(String val) {
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

        public Builder withConnectionRole(String val) {
            connectionRole = val;
            return this;
        }

        public Builder withCounterpartId(Long val) {
            counterpartId = val;
            return this;
        }

        public Builder withCounterpartType(String val) {
            counterpartType = val;
            return this;
        }

        public AlignmentView build() {
            return new AlignmentView(this);
        }
    }
}
