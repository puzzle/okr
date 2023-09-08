package ch.puzzle.okr.models.checkin;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.User;

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

    @Size(max = 4096, message = "Attribute comment has a max length of 4096 characters")
    private String comment;

    @Max(10)
    @Min(0)
    private Integer confidence;

    @NotNull(message = "KeyResult must not be null")
    @ManyToOne
    private KeyResult keyResult;

    @NotNull(message = "Owner must not be null")
    @ManyToOne
    private User createdBy;

    @NotNull(message = "CreatedOn must not be null")
    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    @Column(name = "check_in_type", insertable = false, updatable = false)
    private String checkInType;

    /* Constructor */
    public CheckIn() {
    }

    /* Implement Getter and Setter */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        return "CheckIn{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", confidence=" + confidence +
                ", keyResult=" + keyResult +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", modifiedOn=" + modifiedOn +
                ", checkInType='" + checkInType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckIn checkIn = (CheckIn) o;
        return Objects.equals(id, checkIn.id) && Objects.equals(comment, checkIn.comment) && Objects.equals(confidence, checkIn.confidence) && Objects.equals(keyResult, checkIn.keyResult) && Objects.equals(createdBy, checkIn.createdBy) && Objects.equals(createdOn, checkIn.createdOn) && Objects.equals(modifiedOn, checkIn.modifiedOn) && Objects.equals(checkInType, checkIn.checkInType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, confidence, keyResult, createdBy, createdOn, modifiedOn, checkInType);
    }

    /* Builder */
    public abstract static class Builder {
        private Long id;
        private String comment;
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

        public Builder withComment(String comment) {
            this.comment = comment;
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
