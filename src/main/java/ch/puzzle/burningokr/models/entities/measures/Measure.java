package ch.puzzle.burningokr.models.entities.measures;

import ch.puzzle.burningokr.models.entities.KeyResult;
import ch.puzzle.burningokr.models.entities.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class Measure {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "key_result_id")
    private KeyResult keyResult;

    @NotNull
    @NotBlank
    @Column(name = "change_info")
    private String changeInfo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @NotNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    protected Measure(Long id, KeyResult keyResult, String changeInfo, User createdBy, LocalDateTime createdOn) {
        this.id = id;
        this.keyResult = keyResult;
        this.changeInfo = changeInfo;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }

    protected Measure() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measure measure = (Measure) o;
        return Objects.equals(id, measure.id) && Objects.equals(keyResult, measure.keyResult) && Objects.equals(changeInfo, measure.changeInfo) && Objects.equals(createdBy, measure.createdBy) && Objects.equals(createdOn, measure.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyResult, changeInfo, createdBy, createdOn);
    }

    @Override
    public String toString() {
        return "Measure{" +
                "id=" + id +
                ", keyResult=" + keyResult +
                ", changeInfo='" + changeInfo + '\'' +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                '}';
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(String changeInfo) {
        this.changeInfo = changeInfo;
    }

    public KeyResult getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(KeyResult keyResult) {
        this.keyResult = keyResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}