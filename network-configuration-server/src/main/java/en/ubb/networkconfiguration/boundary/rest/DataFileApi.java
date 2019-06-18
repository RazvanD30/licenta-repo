package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.network.runtime.RunConfigDto;
import en.ubb.networkconfiguration.boundary.dto.network.setup.DataFileDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.boundary.validation.validator.DataFileDtoValidator;
import en.ubb.networkconfiguration.business.service.FileService;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("network-management")
@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
public class DataFileApi {


    private final DataFileDtoValidator dataFileDtoValidator;

    private final FileService fileService;

    @Autowired
    public DataFileApi(FileService fileService, DataFileDtoValidator dataFileDtoValidator) {
        this.fileService = fileService;
        this.dataFileDtoValidator = dataFileDtoValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(dataFileDtoValidator);
    }


    @GetMapping(value = "/files", produces = "application/json")
    public List<DataFileDto> getAll() {
        List<DataFileDto> dtos = new ArrayList<>();
        List<DataFile> dataFiles = this.fileService.getAll();
        dataFiles.forEach(dataFile -> {
            dtos.addAll(DtoMapper.toDtos(dataFile));
        });
        return dtos;
    }

    @PostMapping("/files")
    public void addFile(@Validated @RequestBody DataFileDto dto) throws NotFoundException {
        try {
            this.fileService.addFile(dto.getNetworkId(), dto.getClassPath(), dto.getNLabels(), dto.getType());
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
    }

    @DeleteMapping("/files")
    public void removeFile(@Validated @RequestBody DataFileDto dto) throws NotFoundException {
        try {
            this.fileService.unlinkFile(dto.getNetworkId(), dto.getClassPath());
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
    }

    public List<RunConfigDto> getAllPossibleRunningConfigurations(long networkId) throws NotFoundException {
        try {
            List<DataFile> trainFiles = this.fileService.getTrainFiles(networkId);
            List<DataFile> testFiles = this.fileService.getTestFiles(networkId);
            List<RunConfigDto> runConfigDtos = new ArrayList<>();
            trainFiles.forEach(trainFile -> {
                testFiles.forEach(testFile -> {
                    runConfigDtos.add(RunConfigDto.builder()
                            .networkId(networkId)
                            .trainFileId(trainFile.getId())
                            .testFileId(testFile.getId())
                            .build());
                });
            });
            return runConfigDtos;
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
    }


}
