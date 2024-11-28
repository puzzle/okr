package ch.puzzle.okr.deserializer;

import com.fasterxml.jackson.databind.JsonNode;

public interface MetricOrdinalDeserializer {
    String getKeyResultType(JsonNode root);
}
