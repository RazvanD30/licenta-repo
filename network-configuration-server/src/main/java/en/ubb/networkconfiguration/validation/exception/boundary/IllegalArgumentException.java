package en.ubb.networkconfiguration.validation.exception.boundary;

public class IllegalArgumentException extends BoundaryException {

    public IllegalArgumentException(String argument, String expectation) {
        super("Expected " + expectation + ", received " + argument + ".");
    }
}
