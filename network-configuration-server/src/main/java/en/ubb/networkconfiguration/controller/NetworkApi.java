package en.ubb.networkconfiguration.controller;


import en.ubb.networkconfiguration.domain.enums.LayerType;
import en.ubb.networkconfiguration.domain.network.runtime.Network;
import en.ubb.networkconfiguration.domain.network.runtime.Node;
import en.ubb.networkconfiguration.domain.network.setup.LayerInitializer;
import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.dto.NetworkDto;
import en.ubb.networkconfiguration.dto.setup.NetworkInitDto;
import en.ubb.networkconfiguration.dto.util.DtoMapper;
import en.ubb.networkconfiguration.service.NetworkService;
import en.ubb.networkconfiguration.validation.exception.boundary.BoundaryException;
import en.ubb.networkconfiguration.validation.exception.boundary.NetworkNotFoundException;
import en.ubb.networkconfiguration.validation.validator.NetworkDtoValidator;
import org.nd4j.linalg.activations.Activation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("network-run")
public class NetworkApi {

    @Autowired
    private NetworkDtoValidator networkDtoValidator;

    private final NetworkService networkService;

    @Autowired
    public NetworkApi(NetworkService networkService) {
        this.networkService = networkService;
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(networkDtoValidator);
    }


    @GetMapping(value = "/networks", produces = "application/json")
    public List<NetworkDto> getAll() {
        return this.networkService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/networks/{id}", produces = "application/json")
    public NetworkDto getById(@NotNull @PathVariable Long id) {
        return this.networkService.getById(id).map(DtoMapper::toDto)
                .orElseThrow(() -> new NetworkNotFoundException(id));
    }

    @DeleteMapping(value = "/networks/{id}")
    public void deleteById(@NotNull @PathVariable Long id) {
        if (!this.networkService.deleteById(id)) {
            throw new NetworkNotFoundException(id);
        }
    }

    @PostMapping("/networks/init")
    public void create(@Validated @RequestBody NetworkInitDto dto, BindingResult result, SessionStatus status) {
        try {
            if (result.hasErrors()) {
                return;
            }
            this.networkService.create(DtoMapper.fromDto(dto));
            status.setComplete();
        } catch (IOException ex) {
            throw new BoundaryException("Unexpected error encountered while saving the network.", ex);
        }
    }

    @PutMapping("networks/{id}")
    public void update(
            @Validated @RequestBody NetworkDto dto,
            @NotNull @PathVariable Long id,
            BindingResult result,
            SessionStatus status) {

        if (result.hasErrors()) {
            return;
        }
        dto.setId(id);
        this.networkService.update(DtoMapper.fromDto(dto));
        status.setComplete();
    }

    @GetMapping("networks/run/{id}")
    public String run(@NotNull @PathVariable Long id) throws IOException, InterruptedException {
        Network network = this.networkService.getById(id)
                .orElseThrow(() -> new NetworkNotFoundException(id));

        this.networkService.run(network);
        return "success"; //TODO RETURN RESULT / IMPROVEMENT ETC.
    }

    @GetMapping("networks/save-progress/{id}")
    public String saveProgress(@NotNull @PathVariable Long id) throws IOException {
        Network network = this.networkService.getById(id)
                .orElseThrow(() -> new NetworkNotFoundException(id));

        this.networkService.saveProgress(network);
        return "success"; //TODO RETURN RESULT / IMPROVEMENT ETC.
    }


    @GetMapping(value = "/testing", produces = "application/json")
    public String test() throws Exception {

        Network config = new Network();
        config.setName("test");
        config.setSeed(1234);
        config.setLearningRate(0.01);
        config.setBatchSize(40);
        config.setNEpochs(10);
        config.setNInputs(2);
        config.setNOutputs(2);

        NetworkInitializer networkInitializer = new NetworkInitializer().toBuilder()
                .name("test")
                .seed(1234)
                .learningRate(0.01)
                .batchSize(40)
                .nEpochs(10)
                .nInputs(2)
                .nOutputs(2)
                .build();

        networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.INPUT)
                .nInputs(0)
                .nNodes(2)
                .nOutputs(20) // outputs == nodes on next layer
                .build());

        /*networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.DENSE)
                .nInputs(20)
                .nNodes(20)
                .nOutputs(20)
                .activation(Activation.RELU)
                .build());
                */

        networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.DENSE)
                .nInputs(2)
                .nNodes(20)
                .nOutputs(2)
                .activation(Activation.RELU)
                .build());

        networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.OUTPUT)
                .nInputs(20)
                .nNodes(2)
                .nOutputs(0)
                .activation(Activation.SOFTMAX)
                .build());


        config = networkService.create(networkInitializer);
        config = networkService.run(config);
        config = networkService.saveProgress(config);

        config = networkService.loadNetwork(config);

        networkService.run(config);

        config = networkService.saveProgress(config);

        Node node = config.getLayers().get(0).getNodes().get(0);
        Node newNode = node.toBuilder().bias(0.5).outputLinks(
                node.getOutputLinks().stream()
                        .map(link -> link.toBuilder()
                                .weight(link.getWeight() * 100)
                                .build())
                        .collect(Collectors.toList())
        ).build();
        newNode.getOutputLinks().get(0).setWeight(0.999);
        networkService.updateNode(newNode);

        Network network = networkService.getById(config.getId()).get();
        System.out.println("aa");
        networkService.run(config);


        //TODO SAVE NETWORK AFTER RUN, REFACTOR TO USE ONLY MY DOMAIN CLASS


        return "";
    }

}
