package en.ubb.networkconfiguration.boundary.util;

import en.ubb.networkconfiguration.boundary.dto.authentication.PrivateUserDto;
import en.ubb.networkconfiguration.boundary.dto.authentication.PublicUserDto;
import en.ubb.networkconfiguration.boundary.dto.branch.BranchDto;
import en.ubb.networkconfiguration.boundary.dto.file.DataFileDto;
import en.ubb.networkconfiguration.boundary.dto.file.FileLinkDto;
import en.ubb.networkconfiguration.boundary.dto.network.log.NetworkIterationLogDto;
import en.ubb.networkconfiguration.boundary.dto.network.log.NetworkTrainLogDto;
import en.ubb.networkconfiguration.boundary.dto.network.runtime.LayerDto;
import en.ubb.networkconfiguration.boundary.dto.network.runtime.LinkDto;
import en.ubb.networkconfiguration.boundary.dto.network.runtime.NetworkDto;
import en.ubb.networkconfiguration.boundary.dto.network.runtime.NodeDto;
import en.ubb.networkconfiguration.boundary.dto.network.setup.LayerInitDto;
import en.ubb.networkconfiguration.boundary.dto.network.setup.NetworkInitDto;
import en.ubb.networkconfiguration.boundary.dto.network.virtual.VirtualLayerDto;
import en.ubb.networkconfiguration.boundary.dto.network.virtual.VirtualLinkDto;
import en.ubb.networkconfiguration.boundary.dto.network.virtual.VirtualNetworkDto;
import en.ubb.networkconfiguration.boundary.dto.network.virtual.VirtualNodeDto;
import en.ubb.networkconfiguration.persistence.domain.authentication.Authority;
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.*;
import en.ubb.networkconfiguration.persistence.domain.network.setup.LayerInitializer;
import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualLayer;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualLink;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNetwork;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNode;

import java.io.IOException;
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

    public static DataFileDto toDto(DataFile dataFile) {
        return DataFileDto.builder()
                .id(dataFile.getId())
                .name(dataFile.getName())
                .nLabels(dataFile.getNLabels())
                .build();
    }

    public static DataFile fromDto(DataFileDto dataFile) throws IOException {
        return DataFile.builder()
                .id(dataFile.getId())
                .name(dataFile.getName())
                .nLabels(dataFile.getNLabels())
                .build();
    }

    public static FileLinkDto toDto(NetworkFile networkFile) {
        return FileLinkDto.builder()
                .networkName(networkFile.getNetwork().getName())
                .fileName(networkFile.getDataFile().getName())
                .fileType(networkFile.getType())
                .build();
    }


    public static List<FileLinkDto> toDtos(DataFile dataFile) {
        return dataFile.getNetworks().stream()
                .map(fileNetwork -> FileLinkDto.builder()
                        .fileName(dataFile.getName())
                        .fileType(fileNetwork.getType())
                        .networkName(fileNetwork.getNetwork().getName())
                        .build())
                .collect(Collectors.toList());
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

    public static NetworkIterationLogDto toDto(NetworkIterationLog log) {
        return NetworkIterationLogDto.builder()
                .score(log.getScore())
                .iteration(log.getIteration())
                .build();
    }

    public static NetworkTrainLogDto toDto(NetworkTrainLog log) {
        return NetworkTrainLogDto.builder()
                .accuracy(log.getAccuracy())
                .createDateTime(log.getCreateDateTime())
                .f1Score(log.getF1Score())
                .precision(log.getPrecision())
                .recall(log.getRecall())
                .networkIterationLogs(log.getNetworkIterationLogs().stream()
                        .map(DtoMapper::toDto)
                        .collect(Collectors.toList())
                ).build();
    }

    public static User fromPrivateDto(PrivateUserDto dto) {
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    public static PrivateUserDto toPrivateDto(User user) {
        return PrivateUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getAuthorities().stream().map(Authority::getRole).collect(Collectors.toList()))
                .build();
    }

    public static User fromPublicDto(PublicUserDto dto) {
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .build();
    }

    public static PublicUserDto toPublicDto(User user) {
        return PublicUserDto.builder()
                .username(user.getUsername())
                .roles(user.getAuthorities().stream().map(Authority::getRole).collect(Collectors.toList()))
                .build();
    }

    public static NetworkBranch fromDto(BranchDto dto) {
        return NetworkBranch.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .createDateTime(dto.getCreateDateTime())
                .updateDateTime(dto.getUpdateDateTime())
                .owner(fromPublicDto(dto.getOwner()))
                .networks(new ArrayList<>())
                .contributors(dto.getContributors().stream()
                        .map(DtoMapper::fromPublicDto)
                        .collect(Collectors.toList())
                ).build();
    }

    public static BranchDto toDto(NetworkBranch branch) {
        return BranchDto.builder()
                .id(branch.getId())
                .name(branch.getName())
                .type(branch.getType())
                .createDateTime(branch.getCreateDateTime())
                .updateDateTime(branch.getUpdateDateTime())
                .owner(toPublicDto(branch.getOwner()))
                .contributors(branch.getContributors().stream()
                        .map(DtoMapper::toPublicDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static VirtualLinkDto toDto(VirtualLink virtualLink) {
        return VirtualLinkDto.builder()
                .id(virtualLink.getId())
                .weight(virtualLink.getLink().getWeight())
                .sourceNodeId(virtualLink.getSourceNode() == null ? null : virtualLink.getSourceNode().getId())
                .destinationNodeId(virtualLink.getDestinationNode() == null ? null : virtualLink.getDestinationNode().getId())
                .build();
    }

    public static VirtualNodeDto toDto(VirtualNode virtualNode) {
        return VirtualNodeDto.builder()
                .id(virtualNode.getId())
                .bias(virtualNode.getNode().getBias())
                .status(virtualNode.getStatus())
                .value(virtualNode.getValue())
                .position(virtualNode.getPosition())
                .outputLinks(virtualNode.getOutputLinks().stream()
                        .map(DtoMapper::toDto)
                        .collect(Collectors.toList()))
                .inputLinks(virtualNode.getInputsLinks().stream()
                        .map(DtoMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static VirtualLayerDto toDto(VirtualLayer virtualLayer) {
        return VirtualLayerDto.builder()
                .id(virtualLayer.getId())
                .type(virtualLayer.getLayer().getType())
                .activation(virtualLayer.getLayer().getActivation())
                .nodes(virtualLayer.getNodes().stream()
                        .map(DtoMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static VirtualNetworkDto toDto(VirtualNetwork virtualNetwork) {
        return VirtualNetworkDto.builder()
                .id(virtualNetwork.getId())
                .name(virtualNetwork.getName())
                .networkName(virtualNetwork.getNetwork().getName())
                .networkId(virtualNetwork.getNetwork().getId())
                .layerCount(virtualNetwork.getLayers().size())
                .build();
    }


}
