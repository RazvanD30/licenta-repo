package en.ubb.networkconfiguration.validation.exception.boundary;

public class NetworkValidationException extends BoundaryException{

    public NetworkValidationException(String message) {
        super("Validation failed for those fields: " + message + ".");
    }
}
