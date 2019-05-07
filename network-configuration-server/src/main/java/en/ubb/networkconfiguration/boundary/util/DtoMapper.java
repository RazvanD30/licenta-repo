package en.ubb.networkconfiguration.boundary.util;

import en.ubb.networkconfiguration.boundary.dto.runtime.LayerDto;
import en.ubb.networkconfiguration.boundary.dto.runtime.LinkDto;
import en.ubb.networkconfiguration.boundary.dto.runtime.NetworkDto;
import en.ubb.networkconfiguration.boundary.dto.runtime.NodeDto;
import en.ubb.networkconfiguration.boundary.dto.setup.DataFileDto;
import en.ubb.networkconfiguration.domain.network.runtime.*;
import en.ubb.networkconfiguration.domain.network.setup.LayerInitializer;
import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.boundary.dto.setup.LayerInitDto;
import en.ubb.networkconfiguration.boundary.dto.setup.NetworkInitDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {


    public static NetworkDto toDto(Network network) {
        return NetworkDto.builder()
                .id(network.getId())
                .batchSize(network.getBatchSize())
                .learningRate(network.getLearningRate())
                .nEpochs(network.getNEpochs())
                .name(network.getName())
                .nInputs(network.getNInputs())
                .nOutputs(network.getNOutputs())
                .seed(network.getSeed())
                .layers(network.getLayers().stream()
                        .map(DtoMapper::toDto)
                        .collect(Collectors.toList()))
                .build();

    }

    public static LayerDto toDto(Layer layer) {
        return LayerDto.builder()
                .id(layer.getId())
                .nInputs(layer.getNInputs())
                .nNodes(layer.getNNodes())
                .nOutputs(layer.getNOutputs())
                .activation(layer.getActivation())
                .type(layer.getType())
                .nodes(layer.getNodes().stream()
                        .map(DtoMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static NodeDto toDto(Node node) {
        return NodeDto.builder()
                .id(node.getId())
                .bias(node.getBias())
                .links(node.getOutputLinks().stream()
                        .map(DtoMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static LinkDto toDto(Link link) {
        return LinkDto.builder()
                .id(link.getId())
                .weight(link.getWeight())
                .build();
    }

    public static NetworkInitDto toDto(NetworkInitializer network) {
        return NetworkInitDto.builder()
                .id(network.getId())
                .batchSize(network.getBatchSize())
                .learningRate(network.getLearningRate())
                .nEpochs(network.getNEpochs())
                .name(network.getName())
                .nInputs(network.getNInputs())
                .nOutputs(network.getNOutputs())
                .seed(network.getSeed())
                .layers(network.getLayers().stream()
                        .map(DtoMapper::toDto)
                        .collect(Collectors.toList()))
                .build();

    }


    public static LayerInitDto toDto(LayerInitializer layer) {
        return LayerInitDto.builder()
                .id(layer.getId())
                .nInputs(layer.getNInputs())
                .nNodes(layer.getNNodes())
                .nOutputs(layer.getNOutputs())
                .activation(layer.getActivation())
                .type(layer.getType())
                .build();
    }

    public static Network fromDto(NetworkDto network) {
        return Network.builder()
                .id(network.getId())
                .batchSize(network.getBatchSize())
                .learningRate(network.getLearningRate())
                .nEpochs(network.getNEpochs())
                .name(network.getName())
                .nInputs(network.getNInputs())
                .nOutputs(network.getNOutputs())
                .seed(network.getSeed())
                .layers(network.getLayers().stream()
                        .map(DtoMapper::fromDto)
                        .collect(Collectors.toList()))
                .build();

    }

    public static Layer fromDto(LayerDto layer) {
        return Layer.builder()
                .id(layer.getId())
                .nInputs(layer.getNInputs())
                .nNodes(layer.getNNodes())
                .nOutputs(layer.getNOutputs())
                .activation(layer.getActivation())
                .type(layer.getType())
                .nodes(layer.getNodes().stream()
                        .map(DtoMapper::fromDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Node fromDto(NodeDto node) {
        return Node.builder()
                .id(node.getId())
                .bias(node.getBias())
                .outputLinks(node.getLinks().stream()
                        .map(DtoMapper::fromDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Link fromDto(LinkDto link) {
        return Link.builder()
                .id(link.getId())
                .weight(link.getWeight())
                .build();
    }

    public static NetworkInitializer fromDto(NetworkInitDto network) {
        return NetworkInitializer.builder()
                .id(network.getId())
                .batchSize(network.getBatchSize())
                .learningRate(network.getLearningRate())
                .nEpochs(network.getNEpochs())
                .name(network.getName())
                .nInputs(network.getNInputs())
                .nOutputs(network.getNOutputs())
                .seed(network.getSeed())
                .layers(network.getLayers().stream()
                        .map(DtoMapper::fromDto)
                        .collect(Collectors.toList()))
                .build();

    }


    public static LayerInitializer fromDto(LayerInitDto layer) {
        return LayerInitializer.builder()
                .id(layer.getId())
                .nInputs(layer.getNInputs())
                .nNodes(layer.getNNodes())
                .nOutputs(layer.getNOutputs())
                .activation(layer.getActivation())
                .type(layer.getType())
                .build();
    }

    public static List<DataFileDto> toDtos(DataFile dataFile) {
        List<DataFileDto> dtos = new ArrayList<>();

        dataFile.getNetworks().forEach(networkFile -> {
            DataFileDto dto = DataFileDto.builder()
                    .networkId(networkFile.getNetwork().getId())
                    .classPath(dataFile.getClassPath())
                    .nLabels(dataFile.getNLabels())
                    .type(networkFile.getType())
                    .build();
            dtos.add(dto);
        });
        return dtos;
    }

    public static DataFile fromDtoPartial(DataFileDto dto){
        return DataFile.builder()
                .id(dto.getId())
                .classPath(dto.getClassPath())
                .nLabels(dto.getNLabels())
                .build();
    }

}
