package en.ubb.networkconfiguration.boundary.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.network.runtime.RunConfigDto;
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
        ValidationUtils.rejectIfEmpty(errors, "networkId", "runConfig.networkId.empty");
        ValidationUtils.rejectIfEmpty(errors, "trainFileId", "runConfig.trainFileId.empty");
        ValidationUtils.rejectIfEmpty(errors, "testFileId", "runConfig.testFileId.empty");
    }
}
