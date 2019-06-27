package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.VirtualNetworkService;
import en.ubb.networkconfiguration.business.util.Holder;
import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.dao.*;
import en.ubb.networkconfiguration.persistence.dao.specification.VirtualNetworkSpec;
import en.ubb.networkconfiguration.persistence.domain.network.enums.VirtualNodeStatus;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualLayer;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualLink;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNetwork;
import en.ubb.networkconfiguration.persistence.domain.network.virtual.VirtualNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class VirtualNetworkServiceImpl implements VirtualNetworkService {

    private final VirtualNetworkRepo virtualNetworkRepo;

    private final VirtualLinkRepo virtualLinkRepo;

    private final VirtualNodeRepo virtualNodeRepo;

    private final NetworkRepo networkRepo;

    private final VirtualLayerRepo virtualLayerRepo;

    @Autowired
    public VirtualNetworkServiceImpl(VirtualNetworkRepo virtualNetworkRepo, VirtualLinkRepo virtualLinkRepo, VirtualNodeRepo virtualNodeRepo, VirtualLayerRepo virtualLayerRepo, NetworkRepo networkRepo) {
        this.virtualNetworkRepo = virtualNetworkRepo;
        this.virtualLinkRepo = virtualLinkRepo;
        this.virtualNodeRepo = virtualNodeRepo;
        this.virtualLayerRepo = virtualLayerRepo;
        this.networkRepo = networkRepo;
    }

    @Override
    public VirtualNetwork init(Network network, String name) {

        VirtualNetwork virtualNetwork = virtualNetworkRepo.save(VirtualNetwork.builder()
                .name(name)
                .createDateTime(LocalDateTime.now())
                .network(network)
                .layers(new ArrayList<>())
                .build());

        network.getLayers().forEach(layer -> {
            VirtualLayer virtualLayer = virtualLayerRepo.save(VirtualLayer.builder()
                    .layer(layer)
                    .virtualNetwork(virtualNetwork)
                    .nodes(new ArrayList<>())
                    .build());
            virtualNetwork.addLayer(virtualLayer);


            Holder<Integer> position = new Holder<>(0);
            layer.getNodes().forEach(node -> {

                VirtualNode virtualNode = VirtualNode.builder()
                        .status(VirtualNodeStatus.IGNORE)
                        .value(null)
                        .node(node)
                        .position(position.held())
                        .inputsLinks(new ArrayList<>())
                        .outputLinks(new ArrayList<>())
                        .build();
                virtualLayer.addNode(virtualNodeRepo.save(virtualNode));
            });
            position.hold(position.held() + 1);
        });

        network.getLayers().forEach(layer -> {
            layer.getNodes().forEach(node -> {
                virtualNetwork.getLayers().forEach(virtualLayer -> {
                    virtualLayer.getNodes().forEach(virtualNode -> {
                        if (virtualNode.getNode().getId().equals(node.getId())) {
                            node.getOutputLinks().forEach(link -> {
                                VirtualLink virtualLink = VirtualLink.builder()
                                        .link(link)
                                        .destinationNode(VirtualNode.builder().id(link.getDestination().getId()).build())
                                        .build();
                                virtualNode.addOutputLink(virtualLink);
                            });


                            node.getInputLinks().forEach(link -> {
                                VirtualLink virtualLink = VirtualLink.builder()
                                        .link(link)
                                        .sourceNode(VirtualNode.builder().id(link.getSource().getId()).build())
                                        .build();
                                virtualNode.addInputLink(virtualLink);
                            });
                        }
                    });
                });
            });
        });


        for (int i = 1; i < virtualNetwork.getLayers().size(); i++) {
            VirtualLayer previousLayer = virtualNetwork.getLayers().get(i - 1);
            VirtualLayer currentLayer = virtualNetwork.getLayers().get(i);

            currentLayer.getNodes().forEach(node -> {
                node.getInputsLinks().forEach(inputLink -> {
                    for (VirtualNode prevN : previousLayer.getNodes()) {
                        if (prevN.getNode().getId().equals(inputLink.getSourceNode().getId())) {
                            inputLink.setSourceNode(prevN);
                            break;
                        }
                    }
                });
            });
            previousLayer.getNodes().forEach(node -> {
                node.getOutputLinks().forEach(outputLink -> {
                    for (VirtualNode currN : currentLayer.getNodes()) {
                        if (currN.getNode().getId().equals(outputLink.getDestinationNode().getId())) {
                            outputLink.setDestinationNode(currN);
                            break;
                        }
                    }
                });
            });
        }

        return virtualNetworkRepo.save(virtualNetwork);
    }

    @Override
    public List<VirtualNetwork> getForNetworkId(long networkId) throws NotFoundBussExc {

        return networkRepo.findById(networkId)
                .orElseThrow(() -> new NotFoundBussExc("Network with id " + networkId + " not found"))
                .getVirtualNetworks();
    }

    @Override
    public Optional<VirtualNetwork> getByName(String name) {
        return virtualNetworkRepo.findOne(Specification.where(VirtualNetworkSpec.hasName(name)));
    }

    @Override
    public VirtualLayer getForIdAtPos(long id, int pos) throws NotFoundBussExc {
        VirtualNetwork virtualNetwork = this.findById(id)
                .orElseThrow(() -> new NotFoundBussExc("Virtual network with id " + id + " not found"));

        if (pos > virtualNetwork.getLayers().size() - 1) {
            throw new NotFoundBussExc("Virtual layer at position " + pos + " not found");
        }
        return virtualNetwork.getLayers().get(pos);
    }

    @Override
    public VirtualNetwork reset(VirtualNetwork virtualNetwork) {
        virtualNetwork.getLayers().forEach(layer ->
                layer.getNodes().forEach(node -> {
                    node.setStatus(VirtualNodeStatus.IGNORE);
                    node.setValue(null);
                }));
        return virtualNetworkRepo.save(virtualNetwork);
    }

    @Override
    public VirtualNetwork setInput(VirtualNetwork virtualNetwork, List<Double> inputs) throws NetworkAccessBussExc {
        Iterator<VirtualLayer> iterator = virtualNetwork.getLayers().iterator();
        if (iterator.hasNext()) {
            List<VirtualNode> nodes = iterator.next().getNodes();
            if (nodes.size() != inputs.size()) {
                throw new NetworkAccessBussExc("Input size does not match the input layer neuron count");
            }
            nodes.forEach(node -> {
                node.setValue(inputs.remove(0));
                virtualNodeRepo.save(node);
            });
        }
        return virtualNetworkRepo.getOne(virtualNetwork.getId());
    }

    @Override
    public boolean hasNext(VirtualNetwork network) {
        for (VirtualLayer virtualLayer : network.getLayers()) {
            for (VirtualNode virtualNode : virtualLayer.getNodes()) {
                if (virtualNode.getValue() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public VirtualNetwork next(VirtualNetwork network) {
        boolean found = false;
        for (VirtualLayer virtualLayer : network.getLayers()) {
            if (found) {
                virtualLayer.getNodes().forEach(offlineNode -> {
                            offlineNode.setValue(offlineNode.getValue() + offlineNode.getNode().getBias());
                            virtualNodeRepo.save(offlineNode);
                        }
                );
                break;
            }
            for (VirtualNode virtualNode : virtualLayer.getNodes()) {

                if (virtualNode.getOutputLinks().stream()
                        .anyMatch(offlineLink -> offlineLink.getLink()
                                .getSource()
                                .getVirtualNodes()
                                .get(0)
                                .getValue() == null)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                virtualLayer.getNodes().forEach(offlineNode -> {
                    offlineNode.getOutputLinks().forEach(offlineLink -> {
                        VirtualNode target = offlineLink.getLink().getSource().getVirtualNodes().get(0);
                        double weight = offlineLink.getLink().getWeight();
                        //double bias = target.getNode().getBias();
                        target.setValue(target.getValue() + offlineNode.getValue() * weight);
                    });
                });
            }
        }
        return virtualNetworkRepo.getOne(network.getId());
    }

    @Override
    public Optional<VirtualNetwork> findForNetwork(Network network) {
        return virtualNetworkRepo.findAll().stream()
                .filter(offlineNetwork -> offlineNetwork.getNetwork().getId().equals(network.getId()))
                .findFirst();
    }

    @Override
    public List<VirtualNetwork> getAll() {
        return virtualNetworkRepo.findAll();
    }

    @Override
    public Optional<VirtualNetwork> findById(long id) {
        return virtualNetworkRepo.findById(id);
    }
}
