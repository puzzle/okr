package ch.puzzle.okr.mapper.checkin.helper;

import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;

import static ch.puzzle.okr.mapper.checkin.helper.TestDataConstants.*;

public class TestDataDtoHelper {

    public static CheckInMetricDto checkInMetricDto() {
        return checkInMetricDto;
    }

    public static CheckInOrdinalDto checkInOrdinalDto() {
        return checkInOrdinalDto;
    }

    private static final CheckInMetricDto checkInMetricDto = new CheckInMetricDto(CHECK_IN_ID, //
                                                                                  CHECK_IN_VERSION, //
                                                                                  CHECK_IN_CHANGE_INFO, //
                                                                                  CHECK_IN_INITIATIVES, //
                                                                                  CHECK_IN_CONFIDENCE, //
                                                                                  KEY_RESULT_ID, //
                                                                                  CHECK_IN_CREATE_DATE_TIME, //
                                                                                  CHECK_IN_MODIFIED_DATE_TIME, //
                                                                                  CHECK_IN_METRIC_VALUE, //
                                                                                  CHECK_IN_IS_WRITEABLE //
    );

    private static final CheckInOrdinalDto checkInOrdinalDto = new CheckInOrdinalDto(CHECK_IN_ID, //
                                                                                     CHECK_IN_VERSION, //
                                                                                     CHECK_IN_CHANGE_INFO, //
                                                                                     CHECK_IN_INITIATIVES, //
                                                                                     CHECK_IN_CONFIDENCE, //
                                                                                     KEY_RESULT_ID, //
                                                                                     CHECK_IN_CREATE_DATE_TIME, //
                                                                                     CHECK_IN_MODIFIED_DATE_TIME, //
                                                                                     CHECK_IN_ORDINAL_ZONE, //
                                                                                     CHECK_IN_IS_WRITEABLE //
    );
}
