package ch.puzzle.okr.models.keyresult;

import ch.puzzle.okr.models.Action;

import java.util.List;

public record KeyResultWithActionList(KeyResult keyResult, List<Action> actionList) {
}
