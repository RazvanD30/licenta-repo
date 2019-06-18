package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.OfflineNetworkService;
import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.persistence.dao.OfflineLayerRepo;
import en.ubb.networkconfiguration.persistence.dao.OfflineLinkRepo;
import en.ubb.networkconfiguration.persistence.dao.OfflineNetworkRepo;
import en.ubb.networkconfiguration.persistence.dao.OfflineNodeRepo;
import en.ubb.networkconfiguration.persistence.domain.network.enums.OfflineNodeStatus;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineLayer;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineLink;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineNetwork;
import en.ubb.networkconfiguration.persistence.domain.network.offline.OfflineNode;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class OfflineNetworkServiceImpl implements OfflineNetworkService {

    private final OfflineNetworkRepo offlineNetworkRepo;

    private final OfflineLinkRepo offlineLinkRepo;

    private final OfflineNodeRepo offlineNodeRepo;

    private final OfflineLayerRepo offlineLayerRepo;

    @Autowired
    public OfflineNetworkServiceImpl(OfflineNetworkRepo offlineNetworkRepo, OfflineLinkRepo offlineLinkRepo, OfflineNodeRepo offlineNodeRepo, OfflineLayerRepo offlineLayerRepo) {
        this.offlineNetworkRepo = offlineNetworkRepo;
        this.offlineLinkRepo = offlineLinkRepo;
        this.offlineNodeRepo = offlineNodeRepo;
        this.offlineLayerRepo = offlineLayerRepo;
    }

    @Override
    public OfflineNetwork init(Network network) {

        OfflineNetwork offlineNetwork = offlineNetworkRepo.save(
                OfflineNetwork.builder()
                        .name(network.getName())
                        .createDateTime(LocalDateTime.now())
                        .network(network)
                        .layers(new ArrayList<>())
                        .build());

        network.getLayers().forEach(layer -> {

            OfflineLayer offlineLayer = offlineLayerRepo.save(
                    OfflineLayer.builder()
                            .layer(layer)
                            .offlineNetwork(offlineNetwork)
                            .nodes(new ArrayList<>())
                            .build());
            offlineNetwork.addLayer(offlineLayer);

            layer.getNodes().forEach(node -> {
                OfflineNode offlineNode = offlineNodeRepo.save(
                        OfflineNode.builder()
                                .status(OfflineNodeStatus.IGNORE)
                                .value(null)
                                .node(node)
                                .offlineLayer(offlineLayer)
                                .links(new ArrayList<>())
                                .build());
                offlineLayer.addNode(offlineNode);

                node.getOutputLinks().forEach(link -> {
                    OfflineLink offlineLink = offlineLinkRepo.save(
                            OfflineLink.builder()
                                    .link(link)
                                    .offlineNode(offlineNode)
                                    .build());
                    offlineNode.addLink(offlineLink);
                });


            });

        });

        return offlineNetworkRepo.save(offlineNetwork);
    }

    @Override
    public OfflineNetwork reset(OfflineNetwork offlineNetwork) {
        offlineNetwork.getLayers().forEach(layer ->
                layer.getNodes().forEach(node -> {
                    node.setStatus(OfflineNodeStatus.IGNORE);
                    node.setValue(null);
                }));
        return offlineNetworkRepo.save(offlineNetwork);
    }

    @Override
    public OfflineNetwork setInput(OfflineNetwork offlineNetwork, List<Double> inputs) throws NetworkAccessBussExc {
        Iterator<OfflineLayer> iterator = offlineNetwork.getLayers().iterator();
        if(iterator.hasNext()){
            List<OfflineNode> nodes = iterator.next().getNodes();
            if(nodes.size() != inputs.size()){
                throw new NetworkAccessBussExc("Input size does not match the input layer neuron count");
            }
            nodes.forEach(node -> {
                        node.setValue(inputs.remove(0));
                        offlineNodeRepo.save(node);
                    });
        }
        return offlineNetworkRepo.getOne(offlineNetwork.getId());
    }

    @Override
    public boolean hasNext(OfflineNetwork network) {
        for(OfflineLayer offlineLayer: network.getLayers()){
            for(OfflineNode offlineNode: offlineLayer.getNodes()){
                if(offlineNode.getValue() == null){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public OfflineNetwork next(OfflineNetwork network) {
        boolean found = false;
        for(OfflineLayer offlineLayer: network.getLayers()){
            if(found){
                offlineLayer.getNodes().forEach(offlineNode -> {
                            offlineNode.setValue(offlineNode.getValue() + offlineNode.getNode().getBias());
                            offlineNodeRepo.save(offlineNode);
                        }
                );
                break;
            }
            for(OfflineNode offlineNode: offlineLayer.getNodes()){

                if(offlineNode.getLinks().stream()
                        .anyMatch(offlineLink -> offlineLink.getLink()
                                .getNode()
                                .getOfflineNodes()
                                .get(0)
                                .getValue() == null)){
                    found = true;
                    break;
                }
            }
            if(found) {
                offlineLayer.getNodes().forEach(offlineNode -> {
                    offlineNode.getLinks().forEach(offlineLink -> {
                        OfflineNode target = offlineLink.getLink().getNode().getOfflineNodes().get(0);
                        double weight = offlineLink.getLink().getWeight();
                        //double bias = target.getNode().getBias();
                        target.setValue(target.getValue() + offlineNode.getValue() * weight);
                    });
                });
            }
        }
        return offlineNetworkRepo.getOne(network.getId());
    }

    @Override
    public Optional<OfflineNetwork> findForNetwork(Network network) {
        return offlineNetworkRepo.findAll().stream()
                .filter(offlineNetwork -> offlineNetwork.getNetwork().getId().equals(network.getId()))
                .findFirst();
    }

    @Override
    public List<OfflineNetwork> getAll() {
        return offlineNetworkRepo.findAll();
    }

    @Override
    public Optional<OfflineNetwork> findById(long id) {
        return offlineNetworkRepo.findById(id);
    }
}
