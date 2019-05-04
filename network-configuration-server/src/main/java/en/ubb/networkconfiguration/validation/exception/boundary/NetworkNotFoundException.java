package en.ubb.networkconfiguration.validation.exception.boundary;

public class NetworkNotFoundException extends BoundaryException {

    public NetworkNotFoundException(Long id) {
        super("Could not find network with id [" + id + "].");
    }
}
