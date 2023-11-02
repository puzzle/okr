package ch.puzzle.okr;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;

public class KeyResultTestHelpers {

    public static final String DESCRIPTION = "Description";
    public static final String TITLE = "Keyresult 1";
    public static final String COMMIT_ZONE = "Eine Pflanze";
    public static final int CONFIDENCE = 6;
    public static final Number OBJECTIVE_ID = 1;
    public static final Number LAST_CHECK_IN_ID = 1;
    public static final Double BASELINE_VALUE = 1.0;
    public static final Double STRETCH_GOAL_VALUE = 5.0;
    public static final String TARGET_ZONE = "Ein Baum";
    public static final String STRETCH_ZONE = "Ein Wald";
    public static final String QUARTER_LABEL = "GJ 22/23-Q4";
    public static final Zone LAST_CHECK_IN_ZONE = Zone.COMMIT;
    public static final String FIRSTNAME = "Johnny";
    public static final String LASTNAME = "Appleseed";
    public static final String START_DATE = "-999999999-01-01";
    public static final Unit KEY_RESULT_UNIT = Unit.FTE;
    public static final String KEY_RESULT_CREATED_ON = "-999999999-01-01T00:00:00";
    public static final String OBJECTIVE_STATE_ONGOING = "ONGOING";
    public static final String JSON_PATH_ID = "$.id";
    public static final String JSON_PATH_TITLE = "$.title";
    public static final String JSON_PATH_UNIT = "$.unit";
    public static final String JSON_PATH_BASELINE = "$.baseline";
    public static final String JSON_PATH_OBJECTIVE_ID = "$.objective.id";
    public static final String JSON_PATH_DESCRIPTION = "$.description";
    public static final String JSON_PATH_KEY_RESULT_TYPE = "$.keyResultType";
    public static final String JSON_PATH_STRETCH_GOAL = "$.stretchGoal";
    public static final String JSON_PATH_OWNER_FIRSTNAME = "$.owner.firstname";
    public static final String JSON_PATH_OBJECTIVE_STATE = "$.objective.state";
    public static final String JSON_PATH_ID_LAST_CHECK_IN_VALUE = "$.lastCheckIn.value";
    public static final String JSON_PATH_LAST_CHECK_IN_CONFIDENCE = "$.lastCheckIn.confidence";
    public static final String JSON_PATH_CREATED_ON = "$.createdOn";
    public static final String JSON_PATH_COMMIT_ZONE = "$.commitZone";
    public static final String JSON_PATH_TARGET_ZONE = "$.targetZone";
    public static final String JSON_PATH_STRETCH_ZONE = "$.stretchZone";
    public static final String JSON_PATH_LAST_CHECK_IN_ID = "$.lastCheckIn.id";
    public static final String JSON_PATH_LAST_CHECK_IN_ZONE = "$.lastCheckIn.value";
    public static final String JSON_PATH_QUARTER_LABEL = "$.objective.keyResultQuarterDto.label";
    public static final String JSON_PATH_QUARTER_START_DATE = "$.objective.keyResultQuarterDto.startDate";
    public static final String JSON = "{\"title\":  \"Keyresult 1\",\"keyResultType\":  \"metric\"}";
    public static final String URL_BASE = "/api/v2/keyresults";
    public static final String URL_TO_KEY_RESULT_1 = "/api/v2/keyresults/1";
    public static final String URL_TO_KEY_RESULT_10 = "/api/v2/keyresults/10";
    public static final String URL_TO_KEY_RESULT_55 = "/api/v2/keyresults/55";
    public static final String URL_TO_KEY_RESULT_1000 = "/api/v2/keyresults/1000";
    public static final String URL_TO_KEY_RESULT_5_CHECK_IN = "/api/v2/keyresults/5/checkins";
    public static final String URL_TO_KEY_RESULT_1_CHECK_IN = "/api/v2/keyresults/1/checkins";

