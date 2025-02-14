package ch.puzzle.okr;

import static java.util.Map.entry;

import ch.puzzle.okr.dto.checkin.*;
import ch.puzzle.okr.dto.keyresult.*;
import java.util.Map;

public class Constants {
    private Constants() {
    }

    public static final String KEY_RESULT_TYPE_METRIC = "metric";
    public static final String KEY_RESULT_TYPE_ORDINAL = "ordinal";
    public static final String OBJECTIVE = "Objective";
    public static final String STATE_DRAFT = "Draft";
    public static final String KEY_RESULT = "KeyResult";
    public static final String CHECK_IN = "Check-in";
    public static final String ACTION = "Action";
    public static final String UNIT = "Unit";
    public static final String ALIGNMENT = "Alignment";
    public static final String COMPLETED = "Completed";
    public static final String QUARTER = "Quarter";
    public static final String TEAM = "Team";
    public static final String USER = "User";
    public static final String USER_TEAM = "UserTeam";

    public static final String BACKLOG_QUARTER_LABEL = "Backlog";
    public static final String CHECK_IN_KEY_RESULT_ID_ATTRIBUTE_NAME = "keyResultId";
    public static final String KEY_RESULT_TYPE_ATTRIBUTE_NAME = "keyResultType";

    public static final Map<String, Class<? extends KeyResultDto>> KEY_RESULT_MAP = Map
            .ofEntries(entry(KEY_RESULT_TYPE_METRIC, KeyResultMetricDto.class),
                       entry(KEY_RESULT_TYPE_ORDINAL, KeyResultOrdinalDto.class));

    public static final Map<String, Class<? extends CheckInDto>> CHECK_IN_MAP = Map
            .ofEntries(entry(KEY_RESULT_TYPE_METRIC, CheckInMetricDto.class),
                       entry(KEY_RESULT_TYPE_ORDINAL, CheckInOrdinalDto.class));
}
