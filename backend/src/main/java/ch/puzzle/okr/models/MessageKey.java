package ch.puzzle.okr.models;

public class MessageKey {
    private MessageKey() {
    }

    public static final String ATTRIBUTE_NOT_NULL = "ATTRIBUTE_NOT_NULL";
    public static final String ATTRIBUTE_NOT_BLANK = "ATTRIBUTE_NOT_BLANK";
    public static final String ATTRIBUTE_NOT_VALID = "ATTRIBUTE_NOT_VALID";
    public static final String ATTRIBUTE_SIZE_BETWEEN = "ATTRIBUTE_SIZE_BETWEEN_{min}_{max}";
    public static final String ATTRIBUTE_MIN_VALUE = "ATTRIBUTE_MIN_VALUE_{value}";
    public static final String ATTRIBUTE_MAX_VALUE = "ATTRIBUTE_MAX_VALUE_{value}";
}
