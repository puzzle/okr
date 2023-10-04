package ch.puzzle.okr.models.alignment;

import ch.puzzle.okr.models.Objective;

public interface AlignmentInterface<T> {

    Long getId();

    T getAlignmentTarget();

    void setAlignmentTarget(T alignmentTarget);

    Objective getAlignedObjective();

    void setAlignedObjective(Objective alignedObjective);
}
