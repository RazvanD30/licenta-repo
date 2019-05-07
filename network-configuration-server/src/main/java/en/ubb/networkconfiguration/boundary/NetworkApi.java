package en.ubb.networkconfiguration.boundary;


import en.ubb.networkconfiguration.boundary.dto.runtime.NetworkDto;
import en.ubb.networkconfiguration.boundary.dto.runtime.RunConfigDto;
import en.ubb.networkconfiguration.boundary.dto.setup.NetworkInitDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.domain.enums.FileType;
import en.ubb.networkconfiguration.domain.enums.LayerType;
import en.ubb.networkconfiguration.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.domain.network.runtime.Network;
import en.ubb.networkconfiguration.domain.network.runtime.Node;
import en.ubb.networkconfiguration.domain.network.setup.LayerInitializer;
import en.ubb.networkconfiguration.domain.network.setup.NetworkInitializer;
import en.ubb.networkconfiguration.service.FileService;
import en.ubb.networkconfiguration.service.NetworkService;
import en.ubb.networkconfiguration.validation.exception.boundary.FileAccessException;
import en.ubb.networkconfiguration.validation.exception.boundary.NetworkAccessException;
import en.ubb.networkconfiguration.validation.exception.boundary.NotFoundException;
import en.ubb.networkconfiguration.validation.exception.business.FileAccessBussExc;
import en.ubb.networkconfiguration.validation.exception.business.NetworkAccessBussExc;
import en.ubb.networkconfiguration.validation.exception.business.NotFoundBussExc;
import en.ubb.networkconfiguration.validation.validator.NetworkDtoValidator;
import en.ubb.networkconfiguration.validation.validator.RunConfigDtoValidator;
import org.nd4j.linalg.activations.Activation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("network-management")
public class NetworkApi {

    private final NetworkDtoValidator networkDtoValidator;

    private final RunConfigDtoValidator runConfigDtoValidator;

    private final FileService fileService;

    private final NetworkService networkService;

    @Autowired
    public NetworkApi(NetworkService networkService, NetworkDtoValidator networkDtoValidator, FileService fileService, RunConfigDtoValidator runConfigDtoValidator) {
        this.networkService = networkService;
        this.networkDtoValidator = networkDtoValidator;
        this.fileService = fileService;
        this.runConfigDtoValidator = runConfigDtoValidator;
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(networkDtoValidator, runConfigDtoValidator);
    }


    @GetMapping(value = "/networks", produces = "application/json")
    public List<NetworkDto> getAll() {
        return this.networkService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/networks/{id}", produces = "application/json")
    public NetworkDto getById(@NotNull @PathVariable Long id) throws NotFoundException {
        return this.networkService.findById(id).map(DtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Network not found."));
    }

    @DeleteMapping(value = "/networks/{id}")
    public void deleteById(@NotNull @PathVariable Long id) throws NotFoundException {
        if (!this.networkService.deleteById(id)) {
            throw new NotFoundException("Network not found.");
        }
    }

    @PutMapping("networks")
    public void update(@Validated @RequestBody NetworkDto dto, BindingResult result, SessionStatus status) throws NotFoundException {

        if (result.hasErrors()) {
            return;
        }
        try {
            this.networkService.update(DtoMapper.fromDto(dto));
            status.setComplete();
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
    }

    @GetMapping("networks/run/{id}")
    public String run(@NotNull @PathVariable Long id,
                      @Validated @NotNull @RequestBody RunConfigDto runConfig) throws NotFoundException, FileAccessException {

        Network network = this.networkService.findById(id)
                .orElseThrow(() -> new NotFoundException("Network not found."));
        try {

            DataFile trainFile = this.fileService.findFile(runConfig.getTrainFileId())
                    .orElseThrow(() -> new NotFoundException("Train file not found."));

            DataFile testFile = this.fileService.findFile(runConfig.getTestFileId())
                    .orElseThrow(() -> new NotFoundException("Test file not found."));

            this.networkService.run(network, trainFile, testFile);
        } catch (FileAccessBussExc ex) {
            throw new FileAccessException("Could not access the given files.");
        }
        return "success"; //TODO RETURN RESULT / IMPROVEMENT ETC.
    }

    @GetMapping("networks/save-progress/{id}")
    public String saveProgress(@NotNull @PathVariable Long id) throws NotFoundException, NetworkAccessException {

        Network network = this.networkService.findById(id)
                .orElseThrow(() -> new NotFoundException("Network not found."));
        try {
            this.networkService.saveProgress(network);
        } catch (NetworkAccessBussExc ex) {
            throw new NetworkAccessException("Could not access the given network.");
        }
        return "success"; //TODO RETURN RESULT / IMPROVEMENT ETC.
    }



    @GetMapping(value = "/testing/create", produces = "application/json")
    public NetworkDto testCreate() throws NetworkAccessBussExc, NotFoundBussExc {
        Network network = new Network();
        network.setName("test");
        network.setSeed(1234);
        network.setLearningRate(0.01);
        network.setBatchSize(40);
        network.setNEpochs(10);
        network.setNInputs(2);
        network.setNOutputs(2);

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

        network = networkService.create(networkInitializer);

        String trainClassPath = "classification/linear_data_train.csv";
        String testClassPath = "classification/linear_data_eval.csv";
        int trainLabels = 2;
        int testLabels = 2;

        fileService.addFile(network.getId(),trainClassPath,trainLabels,FileType.TRAIN);
        fileService.addFile(network.getId(),testClassPath,testLabels,FileType.TEST);

        return DtoMapper.toDto(network);
    }


    @GetMapping(value = "/testing", produces = "application/json")
    public NetworkDto test() throws Exception {

        String trainClassPath = "classification/linear_data_train.csv";
        String testClassPath = "classification/linear_data_eval.csv";
        DataFile trainFile = this.fileService.findFile(trainClassPath).get();
        DataFile testFile = this.fileService.findFile(testClassPath).get();

        Network network = networkService.getAll().get(0);

        network = networkService.run(network, trainFile, testFile);
        network = networkService.saveProgress(network);
        /*
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

        Network network = networkService.findById(config.getId()).get();
        System.out.println("aa");
        networkService.run(config);
*/

        //TODO SAVE NETWORK AFTER RUN, REFACTOR TO USE ONLY MY DOMAIN CLASS


        return DtoMapper.toDto(network);
    }

}
