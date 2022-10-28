package ch.puzzle.okr.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessClientMessageTest {

    @ParameterizedTest
    @CsvSource(value= {
            "Error twenty-two, Error twenty-two",
            ", A unknown Error is occurred!",
            "null, A unknown Error is occurred!"
            }, nullValues={"null"}
    )
    void shouldInstantiateCorrectly2(String message, String result) {
        BusinessClientMessage businessClientMessage = new BusinessClientMessage(22, message);
        assertEquals(22, businessClientMessage.code);
        assertEquals(result, businessClientMessage.message);
    }
}