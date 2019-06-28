package en.ubb.networkconfiguration.boundary.validation.exception;

public class BoundaryException extends Exception {

    public BoundaryException() {
    }

    public BoundaryException(String message) {
        super(message);
    }

    public BoundaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoundaryException(Throwable cause) {
        super(cause.getMessage());
    }

    public BoundaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
