package en.ubb.networkconfiguration.validation.exception.boundary;

public class NetworkNotFoundException extends NotFoundException {

    public NetworkNotFoundException(Long id) {
        super("Could not find network with id [" + id + "].");
    }
}
