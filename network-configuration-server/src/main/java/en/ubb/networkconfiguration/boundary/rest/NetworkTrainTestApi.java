package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.file.RunConfigDto;
import en.ubb.networkconfiguration.boundary.dto.network.traintest.NetworkEvalDto;
import en.ubb.networkconfiguration.boundary.validation.exception.FileAccessException;
import en.ubb.networkconfiguration.boundary.validation.exception.NetworkAccessException;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.business.service.FileService;
import en.ubb.networkconfiguration.business.service.NetworkService;
import en.ubb.networkconfiguration.business.validation.exception.FileAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NetworkAccessBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import org.antlr.runtime.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("network-management/train-test")
public class NetworkTrainTestApi {

    private final NetworkService networkService;

    private final FileService fileService;

    @Autowired
    public NetworkTrainTestApi(NetworkService networkService, FileService fileService) {
        this.networkService = networkService;
        this.fileService = fileService;
    }


    private boolean containsFile(Network network, DataFile dataFile, FileType fileType){
        return network.getFiles().stream()
                .anyMatch(networkFile -> networkFile.getType().equals(fileType)
                        && networkFile.getDataFile().getName().equals(dataFile.getName()));
    }


    @PostMapping("/run/{id}")
    public NetworkEvalDto run(@NotNull @PathVariable long id,
                      @Validated @NotNull @RequestBody RunConfigDto runConfigDto) throws NotFoundException, FileAccessException, NetworkAccessException {

        Network network = this.networkService.findById(id)
                .orElseThrow(() -> new NotFoundException("Network not found."));
        if(network.isTraining()){
            throw new NetworkAccessException("Network is currently training");
        }
        try {

            DataFile trainFile = this.fileService.findFile(runConfigDto.getTrainFileName())
                    .orElseThrow(() -> new NotFoundException("Train file not found."));

            DataFile testFile = this.fileService.findFile(runConfigDto.getTestFileName())
                    .orElseThrow(() -> new NotFoundException("Test file not found."));

            if(!containsFile(network,trainFile,FileType.TRAIN)){
                throw new NotFoundException("Train file was not linked to the network");
            }
            if(!containsFile(network,testFile,FileType.TEST)){
                throw new NotFoundException("Test file was not linked to the network");
            }

            return new NetworkEvalDto(this.networkService.run(network, trainFile, testFile));
        } catch (FileAccessBussExc ex) {
            throw new FileAccessException("Could not access the given files.");
        }
    }

    @PostMapping("/save-progress/{id}")
    public void saveProgress(@NotNull @PathVariable long id) throws NotFoundException, NetworkAccessException {

        Network network = this.networkService.findById(id)
                .orElseThrow(() -> new NotFoundException("Network not found."));
        try {
            this.networkService.saveProgress(network);
        } catch (NetworkAccessBussExc ex) {
            throw new NetworkAccessException("Could not access the given network.");
        }
    }

}
