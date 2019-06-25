package en.ubb.networkconfiguration.boundary.rest;

import en.ubb.networkconfiguration.boundary.dto.file.DataFileDto;
import en.ubb.networkconfiguration.boundary.dto.file.FileLinkDto;
import en.ubb.networkconfiguration.boundary.dto.file.RunConfigDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.boundary.validation.exception.BoundaryException;
import en.ubb.networkconfiguration.boundary.validation.exception.FileAccessException;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.boundary.validation.validator.DataFileDtoValidator;
import en.ubb.networkconfiguration.business.service.FileService;
import en.ubb.networkconfiguration.business.validation.exception.FileAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("file-management")
public class DataFileApi {


    private final DataFileDtoValidator dataFileDtoValidator;

    private final FileService fileService;

    @Autowired
    public DataFileApi(FileService fileService, DataFileDtoValidator dataFileDtoValidator) {
        this.fileService = fileService;
        this.dataFileDtoValidator = dataFileDtoValidator;
    }

    @GetMapping
    public List<DataFileDto> getAll() {
        return this.fileService.getAll().stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/run-configs/{networkId}")
    public List<RunConfigDto> getAllPossibleRunningConfigurations(@PathVariable long networkId) throws NotFoundException {
        try {
            List<DataFile> trainFiles = this.fileService.getTrainFiles(networkId);
            List<DataFile> testFiles = this.fileService.getTestFiles(networkId);
            List<RunConfigDto> runConfigDtos = new ArrayList<>();
            trainFiles.forEach(trainFile -> {
                testFiles.forEach(testFile -> {
                    runConfigDtos.add(RunConfigDto.builder()
                            .networkId(networkId)
                            .trainFileId(trainFile.getId())
                            .trainFileName(trainFile.getName())
                            .testFileId(testFile.getId())
                            .testFileName(testFile.getName())
                            .build());
                });
            });
            return runConfigDtos;
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
    }

    @GetMapping("/{fileName}")
    public DataFileDto getByName(@PathVariable String fileName) throws NotFoundException {
        return DtoMapper.toDto(this.fileService.findFile(fileName)
                .orElseThrow(() -> new NotFoundException("File with name " + fileName + " not found")));
    }


    @PostMapping
    public void addFile(@RequestParam("file") MultipartFile file, @RequestParam String nLabels) throws FileAccessException {
        try {
            this.fileService.addFile(file, Integer.parseInt(nLabels));
        } catch (FileAccessBussExc ex) {
            throw new FileAccessException(ex);
        }
    }

    @DeleteMapping("/{fileName}")
    public void removeFile(@PathVariable String fileName) throws NotFoundException {
        try {
            this.fileService.removeFile(fileName);
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
    }


    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws NotFoundException {
        DataFile dataFile = this.fileService.findFile(fileName)
                .orElseThrow(() -> new NotFoundException("File with name " + fileName + " not found"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dataFile.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dataFile.getName() + "\"")
                .body(new ByteArrayResource(dataFile.getData()));
    }

    @GetMapping("/links/{fileName}")
    public List<FileLinkDto> getLinks(@PathVariable String fileName) throws NotFoundException {
        DataFile dataFile = this.fileService.findFile(fileName)
                .orElseThrow(() -> new NotFoundException("File with name " + fileName + " not found"));
        return dataFile.getNetworks().stream()
                .map(networkFile -> FileLinkDto.builder()
                        .fileType(networkFile.getType())
                        .fileName(dataFile.getName())
                        .networkName(networkFile.getNetwork().getName())
                        .build()
                ).collect(Collectors.toList());

    }

    @PostMapping("/links/train/{fileName}")
    public List<FileLinkDto> setTrainLinks(@PathVariable String fileName, @RequestBody List<String> networkNames) throws BoundaryException {
        try {
            this.fileService.saveLinks(fileName, networkNames, FileType.TRAIN);
            return networkNames.stream()
                    .map(networkName -> FileLinkDto.builder()
                            .fileName(fileName)
                            .networkName(networkName)
                            .fileType(FileType.TRAIN)
                            .build())
                    .collect(Collectors.toList());

        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }

    @PostMapping("/links/test/{fileName}")
    public List<FileLinkDto> setTestLinks(@PathVariable String fileName, @RequestBody List<String> networkNames) throws BoundaryException {
        try {
            this.fileService.saveLinks(fileName, networkNames, FileType.TEST);
            return networkNames.stream()
                    .map(networkName -> FileLinkDto.builder()
                            .fileName(fileName)
                            .networkName(networkName)
                            .fileType(FileType.TEST)
                            .build())
                    .collect(Collectors.toList());

        } catch (NotFoundBussExc notFoundBussExc) {
            throw new NotFoundException(notFoundBussExc);
        }
    }


}