    static final String INITIATIVES_1 = "Initiatives1";
    static final String INITIATIVES_2 = "Initiatives2";
    static final String CHANGE_INFO_1 = "Changeinfo1";
    static final String CHANGE_INFO_2 = "Changeinfo2";
    static final User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
            .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
    public static final KeyResult metricKeyResult = KeyResultMetric.Builder.builder().withId(5L).withTitle(TITLE)
            .build();
    public static final CheckIn checkIn1 = CheckInMetric.Builder.builder().withValue(23D).withId(1L)
            .withKeyResult(metricKeyResult).withCreatedBy(user).withCreatedOn(LocalDateTime.MAX)
            .withChangeInfo(CHANGE_INFO_1).withInitiatives(INITIATIVES_1).build();
    public static final CheckIn checkIn2 = CheckInMetric.Builder.builder().withValue(12D).withId(4L)
            .withKeyResult(metricKeyResult).withCreatedBy(user).withCreatedOn(LocalDateTime.MAX)
            .withChangeInfo(CHANGE_INFO_2).withInitiatives(INITIATIVES_2).build();
    public static final CheckInDto checkInDto1 = new CheckInMetricDto(1L, 1, CHANGE_INFO_1, INITIATIVES_1, 6,
            metricKeyResult.getId(), LocalDateTime.MAX, LocalDateTime.MAX, 23D, true);
    public static final CheckInDto checkInDto2 = new CheckInMetricDto(4L, 1, CHANGE_INFO_2, INITIATIVES_2, 5,
            metricKeyResult.getId(), LocalDateTime.MAX, LocalDateTime.MAX, 12D, true);

    public static final KeyResultUserDto keyResultUserDto = new KeyResultUserDto(1L, FIRSTNAME, LASTNAME);
    public static final KeyResultQuarterDto keyResultQuarterDto = new KeyResultQuarterDto(1L, QUARTER_LABEL,
            LocalDate.MIN, LocalDate.MAX);
    public static final KeyResultLastCheckInMetricDto keyResultLastCheckInDto = new KeyResultLastCheckInMetricDto(1L, 1,
            4.0, 6, LocalDateTime.MIN, CHANGE_INFO_1, INITIATIVES_1);
    public static final KeyResultLastCheckInOrdinalDto keyResultLastCheckInOrdinalDto = new KeyResultLastCheckInOrdinalDto(
            1L, 1, LAST_CHECK_IN_ZONE, 6, LocalDateTime.MIN, CHANGE_INFO_2, INITIATIVES_2);
    public static final KeyResultObjectiveDto keyResultObjectiveDto = new KeyResultObjectiveDto(1L,
            OBJECTIVE_STATE_ONGOING, keyResultQuarterDto);

    public static final KeyResultMetricDto keyResultMetricDto = new KeyResultMetricDto(5L, 1, KEY_RESULT_TYPE_METRIC,
            TITLE, DESCRIPTION, 1.0, 5.0, KEY_RESULT_UNIT, keyResultUserDto, keyResultObjectiveDto,
            keyResultLastCheckInDto, LocalDateTime.MIN, LocalDateTime.MAX, true, List.of());
    public static final KeyResultOrdinalDto keyResultOrdinalDto = new KeyResultOrdinalDto(5L, 1,
            KEY_RESULT_TYPE_ORDINAL, TITLE, DESCRIPTION, COMMIT_ZONE, TARGET_ZONE, STRETCH_ZONE, keyResultUserDto,
            keyResultObjectiveDto, keyResultLastCheckInOrdinalDto, LocalDateTime.MIN, LocalDateTime.MAX, true, List.of());
    public static final Objective objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    public static final KeyResult ordinalKeyResult = KeyResultOrdinal.Builder.builder().withId(3L)
            .withTitle("Keyresult 2").withOwner(user).withObjective(objective).build();

