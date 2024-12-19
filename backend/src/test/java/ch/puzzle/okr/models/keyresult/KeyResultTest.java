package ch.puzzle.okr.models.keyresult;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class KeyResultTest {

    @ParameterizedTest
    @MethodSource("provideKeyResults")
    void resetIdShouldSetIdToNull(KeyResult keyResult) {

        keyResult.resetId();

        assertNull(keyResult.getId());
    }

    private static Stream<Arguments> provideKeyResults() {
        return Stream
                .of(Arguments.of(KeyResultMetric.Builder.builder().withId(1L).build()),
                    Arguments.of(KeyResultOrdinal.Builder.builder().withId(1L).build()));
    }

}