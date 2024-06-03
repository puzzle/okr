package ch.puzzle.okr.mapper.checkin.helper;

import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AssertHelper {

    public static void assertCheckInMetricDto(CheckInMetric expected, CheckInMetricDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getChangeInfo(), actual.changeInfo());
        assertEquals(expected.getInitiatives(), actual.initiatives());
        assertEquals(expected.getConfidence(), actual.confidence());
        assertEquals(expected.getKeyResult().getId(), actual.keyResultId());
        assertEquals(expected.getCreatedOn(), actual.createdOn());
        assertEquals(expected.getModifiedOn(), actual.modifiedOn());
        assertEquals(expected.getValue(), actual.value());
        assertEquals(expected.isWriteable(), actual.writeable());
    }

    public static void assertCheckInMetric(CheckInMetricDto expected, CheckInMetric actual) {
        assertEquals(expected.id(), actual.getId());
        assertEquals(expected.version(), actual.getVersion());
        assertEquals(expected.changeInfo(), actual.getChangeInfo());
        assertEquals(expected.initiatives(), actual.getInitiatives());
        assertEquals(expected.confidence(), actual.getConfidence());
        assertEquals(expected.keyResultId(), actual.getKeyResult().getId());
        assertEquals(expected.createdOn(), actual.getCreatedOn());
        assertEquals(expected.modifiedOn(), actual.getModifiedOn());
        assertEquals(expected.value(), actual.getValue());
        assertFalse(actual.isWriteable()); // TODO: immer false?
    }

    public static void assertCheckInOrdinalDto(CheckInOrdinal expected, CheckInOrdinalDto actual) {
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getVersion(), actual.version());
        assertEquals(expected.getChangeInfo(), actual.changeInfo());
        assertEquals(expected.getInitiatives(), actual.initiatives());
        assertEquals(expected.getConfidence(), actual.confidence());
        assertEquals(expected.getKeyResult().getId(), actual.keyResultId());
        assertEquals(expected.getCreatedOn(), actual.createdOn());
        assertEquals(expected.getModifiedOn(), actual.modifiedOn());
        assertEquals(expected.getZone(), actual.value());
        assertEquals(expected.isWriteable(), actual.writeable());
    }

    public static void assertCheckInOrdinal(CheckInOrdinalDto checkInOrdinalDto, CheckInOrdinal checkInOrdinal) {
        assertEquals(checkInOrdinalDto.id(), checkInOrdinal.getId());
        assertEquals(checkInOrdinalDto.version(), checkInOrdinal.getVersion());
        assertEquals(checkInOrdinalDto.changeInfo(), checkInOrdinal.getChangeInfo());
        assertEquals(checkInOrdinalDto.initiatives(), checkInOrdinal.getInitiatives());
        assertEquals(checkInOrdinalDto.confidence(), checkInOrdinal.getConfidence());
        assertEquals(checkInOrdinalDto.keyResultId(), checkInOrdinal.getKeyResult().getId());
        assertEquals(checkInOrdinalDto.createdOn(), checkInOrdinal.getCreatedOn());
        assertEquals(checkInOrdinalDto.modifiedOn(), checkInOrdinal.getModifiedOn());
        assertEquals(checkInOrdinalDto.value(), checkInOrdinal.getZone()); // TODO: API - rename value -> zone in dto
        assertFalse(checkInOrdinal.isWriteable()); // TODO: immer false?
    }
}
