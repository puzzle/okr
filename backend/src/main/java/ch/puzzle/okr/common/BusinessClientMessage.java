package ch.puzzle.okr.common;

public class BusinessClientMessage {
    public final int code;
    public final String message;

    public BusinessClientMessage(int code, String message) {
        this.code = code;
        if (message == null || message.isBlank()) {
            this.message = "A unknown Error is occurred!";
        } else {
            this.message = message;
        }
    }
}
