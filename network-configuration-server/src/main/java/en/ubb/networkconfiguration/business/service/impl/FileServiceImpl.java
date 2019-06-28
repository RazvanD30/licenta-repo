package en.ubb.networkconfiguration.business.service.impl;

import com.sun.javafx.binding.StringFormatter;
import en.ubb.networkconfiguration.business.service.FileService;
import en.ubb.networkconfiguration.business.validation.exception.FileAccessBussExc;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.dao.DataFileRepo;
import en.ubb.networkconfiguration.persistence.dao.NetworkRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.DataFileSpec;
import en.ubb.networkconfiguration.persistence.dao.specification.NetworkSpec;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final DataFileRepo dataFileRepo;

    private final NetworkRepo networkRepo;

    @Autowired
    public FileServiceImpl(DataFileRepo dataFileRepo, NetworkRepo networkRepo) {
        this.dataFileRepo = dataFileRepo;
        this.networkRepo = networkRepo;
    }


    @Override
    public DataFile addFile(@NotNull MultipartFile file, int nLabels) throws FileAccessBussExc {

        if (file.getOriginalFilename() == null) {
            throw new FileAccessBussExc("Filename not found");
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (filename.contains("..")) {
            throw new FileAccessBussExc("Filename contains invalid path sequence: " + filename);
        }

        DataFile dataFile;
        try {
            dataFile = DataFile.builder()
                    .name(filename)
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .nLabels(nLabels)
                    .networks(new ArrayList<>())
                    .build();
        } catch (IOException e) {
            throw new FileAccessBussExc("Error encountered while accessing the file contents");
        }
        return dataFileRepo.save(dataFile);
    }


    @Transactional
    @Override
    public void saveLinks(String fileName, List<String> networkNames, FileType type) throws NotFoundBussExc {

        DataFile dataFile = this.findFile(fileName)
                .orElseThrow(() -> new NotFoundBussExc("File not found"));

        List<NetworkFile> networks = new ArrayList<>(dataFile.getNetworks());

        networks.forEach(networkFile -> {
            if (networkFile.getType().equals(type)) {
                Network network = networkFile.getNetwork();
                if (network != null) {
                    network.removeFile(dataFile, type);
                    this.networkRepo.save(network);
                }
            }
        });
        
        for (String networkName : networkNames) {
            Network network = this.networkRepo.findOne(Specification.where(NetworkSpec.hasName(networkName)))
                    .orElseThrow(() -> new NotFoundBussExc("Network with name " + networkName + " not found"));
            if(network.getNInputs() != dataFile.getNLabels()){
                throw new ValidationException("Incompatible files: Network " + network.getName() + " has "
                        + network.getNInputs() + " inputs and the file " + dataFile.getName()
                        + " has " + dataFile.getNLabels() + " labels");
            }
            network.addFile(dataFile, type);
            this.networkRepo.save(network);
        }
    }

    @Override
    public DataFile removeFile(String fileName) throws NotFoundBussExc {
        DataFile dataFile = this.findFile(fileName)
                .orElseThrow(() -> new NotFoundBussExc("File with name " + fileName + " not found "));

        this.dataFileRepo.deleteById(dataFile.getId());
        return dataFile;
    }

    @Override
    public DataFile removeFile(long dataFileId) throws NotFoundBussExc {
        Optional<DataFile> dataFileOpt = this.dataFileRepo.findById(dataFileId);
        if (!dataFileOpt.isPresent()) {
            throw new NotFoundBussExc("Data file with id " + dataFileId + " not found");
        }
        this.dataFileRepo.deleteById(dataFileId);
        return dataFileOpt.get();
    }

    @Override
    public Optional<DataFile> findFile(String fileName) {
        return this.dataFileRepo.findOne(Specification.where(DataFileSpec.hasName(fileName)));
    }

    @Override
    public Optional<DataFile> findFile(long id) {
        return this.dataFileRepo.findById(id);
    }

    @Override
    public List<DataFile> getAll() {
        return this.dataFileRepo.findAll();
    }

    @Override
    public List<DataFile> getTrainFiles(long networkId) throws NotFoundBussExc {
        final Network persistedNetwork = this.networkRepo.findById(networkId)
                .orElseThrow(() -> new NotFoundBussExc("Network with id " + networkId + " not found"));

        return persistedNetwork.getFiles().stream()
                .filter(networkFile -> networkFile.getType().equals(FileType.TRAIN))
                .map(NetworkFile::getDataFile)
                .collect(Collectors.toList());
    }

    @Override
    public List<DataFile> getTestFiles(long networkId) throws NotFoundBussExc {
        final Network persistedNetwork = this.networkRepo.findById(networkId)
                .orElseThrow(() -> new NotFoundBussExc("Network with id " + networkId + " not found"));

        return persistedNetwork.getFiles().stream()
                .filter(networkFile -> networkFile.getType().equals(FileType.TEST))
                .map(NetworkFile::getDataFile)
                .collect(Collectors.toList());
    }
}
