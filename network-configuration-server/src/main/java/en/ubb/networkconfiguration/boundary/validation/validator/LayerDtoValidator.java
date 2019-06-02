package en.ubb.networkconfiguration.boundary.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.network.runtime.LayerDto;
import en.ubb.networkconfiguration.boundary.dto.network.runtime.NodeDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LayerDtoValidator implements Validator {

    private final NodeDtoValidator nodeDtoValidator;

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return LayerDto.class.equals(clazz);
    }

    @Autowired
    public LayerDtoValidator(NodeDtoValidator nodeDtoValidator) {
        if (nodeDtoValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is required and must not be null.");
        }
        if (!nodeDtoValidator.supports(NodeDto.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must support the validation of [NodeDto] instances.");
        }
        this.nodeDtoValidator = nodeDtoValidator;
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "type", "layer.type.empty");
        ValidationUtils.rejectIfEmpty(errors, "nodes", "layer.nodes.null");

        LayerDto layer = (LayerDto) target;

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
        if (layer.getNodes() != null) {
            if (layer.getNodes().isEmpty()) {
                errors.rejectValue("nodes", "layer.nodes.empty");
            } else if (layer.getNNodes() != layer.getNodes().size()) {
                errors.rejectValue("nNodes", "layer.nNodes.wrong");
            }
            for (int i = 0; i < layer.getNodes().size(); i++) {
                try {
                    errors.pushNestedPath("nodes[" + i + "]");
                    ValidationUtils.invokeValidator(this.nodeDtoValidator, layer.getNodes().get(i), errors);
                } finally {
                    errors.popNestedPath();
                }
            }
        }
    }
}
