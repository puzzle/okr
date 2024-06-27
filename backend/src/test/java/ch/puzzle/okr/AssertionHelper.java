package ch.puzzle.okr;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class AssertionHelper {

    public static void assertOkrResponseStatusException(OkrResponseStatusException exception,
            List<ErrorDto> expectedErrors) {

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    public static void assertOkrResponseStatusException(HttpStatus statusCode, OkrResponseStatusException exception,
            List<ErrorDto> expectedErrors) {

        assertEquals(statusCode, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

}
