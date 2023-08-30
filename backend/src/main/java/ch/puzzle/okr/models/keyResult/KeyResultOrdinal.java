package ch.puzzle.okr.models.keyResult;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("Ordinal")
public class KeyResultOrdinal extends KeyResult {
    @NotNull
    private String commitZone;

    @NotNull
    private String targetZone;

    @NotNull
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
    }

    public KeyResultOrdinal(Builder builder) {
        super(builder);
        setCommitZone(builder.commitZone);
        setTargetZone(builder.targetZone);
        setStretchZone(builder.stretchZone);
    }

    public static class Builder extends KeyResult.Builder {
        private @NotNull String commitZone;
        private @NotNull String targetZone;
        private @NotNull String stretchZone;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withCommitZone(@NotNull String commitZone) {
            this.commitZone = commitZone;
            return this;
        }

        public Builder withTargetZone(@NotNull String targetZone) {
            this.targetZone = targetZone;
            return this;
        }

        public Builder withStretchZone(@NotNull String stretchZone) {
            this.stretchZone = stretchZone;
            return this;
        }

        @Override
        public KeyResult build() {
            return new KeyResultOrdinal(this);
        }
    }
}
