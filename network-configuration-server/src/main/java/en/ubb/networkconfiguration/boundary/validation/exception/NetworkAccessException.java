package en.ubb.networkconfiguration.boundary.validation.exception;

public class NetworkAccessException extends BoundaryException {

    public NetworkAccessException() {
    }

    public NetworkAccessException(String message) {
        super(message);
    }

    public NetworkAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkAccessException(Throwable cause) {
        super(cause);
    }

    public NetworkAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
