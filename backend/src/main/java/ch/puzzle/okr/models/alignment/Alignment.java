package ch.puzzle.okr.models.alignment;

import ch.puzzle.okr.models.Objective;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "alignment_type")
public abstract class Alignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_alignment")
    private Long id;
    @Version
    private int version;
    @NotNull(message = "Aligned objective must not be null")
    @ManyToOne
    private Objective alignedObjective;

    protected Alignment() {
    }

    protected Alignment(Alignment.Builder<?> builder) {
        id = builder.id;
        version = builder.version;
        setAlignedObjective(builder.alignedObjective);
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public Objective getAlignedObjective() {
        return alignedObjective;
    }

    public void setAlignedObjective(Objective alignedObjective) {
        this.alignedObjective = alignedObjective;
    }

    @Override
    public String toString() {
        return "Alignment{" + "id=" + id + ", version=" + version + ", alignedObjective=" + alignedObjective + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Alignment alignment = (Alignment) o;
        return Objects.equals(id, alignment.id) && version == alignment.version
                && Objects.equals(alignedObjective, alignment.alignedObjective);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, alignedObjective);
    }

    public abstract static class Builder<T> {
        private Long id;
        private int version;
        private Objective alignedObjective;

        Builder() {
        }

        public T withId(Long id) {
            this.id = id;
            return (T) this;
        }

        public T withVersion(int version) {
            this.version = version;
            return (T) this;
        }

        public T withAlignedObjective(Objective alignedObjective) {
            this.alignedObjective = alignedObjective;
            return (T) this;
        }

        public abstract Alignment build();
    }
}
