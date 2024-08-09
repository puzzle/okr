package ch.puzzle.okr.mapper.keyresult.helper;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;

import static ch.puzzle.okr.mapper.keyresult.helper.TestDataConstants.*;

public class TestDataHelper {

    public static KeyResultMetric keyResultMetric() {
        KeyResult keyResult = TestDataHelper.keyResultMetric;
        keyResult.setWriteable(KEY_RESULT_IS_WRITABLE);
        return (KeyResultMetric) keyResult;
    }

    public static KeyResultOrdinal keyResultOrdinal() {
        KeyResult keyResult = TestDataHelper.keyResultOrdinal;
        keyResult.setWriteable(KEY_RESULT_IS_WRITABLE);
        return (KeyResultOrdinal) keyResult;
    }

    public static CheckInMetric checkInMetric() {
        return (CheckInMetric) checkInMetric;
    }

    public static CheckInOrdinal checkInOrdinal() {
        return (CheckInOrdinal) checkInOrdinal;
    }

    public static Action action(KeyResult keyResult) {
        if (keyResult instanceof KeyResultMetric)
            return actionForKeyResultMetric;
        if (keyResult instanceof KeyResultOrdinal)
            return actionForKeyResultOrdinal;
        return null;
    }

    public static User owner() {
        return owner;
    }

    public static Objective objective() {
        return objective;
    }

    private static final CheckIn checkInMetric = CheckInMetric.Builder.builder() //
            .withId(CHECK_IN_ID) //
            .withVersion(CHECK_IN_VERSION) //
            .withValue(CHECK_IN_METRIC_VALUE) //
            .withConfidence(CHECK_IN_CONFIDENCE) //
            .withCreatedOn(CHECK_IN_CREATED_ON) //
            .withChangeInfo(CHECK_IN_CHANGE_INFO) //
            .withInitiatives(CHECK_IN_INITIATIVES) //
            .build();

    private static final CheckIn checkInOrdinal = CheckInOrdinal.Builder.builder() //
            .withId(CHECK_IN_ID) //
            .withVersion(CHECK_IN_VERSION) //
            .withZone(CHECK_IN_ORDINAL_ZONE) //
            .withConfidence(CHECK_IN_CONFIDENCE) //
            .withCreatedOn(CHECK_IN_CREATED_ON) //
            .withChangeInfo(CHECK_IN_CHANGE_INFO) //
            .withInitiatives(CHECK_IN_INITIATIVES) //
            .build();

    private static final User owner = User.Builder.builder() //
            .withId(USER_ID) //
            .withFirstname(USER_FIRSTNAME) //
            .withLastname(USER_LASTNAME) //
            .build();

    private static final Quarter quarter = Quarter.Builder.builder() //
            .withId(QUARTER_ID) //
            .withLabel(QUARTER_LABEL) //
            .withStartDate(QUARTER_START_DATE) //
            .withEndDate(QUARTER_END_DATE) //
            .build();

    private static final Objective objective = Objective.Builder.builder() //
            .withId(OBJECTIVE_ID) //
            .withQuarter(quarter) //
            .withState(OBJECTIVE_STATE) //
            .build();

    private static final KeyResult keyResultMetric = KeyResultMetric.Builder.builder() //
            .withId(KEY_RESULT_ID) //
            .withVersion(KEY_RESULT_VERSION) //
            .withTitle(KEY_RESULT_TITLE) //
            .withDescription(KEY_RESULT_DESCRIPTION) //
            .withBaseline(KEY_RESULT_BASELINE) //
            .withStretchGoal(KEY_RESULT_STRETCH_GOAL) //
            .withUnit(KEY_RESULT_UNIT) //
            .withOwner(owner) //
            .withObjective(objective) //
            .withCreatedOn(CHECK_IN_CREATED_ON) //
            .withModifiedOn(CHECK_IN_MODIFIED_ON) //
            .build();

    private static final KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder() //
            .withId(KEY_RESULT_ID) //
            .withVersion(KEY_RESULT_VERSION) //
            .withTitle(KEY_RESULT_TITLE) //
            .withDescription(KEY_RESULT_DESCRIPTION) //
            .withCommitZone(KEY_RESULT_COMMIT_ZONE).withTargetZone(KEY_RESULT_TARGET_ZONE)
            .withStretchZone(KEY_RESULT_STRETCH_ZONE).withOwner(owner) //
            .withObjective(objective) //
            .withCreatedOn(CHECK_IN_CREATED_ON) //
            .withModifiedOn(CHECK_IN_MODIFIED_ON) //
            .build();

    private static final Action actionForKeyResultMetric = Action.Builder.builder() //
            .withId(ACTION_ID) //
            .withVersion(ACTION_VERSION) //
            .withAction(ACTION_ACTION) //
            .withPriority(ACTION_PRIORITY) //
            .withIsChecked(ACTION_IS_CHECKED) //
            .withKeyResult(keyResultMetric) //
            .build();

    private static final Action actionForKeyResultOrdinal = Action.Builder.builder() //
            .withId(ACTION_ID) //
            .withVersion(ACTION_VERSION) //
            .withAction(ACTION_ACTION) //
            .withPriority(ACTION_PRIORITY) //
            .withIsChecked(ACTION_IS_CHECKED) //
            .withKeyResult(keyResultOrdinal) //
            .build();

}
