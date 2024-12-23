package ch.puzzle.okr.test;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import java.time.LocalDateTime;

public class CheckInTestHelpers {
    public static final String CHANGE_INFO = "ChangeInfo";
    public static final String CHANGE_INFO_1 = "ChangeInfo1";
    public static final String CHANGE_INFO_2 = "ChangeInfo2";

    public static final String INITIATIVES = "Initiatives";
    public static final String INITIATIVES_1 = "Initiatives1";
    public static final String INITIATIVES_2 = "Initiatives2";

    public static final String CHECK_IN_5_URL = "/api/v2/checkins/5";
    public static final String CHECK_IN_BASE_URL = "/api/v2/checkins";

    public static final String JSON_CHANGE_INFO = "changeinfo";
    public static final String JSON_INITIATIVES = "initiatives";
    public static final String JSON = "{\"" + JSON_CHANGE_INFO + "\":" + "\"" + CHANGE_INFO_1 + "\""
                                      + ", \"keyResultId\":  1}";
    public static final String JSON_WITHOUT_KEY_RESULT_ID = "{\"" + JSON_CHANGE_INFO + "\":" + "\"" + CHANGE_INFO_1
                                                            + "\"}";

    public static final String JSON_PATH_ID = "$.id";
    public static final String JSON_PATH_CHANGE_INFO = "$.changeInfo";
    public static final String JSON_PATH_INITIATIVES = "$.initiatives";
    public static final String JSON_PATH_CONFIDENCE = "$.confidence";
    public static final String JSON_PATH_KEY_RESULT_ID = "$.keyResultId";
    public static final String JSON_PATH_MODIFIED_ON = "$.modifiedOn";
    public static final String JSON_PATH_CREATED_ON = "$.createdOn";

    public static final String JSON_PATH_VALUE = "$.value";
    public static final String JSON_PATH_ZONE = "$.zone";

    /* Test entities */
    static final Objective objective = Objective.Builder.builder().withId(1L).build();
    public static final CheckIn checkInMetric = CheckInMetric.Builder
            .builder()
            .withValue(30D)
            .withConfidence(5)
            .withChangeInfo(CHANGE_INFO)
            .withInitiatives(INITIATIVES)
            .withCreatedBy(User.Builder.builder().withId(1L).withFirstName("Frank").build())
            .withKeyResult(KeyResultMetric.Builder
                    .builder()
                    .withBaseline(3.0)
                    .withStretchGoal(6.0)
                    .withId(8L)
                    .withObjective(objective)
                    .build())
            .build();
    public static final CheckIn checkInOrdinal = CheckInOrdinal.Builder
            .builder()
            .withZone(Zone.COMMIT)
            .withId(4L)
            .withCreatedBy(User.Builder.builder().withId(2L).withFirstName("Robert").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withChangeInfo(CHANGE_INFO)
            .withInitiatives(INITIATIVES)
            .withKeyResult(KeyResultOrdinal.Builder
                    .builder()
                    .withCommitZone("Baum")
                    .withTargetZone("Wald")
                    .withId(9L)
                    .build())
            .build();

    /* Test DTOs */
    public static final CheckInDto checkInMetricDto = new CheckInMetricDto(5L,
                                                                           1,
                                                                           CHANGE_INFO_1,
                                                                           INITIATIVES_1,
                                                                           6,
                                                                           1L,
                                                                           LocalDateTime.MAX,
                                                                           LocalDateTime.MAX,
                                                                           46D,
                                                                           true);
    public static final CheckInDto checkInOrdinalDto = new CheckInOrdinalDto(4L,
                                                                             1,
                                                                             CHANGE_INFO_2,
                                                                             INITIATIVES_2,
                                                                             5,
                                                                             2L,
                                                                             LocalDateTime.MAX,
                                                                             LocalDateTime.MAX,
                                                                             Zone.COMMIT,
                                                                             true);

    public static final String CHECK_IN_METRIC_JSON = """
            {
              "id": 42,
              "version": 0,
              "changeInfo": "Change_Info",
              "initiatives": "Initiatives",
              "confidence": 5,
              "keyResultId": 1000,
              "createdOn": null,
              "modifiedOn": null,
              "value": 23.0,
              "isWriteable": false
            }
            """;

    public static final String CHECK_IN_ORDINAL_JSON = """
            {
            "id": 43,
            "version": 0,
            "changeInfo": "Change_Info",
            "initiatives": "Initiatives",
            "confidence": 7,
            "keyResultId": 1001,
            "createdOn": null,
            "modifiedOn": null,
            "zone": "STRETCH",
            "isWriteable": false
            }
            """;

    private CheckInTestHelpers() {
    }

}
