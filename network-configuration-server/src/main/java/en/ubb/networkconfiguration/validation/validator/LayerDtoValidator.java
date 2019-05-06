package en.ubb.networkconfiguration.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.runtime.LayerDto;
import en.ubb.networkconfiguration.boundary.dto.runtime.NodeDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LayerDtoValidator implements Validator {

    private final Validator nodeDtoValidator;

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return LayerDto.class.equals(clazz);
    }

    public LayerDtoValidator(Validator nodeDtoValidator) {
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
        ValidationUtils.rejectIfEmpty(errors, "type", "type.empty");
        ValidationUtils.rejectIfEmpty(errors, "activation", "activation.empty");
        ValidationUtils.rejectIfEmpty(errors, "nodes", "nodes.empty");

        LayerDto layer = (LayerDto) target;

        if (layer.getNInputs() < 0) {
            errors.rejectValue("nInputs", "nInputs.ltZero");
        }
        if (layer.getNNodes() <= 0) {
            errors.rejectValue("nNodes", "nNodes.leZero");
        }
        if (layer.getNOutputs() < 0) {
            errors.rejectValue("nOutputs", "nOutputs.ltZero");
        }
        if (layer.getNodes() != null) {
            if (layer.getNodes().isEmpty()) {
                errors.rejectValue("nodes", "nodes.listEmpty");
            } else if (layer.getNNodes() != layer.getNodes().size()) {
                errors.rejectValue("nodes", "nodes.ne.nNodes");
                errors.rejectValue("nNodes", "nNodes.ne.nodes");
            }
            layer.getNodes().forEach(node -> {
                try {
                    errors.pushNestedPath("node");
                    ValidationUtils.invokeValidator(this.nodeDtoValidator, node, errors);
                } finally {
                    errors.popNestedPath();
                }
            });
        }
    }
}
