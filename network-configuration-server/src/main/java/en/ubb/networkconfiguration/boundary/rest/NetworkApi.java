package en.ubb.networkconfiguration.boundary.rest;


import en.ubb.networkconfiguration.boundary.dto.network.runtime.NetworkDto;
import en.ubb.networkconfiguration.boundary.dto.network.runtime.RunConfigDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.boundary.validation.exception.FileAccessException;
import en.ubb.networkconfiguration.boundary.validation.exception.NetworkAccessException;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.boundary.validation.validator.NetworkDtoValidator;
import en.ubb.networkconfiguration.boundary.validation.validator.RunConfigDtoValidator;
import en.ubb.networkconfiguration.business.service.BranchService;
import en.ubb.networkconfiguration.business.service.FileService;
import en.ubb.networkconfiguration.business.service.NetworkService;
import en.ubb.networkconfiguration.business.service.UserService;
import en.ubb.networkconfiguration.business.validation.exception.FileAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.enums.BranchType;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import en.ubb.networkconfiguration.persistence.domain.network.enums.LayerType;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.setup.LayerInitializer;
import en.ubb.networkconfiguration.persistence.domain.network.setup.NetworkInitializer;
import org.nd4j.linalg.activations.Activation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("network-configuration")
public class NetworkApi {

    private final NetworkDtoValidator networkDtoValidator;

    private final RunConfigDtoValidator runConfigDtoValidator;

    private final FileService fileService;

    private final NetworkService networkService;

    private final BranchService branchService;

    private final UserService userService;

    @Autowired
    public NetworkApi(NetworkService networkService, NetworkDtoValidator networkDtoValidator, FileService fileService, RunConfigDtoValidator runConfigDtoValidator, BranchService branchService, UserService userService) {
        this.networkService = networkService;
        this.networkDtoValidator = networkDtoValidator;
        this.fileService = fileService;
        this.runConfigDtoValidator = runConfigDtoValidator;
        this.branchService = branchService;
        this.userService = userService;
    }


    @InitBinder("networkDto")
    protected void initNetworkBinder(WebDataBinder binder) {
        binder.addValidators(networkDtoValidator);
    }

    @InitBinder("runConfigDto")
    protected void initRunConfigBinder(WebDataBinder binder) {
        binder.addValidators(runConfigDtoValidator);
    }


    @GetMapping(value = "/runtime", produces = "application/json")
    public List<NetworkDto> getAll() {
        return this.networkService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/runtime/{id}", produces = "application/json")
    public NetworkDto getById(@NotNull @PathVariable Long id) throws NotFoundException {
        return this.networkService.findById(id).map(DtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Network not found."));
    }

    @DeleteMapping(value = "/runtime/{id}")
    public void deleteById(@NotNull @PathVariable Long id) throws NotFoundException {
        if (!this.networkService.deleteById(id)) {
            throw new NotFoundException("Network not found.");
        }
    }

    @PutMapping("/runtime")
    public void update(@Validated @RequestBody NetworkDto networkDto, BindingResult result, SessionStatus status) throws NotFoundException {

        if (result.hasErrors()) {
            return;
        }
        try {
            this.networkService.update(DtoMapper.fromDto(networkDto));
            status.setComplete();
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
    }

    @GetMapping("runtime/run/{id}")
    public String run(@NotNull @PathVariable Long id,
                      @Validated @NotNull @RequestBody RunConfigDto runConfigDto) throws NotFoundException, FileAccessException {

        Network network = this.networkService.findById(id)
                .orElseThrow(() -> new NotFoundException("Network not found."));
        try {

            DataFile trainFile = this.fileService.findFile(runConfigDto.getTrainFileId())
                    .orElseThrow(() -> new NotFoundException("Train file not found."));

            DataFile testFile = this.fileService.findFile(runConfigDto.getTestFileId())
                    .orElseThrow(() -> new NotFoundException("Test file not found."));

            this.networkService.run(network, trainFile, testFile);
        } catch (FileAccessBussExc ex) {
            throw new FileAccessException("Could not access the given files.");
        }
        return "success"; //TODO RETURN RESULT / IMPROVEMENT ETC.
    }

    @GetMapping("runtime/save-progress/{id}")
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





    //TODO REMOVE BELOW

    @GetMapping(value = "/testing/create", produces = "application/json")
    public NetworkDto testCreate() throws NetworkAccessBussExc, NotFoundBussExc {

        NetworkInitializer networkInitializer = new NetworkInitializer().toBuilder()
                .name("test" + new Random().nextInt())
                .seed(1234)
                .learningRate(0.01)
                .batchSize(5)
                .nEpochs(10)
                .nInputs(8)
                .nOutputs(1)
                .build();

        networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.INPUT)
                .nInputs(0)
                .nNodes(2)
                .nOutputs(20) // outputs == nodes on next offlineLayer
                .build());

        networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.DENSE)
                .nInputs(2)
                .nNodes(20)
                .nOutputs(20)
                .activation(Activation.RELU)
                .build());

        networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.DENSE)
                .nInputs(20)
                .nNodes(20)
                .nOutputs(20)
                .activation(Activation.RELU)
                .build());

        networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.DENSE)
                .nInputs(20)
                .nNodes(20)
                .nOutputs(20)
                .activation(Activation.RELU)
                .build());

        networkInitializer.addLayer(new LayerInitializer().toBuilder()
                .type(LayerType.DENSE)
                .nInputs(20)
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

        NetworkBranch branch = NetworkBranch.builder()
                .name("main-branch")
                .type(BranchType.TEST)
                .networks(new ArrayList<>())
                .owner(userService.getAll().get(0))
                .contributors(new ArrayList<>())
                .build();

        branch = branchService.create(branch, null);

        Network network = networkService.create(branch, networkInitializer);

        String trainClassPath = "classification/train.csv";
        String testClassPath = "classification/eval.csv";
        //        String trainClassPath = "classification/linear_data_train.csv";
        //        String testClassPath = "classification/linear_data_eval.csv";
        int trainLabels = 2;
        int testLabels = 2;

        fileService.addFile(network.getId(), trainClassPath, trainLabels, FileType.TRAIN);
        fileService.addFile(network.getId(), testClassPath, testLabels, FileType.TEST);

        return DtoMapper.toDto(network);
    }


    @GetMapping(value = "/testing", produces = "application/json")
    public NetworkDto test() throws Exception {

        String trainClassPath = "classification/train.csv";
        String testClassPath = "classification/eval.csv";
        DataFile trainFile = this.fileService.findFile(trainClassPath).get();
        DataFile testFile = this.fileService.findFile(testClassPath).get();

        List<Network> all = networkService.getAll();
        Network network = all.get(all.size() - 1);

        network = networkService.run(network, trainFile, testFile);
        /*
        config = networkService.loadNetwork(config);

        networkService.run(config);

        config = networkService.saveProgress(config);

        Node offlineNode = config.getLayers().get(0).getNodes().get(0);
        Node newNode = offlineNode.toBuilder().bias(0.5).outputLinks(
                offlineNode.getOutputLinks().stream()
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
