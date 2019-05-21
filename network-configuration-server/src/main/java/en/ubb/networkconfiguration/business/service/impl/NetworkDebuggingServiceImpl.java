package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.boundary.dto.debugging.DebuggingNode;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.business.service.NetworkDebuggingService;

import java.util.ArrayList;
import java.util.List;

public class NetworkDebuggingServiceImpl implements NetworkDebuggingService {

    @Override
    public List<DebuggingNode> initialize(Network network) {
        List<DebuggingNode> inputNodes = new ArrayList<>();
/*
        network.getLayers().get(0).getNodes().forEach(inputNode -> {
            DebuggingNode debuggingNode = new DebuggingNode();
            debuggingNode.setBias(0);
            debuggingNode.setStatus(NodeStatus.INPUT);
            Node currentNode = inputNode;
            while(!currentNode.getOutputLinks().isEmpty()){
                currentNode.getOutputLinks().forEach(link -> {
                    currentNode.setOutputLinks();


                });



            }

            inputNode.getOutputLinks().forEach(link -> {

            });

        });

        while()


        network.getLayers().forEach(layer -> {
            if(layer.getType().equals(LayerType.INPUT))

            layer.getNodes().forEach(node -> {
                node.getOutputLinks().

            });
        });
        */
        return inputNodes;
    }

    @Override
    public void initializeData(DebuggingNode node) {

    }

    @Override
    public List<DebuggingNode> next(DebuggingNode node) {
        return null;
    }
}
