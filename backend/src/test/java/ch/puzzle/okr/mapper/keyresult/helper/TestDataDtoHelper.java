package ch.puzzle.okr.mapper.keyresult.helper;

import ch.puzzle.okr.dto.keyresult.*;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static ch.puzzle.okr.mapper.keyresult.helper.TestDataConstants.*;

public class TestDataDtoHelper {
    public static KeyResultMetricDto keyResultMetricDto() {
        return keyResultMetricDto;
    }

    public static KeyResultOrdinalDto keyResultOrdinalDto() {
        return keyResultOrdinalDto;
    }

    public static KeyResultLastCheckInMetricDto lastCheckInMetricDto() {
        return lastCheckInMetricDto;
    }

    public static KeyResultLastCheckInOrdinalDto lastCheckInOrdinalDto() {
        return lastCheckInOrdinalDto;
    }

    private static final KeyResultUserDto ownerDto = new KeyResultUserDto( //
            USER_ID, //
            USER_FIRST_NAME, //
            USER_LAST_NAME //
    );

    private static final KeyResultQuarterDto quarterDto = new KeyResultQuarterDto( //
            QUARTER_ID, //
            QUARTER_LABEL, //
            QUARTER_START_DATE, //
            QUARTER_END_DATE //
    );

    private static final KeyResultObjectiveDto objectiveDto = new KeyResultObjectiveDto( //
            OBJECTIVE_ID, //
            OBJECTIVE_STATE.name(), //
            quarterDto //
    );

    private static final KeyResultLastCheckInMetricDto lastCheckInMetricDto = new KeyResultLastCheckInMetricDto( //
            CHECK_IN_ID, //
            CHECK_IN_VERSION, //
            CHECK_IN_METRIC_VALUE, //
            CHECK_IN_CONFIDENCE, //
            CHECK_IN_CREATED_ON, //
            CHECK_IN_CHANGE_INFO, //
            CHECK_IN_INITIATIVES //
    );

    private static final KeyResultLastCheckInOrdinalDto lastCheckInOrdinalDto = new KeyResultLastCheckInOrdinalDto( //
            CHECK_IN_ID, //
            CHECK_IN_VERSION, //
            CHECK_IN_ORDINAL_ZONE, //
            CHECK_IN_CONFIDENCE, //
            CHECK_IN_CREATED_ON, //
            CHECK_IN_CHANGE_INFO, //
            CHECK_IN_INITIATIVES //
    );

    private static final KeyResultMetricDto keyResultMetricDto = new KeyResultMetricDto( //
            KEY_RESULT_ID, //
            KEY_RESULT_VERSION, //
            KEY_RESULT_TYPE_METRIC, //
            KEY_RESULT_TITLE, //
            KEY_RESULT_DESCRIPTION, //
            KEY_RESULT_BASELINE, //
            KEY_RESULT_STRETCH_GOAL, //
            KEY_RESULT_UNIT, //
            ownerDto, //
            objectiveDto, //
            lastCheckInMetricDto, //
            CHECK_IN_CREATED_ON, //
            CHECK_IN_MODIFIED_ON, //
            CHECK_IN_IS_WRITABLE, //
            null // actionList; ony used for keyResultMetric + action -> dto BUT NOT FOR dto -> keyResultMetric
    );

    private static final KeyResultOrdinalDto keyResultOrdinalDto = new KeyResultOrdinalDto( //
            KEY_RESULT_ID, //
            KEY_RESULT_VERSION, //
            KEY_RESULT_TYPE_ORDINAL, //
            KEY_RESULT_TITLE, //
            KEY_RESULT_DESCRIPTION, //
            KEY_RESULT_COMMIT_ZONE, //
            KEY_RESULT_TARGET_ZONE, //
            KEY_RESULT_STRETCH_ZONE, //
            ownerDto, //
            objectiveDto, //
            lastCheckInOrdinalDto, //
            CHECK_IN_CREATED_ON, //
            CHECK_IN_MODIFIED_ON, //
            CHECK_IN_IS_WRITABLE, //
            null // actionList; ony used for keyResultOrdinal + action -> dto BUT NOT FOR dto -> keyResultOrdinal
    );

}
