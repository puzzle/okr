package ch.puzzle.okr.models;

public interface AlignmentInterface<T> {

    Long getId();

    T getAlignmentTarget();

    void setAlignmentTarget(T alignmentTarget);

    Objective getAlignedObjective();

    void setAlignedObjective(Objective alignedObjective);
}
