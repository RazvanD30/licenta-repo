package en.ubb.networkconfiguration.validation.exception.boundary;

public class NetworkAccessException extends BoundaryException {

    public NetworkAccessException(String details) {
        super("Network access error. " + details);
    }
}
