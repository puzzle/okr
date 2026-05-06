package ch.puzzle.okr.deserializer;

import tools.jackson.databind.JsonNode;

public interface MetricOrdinalDeserializer {
    String getKeyResultType(JsonNode root);
}
