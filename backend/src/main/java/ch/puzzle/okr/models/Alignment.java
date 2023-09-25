package ch.puzzle.okr.models;

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
    @NotNull(message = "Aligned objective must not be null")
    @ManyToOne
    private Objective alignedObjective;

    protected Alignment() {
    }

    protected Alignment(Alignment.Builder<?> builder) {
        id = builder.id;
        setAlignedObjective(builder.alignedObjective);
    }

    public Long getId() {
        return id;
    }

    public Objective getAlignedObjective() {
        return alignedObjective;
    }

    public void setAlignedObjective(Objective alignedObjective) {
        this.alignedObjective = alignedObjective;
    }

    @Override
    public String toString() {
        return "Alignment{" + "id=" + id + ", alignedObjective=" + alignedObjective + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Alignment alignment = (Alignment) o;
        return Objects.equals(id, alignment.id) && Objects.equals(alignedObjective, alignment.alignedObjective);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alignedObjective);
    }

    public abstract static class Builder<T> {
        private Long id;
        private Objective alignedObjective;

        Builder() {
        }

        public T withId(Long id) {
            this.id = id;
            return (T) this;
        }

        public T withAlignedObjective(Objective alignedObjective) {
            this.alignedObjective = alignedObjective;
            return (T) this;
        }

        public abstract Alignment build();
    }
}