    public static final String CREATE_BODY_METRIC = """
            {
                "id":null,
                "objectiveId":5,
                "title":"",
                "description":"",
                "ownerId":5,
               "keyResultType":"metric",
               "createdById":5,
               "createdOn":null,
               "modifiedOn":null,
               "baseline":2.0,
               "stretchGoal":5.0,
               "unit":"FTE",
               "actionList":[]
            }
            """;

    public static final String CREATE_BODY_ORDINAL = """
            {
                "id":null,
                "objectiveId":5,
                "title":"",
                "description":"",
                "ownerId":5,
               "keyResultType":"ordinal",
               "createdById":5,
               "createdOn":null,
               "modifiedOn":null,
               "commitZone":"Eine Pflanze",
               "targetZone":"Ein Baum",
               "stretchZone":"Ein Wald",
               "actionList":[]
            }
            """;

    public static final String CREATE_BODY_ORDINAL_ACTION_LIST = """
            {
                "id":null,
                "objectiveId":5,
                "title":"",
                "description":"",
                "ownerId":5,
               "keyResultType":"ordinal",
               "createdById":5,
               "createdOn":null,
               "modifiedOn":null,
               "commitZone":"Eine Pflanze",
               "targetZone":"Ein Baum",
               "stretchZone":"Ein Wald",
               "actionList":[
               {
                        "id":null,
                        "action":"Neue Katze",
                        "priority":0,
                        "isChecked":true,
                        "keyResultId":null
                    },
                    {
                        "id":null,
                        "action":"Neuer Hund",
                        "priority":1,
                        "isChecked":false,
                        "keyResultId":null
                    }
                    ]
            }
            """;

    public static final String CREATE_BODY_KEY_RESULT_TYPE_MISSING = """
            {
               "objectiveId":5,
               "ownerId":5,
               "createdById":5,
               "commitZone":"Eine Pflanze",
               "targetZone":"Ein Baum",
               "stretchZone":"Ein Wald"
            }
            """;

    public static final String CREATE_BODY_KEY_RESULT_TYPE_UNKNOWN = """
            {
               "objectiveId":5,
               "ownerId":5,
               "keyResultType":"unknown",
               "createdById":5,
               "commitZone":"Eine Pflanze",
               "targetZone":"Ein Baum",
               "stretchZone":"Ein Wald"
            }
            """;

    public static final String CREATE_BODY_WITH_ENUM_KEYS = """
            {
                "id":null,
                "keyResultType":"metric",
                "objectiveId":5,
                "title":"",
                "description":"",
                "ownerId":5,
                "ownerFirstname":"",
                "ownerLastname":"",
                "createdById":5,
               "createdByFirstname":"",
               "createdByLastname":"",
               "createdOn":null,
               "modifiedOn":null,
               "baseline":2.0,
               "stretchGoal":5.0,
               "unit":"PERCENT",
               "actionList":[]
            }
            """;

    public static final String PUT_BODY_METRIC = """
            {
                "id":1,
                "keyResultType":"metric",
                "title":"Updated Keyresult",
                "description":"",
                "baseline":2.0,
                "stretchGoal":5.0,
                "unit":"NUMBER",
                "ownerId":5,
                "ownerFirstname":"",
                "ownerLastname":"",
                "objectiveId":5,
                "objectiveState":"INPROGRESS",
                "objectiveQuarterId":1,
                "objectiveQuarterLabel":"GJ 22/23-Q3",
                "objectiveQuarterStartDate":null,
                "objectiveQuarterEndDate":null,
                "lastCheckInId":1,
                "lastCheckInValue":4.0,
                "lastCheckInConfidence":6,
                "lastCheckInCreatedOn":null,
                "lastCheckInComment":"",
                "createdById":5,
                "createdByFirstname":"",
                "createdByLastname":"",
                "createdOn":null,
                "modifiedOn":null,
                "actionList":[]
            }
            """;

    private KeyResultTestHelpers() {
    }
}
