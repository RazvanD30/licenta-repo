package en.ubb.networkconfiguration.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.runtime.RunConfigDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RunConfigDtoValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return RunConfigDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmpty(errors,"networkId","networkId.empty");
        ValidationUtils.rejectIfEmpty(errors,"trainFileId","trainFileId.empty");
        ValidationUtils.rejectIfEmpty(errors,"testFileId","testFileId.empty");
    }
}
