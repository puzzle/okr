package ch.puzzle.okr.models.checkIn;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.keyresult.KeyResult;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "check_in_type")
public abstract class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_check_in")
    private Long id;

    @Size(max = 4096, message = "Attribute changeInfo has a max length of 4096 characters")
    private String changeInfo;

    @Size(max = 4096, message = "Attribute initiatives has a max length of 4096 characters")
    private String initiatives;

    @Max(value = 10, message = "Attribute confidence has a max value of 10")
    @Min(value = 1, message = "Attribute confidence has a min value of 1")
    @NotNull(message = "Confidence must not be null")
    private Integer confidence;

    @NotNull(message = "KeyResult must not be null")
    @ManyToOne
    private KeyResult keyResult;

    @NotNull(message = "CreatedBy must not be null")
    @ManyToOne
    private User createdBy;

    @NotNull(message = "CreatedOn must not be null")
    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    @Column(name = "check_in_type", insertable = false, updatable = false)
    private String checkInType;

    /* Getter and Setter */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(String changeInfo) {
        this.changeInfo = changeInfo;
    }

    public String getInitiatives() {
        return initiatives;
    }

    public void setInitiatives(String initiatives) {
        this.initiatives = initiatives;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public KeyResult getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(KeyResult keyResult) {
        this.keyResult = keyResult;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCheckInType() {
        return checkInType;
    }

    public void setCheckInType(String checkInType) {
        this.checkInType = checkInType;
    }

    /* toString(), equals() and hashCode() methods */

    @Override
    public String toString() {
        return "CheckIn{" + "id=" + id + ", changeInfo='" + changeInfo + '\'' + ", initiatives='" + initiatives + '\''
                + ", confidence=" + confidence + ", keyResult=" + keyResult + ", createdBy=" + createdBy
                + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", checkInType='" + checkInType + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CheckIn checkIn = (CheckIn) o;
        return Objects.equals(id, checkIn.id) && Objects.equals(changeInfo, checkIn.changeInfo)
                && Objects.equals(initiatives, checkIn.initiatives) && Objects.equals(confidence, checkIn.confidence)
                && Objects.equals(keyResult, checkIn.keyResult) && Objects.equals(createdBy, checkIn.createdBy)
                && Objects.equals(createdOn, checkIn.createdOn) && Objects.equals(modifiedOn, checkIn.modifiedOn)
                && Objects.equals(checkInType, checkIn.checkInType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, changeInfo, initiatives, confidence, keyResult, createdBy, createdOn, modifiedOn,
                checkInType);
    }

    /* Constructor */
    public CheckIn() {
    }

    public CheckIn(Builder builder) {
        id = builder.id;
        setChangeInfo(builder.changeInfo);
        setInitiatives(builder.initiatives);
        setConfidence(builder.confidence);
        setKeyResult(builder.keyResult);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
        setModifiedOn(builder.modifiedOn);
        setCheckInType(builder.checkInType);
    }

    /* Builder */
    public abstract static class Builder {
        private Long id;
        private String changeInfo;
        private String initiatives;
        private Integer confidence;
        private KeyResult keyResult;
        private User createdBy;
        private LocalDateTime createdOn;
        private LocalDateTime modifiedOn;
        private String checkInType;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withChangeInfo(String changeInfo) {
            this.changeInfo = changeInfo;
            return this;
        }

        public Builder withInitiatives(String initiatives) {
            this.initiatives = initiatives;
            return this;
        }

        public Builder withConfidence(Integer confidence) {
            this.confidence = confidence;
            return this;
        }

        public Builder withKeyResult(KeyResult keyResult) {
            this.keyResult = keyResult;
            return this;
        }

        public Builder withCreatedBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder withModifiedOn(LocalDateTime modifiedOn) {
            this.modifiedOn = modifiedOn;
            return this;
        }

        public Builder withCheckInType(String checkInType) {
            this.checkInType = checkInType;
            return this;
        }

        public abstract CheckIn build();
    }
}
