package ch.puzzle.okr.mapper.checkin.helper;

import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;

import static ch.puzzle.okr.mapper.checkin.helper.TestDataConstants.*;

public class TestDataHelper {

    public static KeyResult keyResult() {
        return keyResult;
    }

    public static CheckInMetric checkInMetric() {
        checkInMetric.setWriteable(CHECK_IN_IS_WRITEABLE);
        return (CheckInMetric) checkInMetric;
    }

    public static CheckInOrdinal checkInOrdinal() {
        checkInOrdinal.setWriteable(CHECK_IN_IS_WRITEABLE);
        return (CheckInOrdinal) TestDataHelper.checkInOrdinal;
    }

    private static final KeyResult keyResult = KeyResultMetric.Builder.builder() //
                                                                      .withId(KEY_RESULT_ID) //
                                                                      .build();

    private static final CheckIn checkInMetric = CheckInMetric.Builder.builder() //
                                                                      .withId(CHECK_IN_ID) //
                                                                      .withVersion(CHECK_IN_VERSION) //
                                                                      .withChangeInfo(CHECK_IN_CHANGE_INFO) //
                                                                      .withInitiatives(CHECK_IN_INITIATIVES) //
                                                                      .withConfidence(CHECK_IN_CONFIDENCE) //
                                                                      .withKeyResult(keyResult) //
                                                                      .withCreatedOn(CHECK_IN_CREATE_DATE_TIME) //
                                                                      .withModifiedOn(CHECK_IN_MODIFIED_DATE_TIME) //
                                                                      .withValue(CHECK_IN_METRIC_VALUE) //
                                                                      .build();

    private static final CheckIn checkInOrdinal = CheckInOrdinal.Builder.builder() //
                                                                        .withId(CHECK_IN_ID) //
                                                                        .withVersion(CHECK_IN_VERSION) //
                                                                        .withChangeInfo(CHECK_IN_CHANGE_INFO) //
                                                                        .withInitiatives(CHECK_IN_INITIATIVES) //
                                                                        .withConfidence(CHECK_IN_CONFIDENCE) //
                                                                        .withKeyResult(keyResult) //
                                                                        .withCreatedOn(CHECK_IN_CREATE_DATE_TIME) //
                                                                        .withModifiedOn(CHECK_IN_MODIFIED_DATE_TIME) //
                                                                        .withZone(CHECK_IN_ORDINAL_ZONE)
                                                                        .build();
}
