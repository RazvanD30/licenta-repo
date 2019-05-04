package en.ubb.networkconfiguration.dto.util;

import en.ubb.networkconfiguration.domain.network.runtime.Layer;
import en.ubb.networkconfiguration.domain.network.runtime.Link;
import en.ubb.networkconfiguration.domain.network.runtime.Network;
import en.ubb.networkconfiguration.domain.network.runtime.Node;
import en.ubb.networkconfiguration.dto.LayerDto;
import en.ubb.networkconfiguration.dto.LinkDto;
import en.ubb.networkconfiguration.dto.NetworkDto;
import en.ubb.networkconfiguration.dto.NodeDto;

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

}
