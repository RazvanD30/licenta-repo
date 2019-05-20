package en.ubb.networkconfiguration.boundary.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.setup.LayerInitDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LayerInitDtoValidator implements Validator {


    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return LayerInitDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "type", "layer.type.empty");

        LayerInitDto layer = (LayerInitDto) target;

        if(layer.getNInputs() > 0) {
            ValidationUtils.rejectIfEmpty(errors, "activation", "layer.activation.empty");
        }
        if (layer.getNInputs() < 0) {
            errors.rejectValue("nInputs", "layer.nInputs.ltZero");
        }
        if (layer.getNNodes() <= 0) {
            errors.rejectValue("nNodes", "layer.nNodes.leZero");
        }
        if (layer.getNOutputs() < 0) {
            errors.rejectValue("nOutputs", "layer.nOutputs.ltZero");
        }
    }
}