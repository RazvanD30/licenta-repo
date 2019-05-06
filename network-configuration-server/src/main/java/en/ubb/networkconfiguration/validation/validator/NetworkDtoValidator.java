package en.ubb.networkconfiguration.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.runtime.LayerDto;
import en.ubb.networkconfiguration.boundary.dto.runtime.NetworkDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class NetworkDtoValidator implements Validator {

    private final Validator layerDtoValidator;

    public NetworkDtoValidator(Validator layerDtoValidator) {
        if (layerDtoValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is required and must not be null.");
        }
        if (!layerDtoValidator.supports(LayerDto.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must support the validation of [LayerDto] instances.");
        }
        this.layerDtoValidator = layerDtoValidator;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return NetworkDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        ValidationUtils.rejectIfEmpty(errors, "layers", "layers.empty");
        NetworkDto network = (NetworkDto) target;
        if (network.getLearningRate() <= 0) {
            errors.rejectValue("learningRate", "learningRate.leZero");
        }
        if (network.getBatchSize() <= 0) {
            errors.rejectValue("batchSize", "batchSize.leZero");
        }
        if (network.getNEpochs() <= 0) {
            errors.rejectValue("nEpochs", "nInputs.leZero");
        }
        if (network.getNInputs() <= 0) {
            errors.rejectValue("nInputs", "nInputs.leZero");
        }
        if (network.getNOutputs() <= 0) {
            errors.rejectValue("nOutputs", "nOutputs.leZero");
        }
        if (network.getLayers() != null) {
            if (network.getLayers().isEmpty()) {
                errors.rejectValue("layers", "layers.listEmpty");
            }
            for(int i = 0; i < network.getLayers().size() - 1; i++){
                LayerDto current = network.getLayers().get(i);
                LayerDto next = network.getLayers().get(i+1);
                if(current.getNNodes() != next.getNInputs() || current.getNOutputs() != next.getNNodes()){
                    errors.rejectValue("layers", "layers.invalidLinkConfig");
                }
            }
            network.getLayers().forEach(layer -> {
                try {
                    errors.pushNestedPath("layer");
                    ValidationUtils.invokeValidator(this.layerDtoValidator, layer, errors);
                } finally {
                    errors.popNestedPath();
                }
            });
        }
    }
}
