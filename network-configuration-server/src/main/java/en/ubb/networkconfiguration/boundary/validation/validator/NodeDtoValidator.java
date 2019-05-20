package en.ubb.networkconfiguration.boundary.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.runtime.NodeDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class NodeDtoValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return NodeDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmpty(errors,"links","node.links.null");
        // the output nodes do not have any links, so no validation on this
    }
}
