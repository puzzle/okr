package ch.puzzle.okr.models.keyresult;

import java.util.List;

import ch.puzzle.okr.models.Action;

public record KeyResultWithActionList(KeyResult keyResult, List<Action> actionList) {
}
