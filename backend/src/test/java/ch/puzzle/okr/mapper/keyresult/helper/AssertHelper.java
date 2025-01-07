package ch.puzzle.okr.mapper.keyresult.helper;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataDtoHelper.lastCheckInMetricDto;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.okr.dto.ActionDto;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import java.util.List;

public class AssertHelper {

    public static void assertKeyResultMetricDtoWithCheckIn(KeyResultMetric expected, KeyResultMetricDto actual,
                                                           List<Action> actions) {
        assertKeyResultMetricDto(expected, actual, actions);
        // checkIn is only available on KeyResultMetricDto (and not on KeyResultMetric).
        // So check actual against the checkInMetricDto from TestDataDtoHelper
        assertNotNull(actual.lastCheckIn());
        assertCheckInMetricDto(lastCheckInMetricDto(), actual.lastCheckIn());
    }

    public static void assertKeyResultMetricDtoWithoutCheckIn(KeyResultMetric expected, KeyResultMetricDto actual,
                                                              List<Action> actions) {
        assertKeyResultMetricDto(expected, actual, actions);
        assertNull(actual.lastCheckIn());
    }

    private static void assertKeyResultMetricDto(KeyResultMetric expected, KeyResultMetricDto actual,
                                                 List<Action> actions) {
        assertEquals(KEY_RESULT_TYPE_METRIC, actual.keyResultType());
        assertEquals(expected.getKeyResultType(), actual.keyResultType());

        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getTitle(), actual.title());
        assertEquals(expected.getDescription(), actual.description());
        assertEquals(expected.getBaseline(), actual.baseline());
        assertEquals(expected.getStretchGoal(), actual.stretchGoal());
        assertEquals(expected.getUnit(), actual.unit());
        assertOwnerDto(expected.getOwner(), actual.owner());
        assertObjectiveDto(expected.getObjective(), actual.objective());
        assertEquals(expected.getCreatedOn(), actual.createdOn());
        assertEquals(expected.getModifiedOn(), actual.modifiedOn());

