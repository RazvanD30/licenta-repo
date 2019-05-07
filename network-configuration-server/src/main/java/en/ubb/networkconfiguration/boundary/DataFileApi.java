package en.ubb.networkconfiguration.boundary;

import en.ubb.networkconfiguration.boundary.dto.setup.DataFileDto;
import en.ubb.networkconfiguration.boundary.util.DtoMapper;
import en.ubb.networkconfiguration.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.service.FileService;
import en.ubb.networkconfiguration.validation.exception.boundary.NotFoundException;
import en.ubb.networkconfiguration.validation.exception.business.NotFoundBussExc;
import en.ubb.networkconfiguration.validation.validator.DataFileDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("network-management")
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
            this.fileService.removeFile(dto.getNetworkId(), dto.getClassPath());
        } catch (NotFoundBussExc ex) {
            throw new NotFoundException(ex);
        }
    }


}
