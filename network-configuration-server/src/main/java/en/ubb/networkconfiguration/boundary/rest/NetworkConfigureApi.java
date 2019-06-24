package en.ubb.networkconfiguration.boundary.rest;


import en.ubb.networkconfiguration.boundary.dto.file.FileLinkDto;
import en.ubb.networkconfiguration.boundary.dto.network.runtime.NetworkDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.boundary.validation.exception.FileAccessException;
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
import en.ubb.networkconfiguration.persistence.domain.authentication.User;
import en.ubb.networkconfiguration.persistence.domain.branch.NetworkBranch;
import en.ubb.networkconfiguration.persistence.domain.network.enums.BranchType;
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
@RequestMapping("network-management/configure")
public class NetworkConfigureApi {

    private final NetworkDtoValidator networkDtoValidator;

    private final RunConfigDtoValidator runConfigDtoValidator;

    private final FileService fileService;

    private final NetworkService networkService;

    private final BranchService branchService;

    private final UserService userService;

    @Autowired
    public NetworkConfigureApi(NetworkService networkService, NetworkDtoValidator networkDtoValidator, FileService fileService, RunConfigDtoValidator runConfigDtoValidator, BranchService branchService, UserService userService) {
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


    @GetMapping
    public List<NetworkDto> getAll() {
        return this.networkService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "withUser/{username}")
    public List<NetworkDto> getAllForUser(@PathVariable String username) throws NotFoundException {
        User user = this.userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        try {
            List<NetworkDto> collect = this.networkService.getAllForBranchID(user.getCurrentBranch().getId()).stream()
                    .map(DtoMapper::toDto)
                    .collect(Collectors.toList());
            System.out.println(collect);
            return collect;
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }

    @GetMapping(value = "/{id}")
    public NetworkDto getById(@NotNull @PathVariable long id) throws NotFoundException {
        return this.networkService.findById(id).map(DtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Network with id " + id + "not found."));
    }

    @GetMapping(value = "/withName/{name}")
    public NetworkDto getByName(@NotNull @PathVariable String name) throws NotFoundException {
        return this.networkService.findByName(name).map(DtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Network with name " + name + " not found"));
    }

    @DeleteMapping(value = "/{id}")
    public NetworkDto deleteById(@NotNull @PathVariable long id) throws NotFoundException {
        try {
            return DtoMapper.toDto(this.networkService.deleteById(id));
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }

    @PutMapping
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

    @PostMapping("/link")
    public void linkFile(@RequestBody FileLinkDto dto) throws NotFoundException, FileAccessException {
        try{
            this.fileService.linkFile(dto.getNetworkId(),dto.getFileName(),dto.getFileType());
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        } catch (FileAccessBussExc fileAccessBussExc) {
            throw new FileAccessException(fileAccessBussExc);
        }
    }

    @PostMapping("/unlink")
    public void unlinkFile(@RequestBody FileLinkDto dto) throws NotFoundException {
        try{
            this.fileService.unlinkFile(dto.getNetworkId(),dto.getFileName());
        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
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
                .nOutputs(20) // outputs == nodes on next virtualLayer
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

        Network network = networkService.create(branch.getId(), networkInitializer);

        String trainClassPath = "classification/train.csv";
        String testClassPath = "classification/eval.csv";
        //        String trainClassPath = "classification/linear_data_train.csv";
        //        String testClassPath = "classification/linear_data_eval.csv";
        int trainLabels = 2;
        int testLabels = 2;

        //fileService.linkFile(network.getId(), trainClassPath, trainLabels, FileType.TRAIN);
        //fileService.linkFile(network.getId(), testClassPath, testLabels, FileType.TEST);

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

        //network = networkService.run(network, trainFile, testFile);
        /*
        config = networkService.loadNetwork(config);

        networkService.run(config);

        config = networkService.saveProgress(config);

        Node targetNode = config.getLayers().get(0).getNodes().get(0);
        Node newNode = targetNode.toBuilder().bias(0.5).outputLinks(
                targetNode.getOutputLinks().stream()
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
