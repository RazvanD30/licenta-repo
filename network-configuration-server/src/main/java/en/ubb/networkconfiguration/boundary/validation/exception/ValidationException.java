package en.ubb.networkconfiguration.boundary.validation.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationException extends BoundaryException {

    private BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        super();
        this.bindingResult = bindingResult;
    }
}
