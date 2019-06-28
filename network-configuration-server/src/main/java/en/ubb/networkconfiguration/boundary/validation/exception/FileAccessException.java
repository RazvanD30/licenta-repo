package en.ubb.networkconfiguration.boundary.validation.exception;

public class FileAccessException extends BoundaryException {

    public FileAccessException() {
    }

    public FileAccessException(String message) {
        super(message);
    }

    public FileAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileAccessException(Throwable cause) {
        super(cause.getMessage());
    }

    public FileAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
