package en.ubb.networkconfiguration.business.service.impl;

import en.ubb.networkconfiguration.business.service.FileService;
import en.ubb.networkconfiguration.business.validation.exception.NotFoundBussExc;
import en.ubb.networkconfiguration.persistence.dao.DataFileRepo;
import en.ubb.networkconfiguration.persistence.dao.NetworkRepo;
import en.ubb.networkconfiguration.persistence.dao.specification.DataFileSpec;
import en.ubb.networkconfiguration.persistence.domain.network.enums.FileType;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.DataFile;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.Network;
import en.ubb.networkconfiguration.persistence.domain.network.runtime.NetworkFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    public DataFile addFile(String classPath, int nLabels) {
        DataFile dataFile = DataFile.builder()
                .classPath(classPath)
                .nLabels(nLabels)
                .networks(new ArrayList<>())
                .build();
        return dataFileRepo.save(dataFile);
    }

    @Override
    public Network addFile(long networkID, String classPath, int nLabels, FileType fileType) throws NotFoundBussExc {
        return this.networkRepo.findById(networkID).map(persistedNetwork ->
                this.findFile(classPath)
                        .map(dataFile -> {
                            persistedNetwork.addFile(dataFile, fileType);
                            return persistedNetwork;
                        })
                        .orElseGet(() -> {
                            DataFile dataFile = DataFile.builder()
                                    .classPath(classPath)
                                    .nLabels(nLabels)
                                    .networks(new ArrayList<>())
                                    .build();
                            DataFile persistedFile = dataFileRepo.save(dataFile);

                            persistedNetwork.addFile(persistedFile, fileType);
                            this.networkRepo.save(persistedNetwork);
                            return persistedNetwork;
                        })
        ).orElseThrow(() -> new NotFoundBussExc("Network with id " + networkID + " not found"));
    }

    @Override
    public Network unlinkFile(long networkID, String classPath) throws NotFoundBussExc {

        final Network persistedNetwork = this.networkRepo.findById(networkID)
                .orElseThrow(() -> new NotFoundBussExc("Network with id " + networkID + " not found"));

        final DataFile persistedDataFile = this.findFile(classPath)
                .orElseThrow(() -> new NotFoundBussExc("File with classpath " + classPath + " not found"));

        persistedNetwork.removeFile(persistedDataFile);
        return persistedNetwork;
    }

    @Override
    public Network linkFile(long networkId, long dataFileId, FileType fileType) throws NotFoundBussExc {

        DataFile persistedDataFile = this.findFile(dataFileId)
                .orElseThrow(() -> new NotFoundBussExc("Datafile with id " + dataFileId + " not found"));

        return this.networkRepo.findById(networkId).map(persistedNetwork -> {
            persistedNetwork.addFile(persistedDataFile, fileType);
            return persistedNetwork;
        }).orElseThrow(() -> new NotFoundBussExc("Network with id " + networkId + " not found"));
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
    public Optional<DataFile> findFile(String classPath) {
        return this.dataFileRepo.findOne(Specification.where(DataFileSpec.hasClasspath(classPath)));
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
