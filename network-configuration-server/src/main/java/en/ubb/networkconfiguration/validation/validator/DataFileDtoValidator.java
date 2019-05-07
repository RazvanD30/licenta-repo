package en.ubb.networkconfiguration.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.setup.DataFileDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DataFileDtoValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return DataFileDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "networkId", "dataFile.networkId.empty");
        ValidationUtils.rejectIfEmpty(errors, "classPath", "dataFile.classPath.empty");
        ValidationUtils.rejectIfEmpty(errors, "type", "dataFile.type.empty");

        DataFileDto dto = (DataFileDto) target;
        if (dto.getNLabels() <= 0) {
            errors.rejectValue("nLabels", "dataFile.nLabels.leZero");
        }
    }
}
