package ch.puzzle.okr.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

public class AssertionHelper {

    public static void assertOkrResponseStatusException(OkrResponseStatusException exception,
                                                        List<ErrorDto> expectedErrors) {

        assertOkrResponseStatusException(BAD_REQUEST, exception, expectedErrors);
    }

    public static void assertOkrResponseStatusException(HttpStatus statusCode, OkrResponseStatusException exception,
                                                        List<ErrorDto> expectedErrors) {

        assertEquals(statusCode, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        Assertions.assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

}
