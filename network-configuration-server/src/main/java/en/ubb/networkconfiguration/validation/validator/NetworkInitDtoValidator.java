package en.ubb.networkconfiguration.validation.validator;

import en.ubb.networkconfiguration.boundary.dto.setup.LayerInitDto;
import en.ubb.networkconfiguration.boundary.dto.setup.NetworkInitDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class NetworkInitDtoValidator implements Validator {

    private final LayerInitDtoValidator layerInitDtoValidator;

    @Autowired
    public NetworkInitDtoValidator(LayerInitDtoValidator layerInitDtoValidator) {
        if (layerInitDtoValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is required and must not be null.");
        }
        if (!layerInitDtoValidator.supports(LayerInitDto.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must support the validation of [LayerInitDto] instances.");
        }
        this.layerInitDtoValidator = layerInitDtoValidator;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return NetworkInitDto.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "network.name.empty");
        ValidationUtils.rejectIfEmpty(errors, "layers", "network.layers.null");
        NetworkInitDto network = (NetworkInitDto) target;
        if (network.getLearningRate() <= 0) {
            errors.rejectValue("learningRate", "network.learningRate.leZero");
        }
        if (network.getBatchSize() <= 0) {
            errors.rejectValue("batchSize", "network.batchSize.leZero");
        }
        if (network.getNEpochs() <= 0) {
            errors.rejectValue("nEpochs", "network.nInputs.leZero");
        }
        if (network.getNInputs() <= 0) {
            errors.rejectValue("nInputs", "network.nInputs.leZero");
        }
        if (network.getNOutputs() <= 0) {
            errors.rejectValue("nOutputs", "network.nOutputs.leZero");
        }
        if (network.getLayers() != null) {
            if (network.getLayers().isEmpty()) {
                errors.rejectValue("layers", "network.layers.empty");
            }
            for (int i = 0; i < network.getLayers().size() - 1; i++) {
                LayerInitDto current = network.getLayers().get(i);
                LayerInitDto next = network.getLayers().get(i + 1);
                if (current.getNNodes() != next.getNInputs() || current.getNOutputs() != next.getNNodes()) {
                    errors.rejectValue("layers", "network.layers.invalidLinkConfig");
                }
            }
            for (int i = 0; i < network.getLayers().size(); i++) {
                errors.pushNestedPath("layers[" + i + "]");
                ValidationUtils.invokeValidator(this.layerInitDtoValidator, network.getLayers().get(i), errors);
                errors.popNestedPath();
            }
        }
    }
}
