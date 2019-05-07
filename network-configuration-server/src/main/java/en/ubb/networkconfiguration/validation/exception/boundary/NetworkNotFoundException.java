package en.ubb.networkconfiguration.validation.exception.boundary;

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
        super(cause);
    }

    public NetworkNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
