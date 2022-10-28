package ch.puzzle.okr.common;

public class BusinessException extends Exception{
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public BusinessClientMessage getClientMessage() {
        return new BusinessClientMessage(this.code, this.getMessage());
    }
}
