package ch.puzzle.okr.models;

public interface AlignmentInterface<T> {

    T getAlignmentTarget();

    void setAlignmentTarget(T alignmentTarget);

    Objective getAlignedObjective();

    void setAlignedObjective(Objective alignedObjective);
}
