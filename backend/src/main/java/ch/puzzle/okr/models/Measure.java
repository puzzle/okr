package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "measure")
public class Measure {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_measure")
    @NotNull
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "key_result_id")
    private KeyResult keyResult;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    private Double value;

    @NotNull
    @NotBlank
    private String changeInfo;

    private String initiatives;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @NotNull
    private LocalDateTime createdOn;

    public Measure() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measure measure = (Measure) o;
        return Objects.equals(id, measure.id) && Objects.equals(keyResult, measure.keyResult) && Objects.equals(value, measure.value) && Objects.equals(changeInfo, measure.changeInfo) && Objects.equals(initiatives, measure.initiatives) && Objects.equals(createdBy, measure.createdBy) && Objects.equals(createdOn, measure.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyResult, value, changeInfo, initiatives, createdBy, createdOn);
    }

    @Override
    public String toString() {
        return "Measure{" +
                "id=" + id +
                ", keyResult=" + keyResult +
                ", value=" + value +
                ", changeInfo='" + changeInfo + '\'' +
                ", initiatives='" + initiatives + '\'' +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                '}';
    }

    private Measure(Builder builder) {
        id = builder.id;
        keyResult = builder.keyResult;
        value = builder.value;
        changeInfo = builder.changeInfo;
        initiatives = builder.initiatives;
        createdBy = builder.createdBy;
        createdOn = builder.createdOn;
    }

    public static final class Builder {
        private @NotNull Long id;
        private KeyResult keyResult;
        private @NotNull @NotBlank @Size(min = 2, max = 50) Double value;
        private @NotNull @NotBlank String changeInfo;
        private String initiatives;
        private @NotNull User createdBy;
        private @NotNull LocalDateTime createdOn;

        public Builder() {
        }

        public Builder id(@NotNull Long val) {
            id = val;
            return this;
        }

        public Builder keyResult(KeyResult val) {
            keyResult = val;
            return this;
        }

        public Builder value(@NotNull @NotBlank @Size(min = 2, max = 50) Double val) {
            value = val;
            return this;
        }

        public Builder changeInfo(@NotNull @NotBlank String val) {
            changeInfo = val;
            return this;
        }

        public Builder initiatives(String val) {
            initiatives = val;
            return this;
        }

        public Builder createdBy(@NotNull User val) {
            createdBy = val;
            return this;
        }

        public Builder createdOn(@NotNull LocalDateTime val) {
            createdOn = val;
            return this;
        }

        public Measure build() {
            return new Measure(this);
        }
    }
}
