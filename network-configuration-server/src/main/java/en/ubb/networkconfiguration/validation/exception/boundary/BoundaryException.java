package en.ubb.networkconfiguration.validation.exception.boundary;

public class BoundaryException extends RuntimeException {

    public BoundaryException() {
        super();
    }

    public BoundaryException(String message) {
        super(message);
    }

    public BoundaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoundaryException(Throwable cause) {
        super(cause);
    }

    protected BoundaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
