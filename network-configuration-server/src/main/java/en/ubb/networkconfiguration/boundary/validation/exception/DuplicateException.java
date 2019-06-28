package en.ubb.networkconfiguration.boundary.validation.exception;

public class DuplicateException extends BoundaryException {

    public DuplicateException() {
    }

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateException(Throwable cause) {
        super(cause.getMessage());
    }
}
