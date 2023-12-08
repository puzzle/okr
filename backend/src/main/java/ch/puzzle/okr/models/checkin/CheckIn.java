package ch.puzzle.okr.models.checkin;

import ch.puzzle.okr.models.MessageKey;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.WriteableInterface;
import ch.puzzle.okr.models.keyresult.KeyResult;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "check_in_type")
public abstract class CheckIn implements WriteableInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_check_in")
    private Long id;

    @Version
    private int version;

    @Size(max = 4096, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String changeInfo;

    @Size(max = 4096, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String initiatives;

    @Max(value = 10, message = MessageKey.ATTRIBUTE_MAX_VALUE)
    @Min(value = 1, message = MessageKey.ATTRIBUTE_MIN_VALUE)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private Integer confidence;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private KeyResult keyResult;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private User createdBy;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    @Column(name = "check_in_type", insertable = false, updatable = false)
    private String checkInType;

    private transient boolean writeable;

    /* Getter and Setter */
    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
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
        return "CheckIn{" + "id=" + id + ", version=" + version + ", changeInfo='" + changeInfo + '\''
                + ", initiatives='" + initiatives + '\'' + ", confidence=" + confidence + ", keyResult=" + keyResult
                + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn
                + ", checkInType='" + checkInType + '\'' + ", writeable=" + writeable + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CheckIn checkIn = (CheckIn) o;
        return Objects.equals(id, checkIn.id) && version == checkIn.version
                && Objects.equals(changeInfo, checkIn.changeInfo) && Objects.equals(initiatives, checkIn.initiatives)
                && Objects.equals(confidence, checkIn.confidence) && Objects.equals(keyResult, checkIn.keyResult)
                && Objects.equals(createdBy, checkIn.createdBy) && Objects.equals(createdOn, checkIn.createdOn)
                && Objects.equals(modifiedOn, checkIn.modifiedOn) && Objects.equals(checkInType, checkIn.checkInType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, changeInfo, initiatives, confidence, keyResult, createdBy, createdOn,
                modifiedOn, checkInType);
    }

    /* Constructor */
    protected CheckIn() {
    }

    protected CheckIn(Builder<?> builder) {
        id = builder.id;
        version = builder.version;
        setChangeInfo(builder.changeInfo);
        setInitiatives(builder.initiatives);
        setConfidence(builder.confidence);
        setKeyResult(builder.keyResult);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
        setModifiedOn(builder.modifiedOn);
    }

    @SuppressWarnings(value = "unchecked")
    public abstract static class Builder<T> {
        private Long id;
        private int version;
        private String changeInfo;
        private String initiatives;
        private Integer confidence;
        private KeyResult keyResult;
        private User createdBy;
        private LocalDateTime createdOn;
        private LocalDateTime modifiedOn;

        public T withId(Long id) {
            this.id = id;
            return (T) this;
        }

        public T withVersion(int version) {
            this.version = version;
            return (T) this;
        }

        public T withChangeInfo(String changeInfo) {
            this.changeInfo = changeInfo;
            return (T) this;
        }

        public T withInitiatives(String initiatives) {
            this.initiatives = initiatives;
            return (T) this;
        }

        public T withConfidence(Integer confidence) {
            this.confidence = confidence;
            return (T) this;
        }

        public T withKeyResult(KeyResult keyResult) {
            this.keyResult = keyResult;
            return (T) this;
        }

        public T withCreatedBy(User createdBy) {
            this.createdBy = createdBy;
            return (T) this;
        }

        public T withCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return (T) this;
        }

        public T withModifiedOn(LocalDateTime modifiedOn) {
            this.modifiedOn = modifiedOn;
            return (T) this;
        }

        public abstract CheckIn build();
    }
}