        assertEquals(1, actual.getActionList().size());
        assertActionDto(actions.get(0), actual.getActionList().get(0));
    }

    public static void assertKeyResultMetric(KeyResultMetricDto expected, KeyResultMetric actual) {
        assertEquals(KEY_RESULT_TYPE_METRIC, actual.getKeyResultType());
        assertEquals(expected.keyResultType(), actual.getKeyResultType());
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.version(), actual.getVersion());
        assertEquals(expected.title(), actual.getTitle());
        assertEquals(expected.description(), actual.getDescription());
        assertEquals(expected.baseline(), actual.getBaseline());
        assertEquals(expected.stretchGoal(), actual.getStretchGoal());
        assertEquals(expected.unit(), actual.getUnit());
        assertOwner(expected.owner(), actual.getOwner());
        assertObjective(expected.objective(), actual.getObjective());
        assertEquals(expected.createdOn(), actual.getCreatedOn());
        assertEquals(expected.modifiedOn(), actual.getModifiedOn());
    }

    public static void assertKeyResultOrdinalDtoWithCheckIn(KeyResultOrdinal expected, KeyResultOrdinalDto actual,
                                                            List<Action> actions) {
        assertKeyResultOrdinalDto(expected, actual, actions);
        assertNotNull(actual.lastCheckIn());
        assertCheckInOrdinalDto(TestDataDtoHelper.lastCheckInOrdinalDto(), actual.lastCheckIn());
    }

    public static void assertKeyResultOrdinalDtoWithoutCheckIn(KeyResultOrdinal expected, KeyResultOrdinalDto actual,
                                                               List<Action> actions) {
        assertKeyResultOrdinalDto(expected, actual, actions);
        // checkIn is only available on KeyResultOrdinalDto (and not on
        // KeyResultOrdinal).
        // So check actual against the KeyResultOrdinalDto from TestDataDtoHelper
        assertNull(actual.lastCheckIn());
    }

    private static void assertKeyResultOrdinalDto(KeyResultOrdinal expected, KeyResultOrdinalDto actual,
                                                  List<Action> actions) {
        assertEquals(KEY_RESULT_TYPE_ORDINAL, actual.keyResultType());
        assertEquals(expected.getKeyResultType(), actual.keyResultType());

        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getTitle(), actual.title());
        assertEquals(expected.getDescription(), actual.description());
        assertEquals(expected.getCommitZone(), actual.commitZone());
        assertEquals(expected.getTargetZone(), actual.targetZone());
        assertEquals(expected.getStretchZone(), actual.stretchZone());
        assertOwnerDto(expected.getOwner(), actual.owner());
        assertObjectiveDto(expected.getObjective(), actual.objective());
        assertEquals(expected.getCreatedOn(), actual.createdOn());
        assertEquals(expected.getModifiedOn(), actual.modifiedOn());

        assertEquals(1, actual.getActionList().size());
        assertActionDto(actions.get(0), actual.getActionList().get(0));
    }

    public static void assertKeyResultOrdinal(KeyResultOrdinalDto expected, KeyResultOrdinal actual) {
        assertEquals(KEY_RESULT_TYPE_ORDINAL, actual.getKeyResultType());
        assertEquals(expected.keyResultType(), actual.getKeyResultType());
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.version(), actual.getVersion());
        assertEquals(expected.title(), actual.getTitle());
        assertEquals(expected.description(), actual.getDescription());
        assertEquals(expected.commitZone(), actual.getCommitZone());
        assertEquals(expected.targetZone(), actual.getTargetZone());
        assertEquals(expected.stretchZone(), actual.getStretchZone());
        assertOwner(expected.owner(), actual.getOwner());
        assertObjective(expected.objective(), actual.getObjective());
        assertEquals(expected.createdOn(), actual.getCreatedOn());
        assertEquals(expected.modifiedOn(), actual.getModifiedOn());
    }

    private static void assertActionDto(Action expected, ActionDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getActionPoint(), actual.action());
        assertEquals(expected.getPriority(), actual.priority());
        assertEquals(expected.isChecked(), actual.isChecked());
        assertEquals(expected.getKeyResult().getId(), actual.keyResultId());
    }

    private static void assertObjectiveDto(Objective expected, KeyResultObjectiveDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getState().name(), actual.state());
        assertQuarterDto(expected.getQuarter(), actual.keyResultQuarterDto());
    }

    private static void assertQuarterDto(Quarter expected, KeyResultQuarterDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getLabel(), actual.label());
        assertEquals(expected.getStartDate(), actual.startDate());
        assertEquals(expected.getEndDate(), actual.endDate());
    }

    private static void assertObjective(KeyResultObjectiveDto expected, Objective actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.state(), actual.getState().name());
        assertQuarter(expected.keyResultQuarterDto(), actual.getQuarter());
    }

    private static void assertQuarter(KeyResultQuarterDto expected, Quarter actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.label(), actual.getLabel());
        assertEquals(expected.startDate(), actual.getStartDate());
        assertEquals(expected.endDate(), actual.getEndDate());
    }

    private static void assertOwnerDto(User expected, KeyResultUserDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getFirstName(), actual.firstName());
        assertEquals(expected.getLastName(), actual.lastName());
    }

    private static void assertOwner(KeyResultUserDto expected, User actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.firstName(), actual.getFirstName());
        assertEquals(expected.lastName(), actual.getLastName());
    }

    private static void assertCheckInMetricDto(KeyResultLastCheckInMetricDto expected,
                                               KeyResultLastCheckInMetricDto actual) {
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.version(), actual.version());
        assertEquals(expected.value(), actual.value());
        assertEquals(expected.confidence(), actual.confidence());
        assertEquals(expected.createdOn(), actual.createdOn());
        assertEquals(expected.changeInfo(), actual.changeInfo());
        assertEquals(expected.initiatives(), actual.initiatives());
    }

    private static void assertCheckInOrdinalDto(KeyResultLastCheckInOrdinalDto expected,
                                                KeyResultLastCheckInOrdinalDto actual) {
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.version(), actual.version());
        assertEquals(expected.zone(), actual.zone());
        assertEquals(expected.confidence(), actual.confidence());
        assertEquals(expected.createdOn(), actual.createdOn());
        assertEquals(expected.changeInfo(), actual.changeInfo());
        assertEquals(expected.initiatives(), actual.initiatives());
    }

}
