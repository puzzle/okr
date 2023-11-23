package ch.puzzle.okr.models;

public class ErrorMsg {
    private ErrorMsg() {
    }

    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String KEYRESULT_CONVERSION = "KEYRESULT_CONVERSION";
    public static final String ALREADY_EXISTS_SAME_NAME = "ALREADY_EXISTS_SAME_NAME";
    public static final String CONVERT_TOKEN = "CONVERT_TOKEN";
    public static final String DATA_HAS_BEEN_UPDATED = "DATA_HAS_BEEN_UPDATED";
    // Model
    public static final String MODEL_NULL = "MODEL_NULL";
    public static final String MODEL_WITH_ID_NOT_FOUND = "MODEL_WITH_ID_NOT_FOUND";

    // Attributes
    public static final String ATTRIBUTE_NULL = "ATTRIBUTE_NULL";
    public static final String ATTRIBUTE_NOT_NULL = "ATTRIBUTE_NOT_NULL";
    public static final String ATTRIBUTE_CHANGED = "ATTRIBUTE_CHANGED";
    public static final String ATTRIBUTE_NOT_BLANK = "ATTRIBUTE_NOT_BLANK";
    public static final String ATTRIBUTE_NOT_VALID = "ATTRIBUTE_NOT_VALID";
    public static final String ATTRIBUTE_SIZE_BETWEEN = "ATTRIBUTE_SIZE_BETWEEN_{min}_{max}";
    public static final String ATTRIBUTE_SET_FORBIDDEN = "ATTRIBUTE_SET_FORBIDDEN";
    public static final String ATTRIBUTE_NOT_SET = "ATTRIBUTE_NOT_SET";
    public static final String ATTRIBUTE_CANNOT_CHANGE = "ATTRIBUTE_CANNOT_CHANGE";
    public static final String NOT_AUTHORIZED_TO_READ = "NOT_AUTHORIZED_TO_READ";
    public static final String NOT_AUTHORIZED_TO_WRITE = "NOT_AUTHORIZED_TO_WRITE";
    public static final String NOT_AUTHORIZED_TO_DELETE = "NOT_AUTHORIZED_TO_DELETE";
    public static final String TOKEN_NULL = "TOKEN_NULL";
}
