package en.ubb.networkconfiguration.boundary.validation.exception;

public class ForbiddenAccessException extends BoundaryException {

    public ForbiddenAccessException() {
    }

    public ForbiddenAccessException(String message) {
        super(message);
    }

    public ForbiddenAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenAccessException(Throwable cause) {
        super(cause.getMessage());
    }

    public ForbiddenAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
