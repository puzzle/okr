package ch.puzzle.okr.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessExceptionTest {

    @Test
    void shouldInstantiateCorrectly() {
        BusinessException businessException = new BusinessException(22, "Error twenty-two");
        assertEquals(22, businessException.getCode());
        assertEquals("Error twenty-two", businessException.getMessage());
        assertEquals(22, businessException.getClientMessage().code);
        assertEquals("Error twenty-two", businessException.getClientMessage().message);
    }
}