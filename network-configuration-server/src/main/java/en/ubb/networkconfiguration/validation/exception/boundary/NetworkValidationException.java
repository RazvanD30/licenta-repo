package en.ubb.networkconfiguration.validation.exception.boundary;

public class NetworkValidationException extends BoundaryException{

    public NetworkValidationException() {
    }

    public NetworkValidationException(String message) {
        super(message);
    }

    public NetworkValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkValidationException(Throwable cause) {
        super(cause);
    }

    public NetworkValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
