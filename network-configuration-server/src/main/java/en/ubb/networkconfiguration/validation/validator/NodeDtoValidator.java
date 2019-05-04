package en.ubb.networkconfiguration.validation.validator;

import en.ubb.networkconfiguration.dto.NodeDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class NodeDtoValidator implements Validator {
    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return NodeDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmpty(errors,"links","links.empty");
        // the output nodes do not have any links, so no validation on this
    }
}
