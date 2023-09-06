package ch.puzzle.okr.models;

public interface AlignmentInterface<TargetType> {

    TargetType getAlignmentTarget();

    void setAlignmentTarget(TargetType alignmentTarget);

    Objective getAlignedObjective();

    void setAlignedObjective(Objective alignedObjective);
}
