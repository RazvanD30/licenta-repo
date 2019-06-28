package en.ubb.networkconfiguration.boundary.validation.exception;

public class NetworkNotFoundException extends NotFoundException {

    public NetworkNotFoundException() {
    }

    public NetworkNotFoundException(String message) {
        super(message);
    }

    public NetworkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkNotFoundException(Throwable cause) {
        super(cause.getMessage());
    }

    public NetworkNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
