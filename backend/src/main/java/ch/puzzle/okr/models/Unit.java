package ch.puzzle.okr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "unit")
@EntityListeners(AuditingEntityListener.class)
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_unit")
    @SequenceGenerator(name = "sequence_unit", allocationSize = 1)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Unit unit)) {
            return false;
        }
        return Objects.equals(getUnitName(), unit.getUnitName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUnitName());
    }

    @Version
    private int version;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(max = 4096, min = 3, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String unitName;

    @CreatedBy
    @ManyToOne(cascade = CascadeType.MERGE)
    private User createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Unit() {
    }

    private Unit(Builder builder) {
        setId(builder.id);
        setVersion(builder.version);
        setUnitName(builder.unitName);
        setCreatedBy(builder.createdBy);
    }

    public static final class Builder {
        private Long id;
        private int version;
        private @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
        @Size(max = 4096, min = 3, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
        String unitName;
        private @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
        User createdBy;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder version(int val) {
            version = val;
            return this;
        }

        public Builder unitName(@NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
        @Size(max = 4096, min = 3, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN) String val) {
            unitName = val;
            return this;
        }

        public Builder createdBy(@NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL) User val) {
            createdBy = val;
            return this;
        }

        public Unit build() {
            return new Unit(this);
        }
    }
}