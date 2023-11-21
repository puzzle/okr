package ch.puzzle.okr.models;

public class ErrorMsg {
    private ErrorMsg() {
    }

    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String KEYRESULT_CONVERSION = "KEYRESULT_CONVERSION";
    public static final String ALREADY_EXISTS_SAME_NAME = "ALREADY_EXISTS_SAME_NAME";
    public static final String CONVERT_TOKEN = "CONVERT_TOKEN";
    public static final String UNSUPPORTED_METHOD_IN_CLASS = "UNSUPPORTED_METHOD_IN_CLASS";
    public static final String MODEL_NULL = "MODEL_NULL";
    public static final String ATTRIBUTE_NULL = "ATTRIBUTE_NULL";
    public static final String ATTRIBUTE_NOT_NULL = "ATTRIBUTE_NOT_NULL";
    public static final String ATTRIBUTE_CHANGED = "ATTRIBUTE_CHANGED";
    public static final String SIZE_BETWEEN = "SIZE_BETWEEN_{min}_{max}";
    public static final String EMPTY_ATTRIBUTE_ON_MODEL = "EMPTY_ATTRIBUTE_ON_MODEL";
    public static final String NULL_ATTRIBUTE_ON_MODEL = "NULL_ATTRIBUTE_ON_MODEL";
}
