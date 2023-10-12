package ch.puzzle.okr.models.keyresult;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;

@Entity
@DiscriminatorValue(KEY_RESULT_TYPE_ORDINAL)
public class KeyResultOrdinal extends KeyResult {
    @NotNull(message = "CommitZone must not be null")
    @Size(max = 400, message = "Attribute commitZone has a max length of 400 characters")
    private String commitZone;

    @NotNull(message = "TargetZone must not be null")
    @Size(max = 400, message = "Attribute targetZone has a max length of 400 characters")
    private String targetZone;

    @NotNull(message = "StretchZone must not be null")
    @Size(max = 400, message = "Attribute stretchZone has a max length of 400 characters")
    private String stretchZone;

    public String getCommitZone() {
        return commitZone;
    }

    public void setCommitZone(String commitZone) {
        this.commitZone = commitZone;
    }

    public String getTargetZone() {
        return targetZone;
    }

    public void setTargetZone(String targetZone) {
        this.targetZone = targetZone;
    }

    public String getStretchZone() {
        return stretchZone;
    }

    public void setStretchZone(String stretchZone) {
        this.stretchZone = stretchZone;
    }

    public KeyResultOrdinal() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof KeyResultOrdinal) {
            return super.equals(o) && Objects.equals(commitZone, ((KeyResultOrdinal) o).commitZone)
                    && Objects.equals(targetZone, ((KeyResultOrdinal) o).targetZone)
                    && Objects.equals(stretchZone, ((KeyResultOrdinal) o).stretchZone);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commitZone, targetZone, stretchZone);
    }

    @Override
    public String toString() {
        return super.toString() + "KeyResultOrdinal{" + "commitZone='" + commitZone + '\'' + ", targetZone='"
                + targetZone + '\'' + ", stretchZone='" + stretchZone + '\'' + '}';
    }

    private KeyResultOrdinal(Builder builder) {
        super(builder);
        setCommitZone(builder.commitZone);
        setTargetZone(builder.targetZone);
        setStretchZone(builder.stretchZone);
    }

    public static class Builder extends KeyResult.Builder<Builder> {
        private String commitZone;
        private String targetZone;
        private String stretchZone;

        private Builder() {
            super(KEY_RESULT_TYPE_ORDINAL);
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withCommitZone(String commitZone) {
            this.commitZone = commitZone;
            return this;
        }

        public Builder withTargetZone(String targetZone) {
            this.targetZone = targetZone;
            return this;
        }

        public Builder withStretchZone(String stretchZone) {
            this.stretchZone = stretchZone;
            return this;
        }

        @Override
        public KeyResult build() {
            return new KeyResultOrdinal(this);
        }
    }
}
