package ch.puzzle.okr.models.keyresult;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class KeyResultTest {

    @ParameterizedTest(name = "resetId() should set the ID to null for all key result types")
    @MethodSource("provideKeyResults")
    void resetIdShouldSetIdToNull(KeyResult keyResult) {

        keyResult.resetId();

        assertNull(keyResult.getId());
    }

    private static Stream<Arguments> provideKeyResults() {
        return Stream.of(Arguments.of(KeyResultMetric.Builder.builder().withId(1L).build()),
                Arguments.of(KeyResultOrdinal.Builder.builder().withId(1L).build()));
    }
}